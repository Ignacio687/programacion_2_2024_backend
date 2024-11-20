package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.client.DeviceClient;
import ar.edu.um.programacion2.jh.service.dto.CustomizationDTO;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class DeviceSynchronizationService {

    private Thread thread;
    private volatile boolean running = true;
    private final Logger log = LoggerFactory.getLogger(DeviceSynchronizationService.class);

    @Autowired
    protected DeviceClient deviceClient;

    @Autowired
    protected DeviceRepository deviceRepository;

    @Autowired
    protected CharacteristicRepository characteristicRepository;

    @Autowired
    protected ExtraRepository extraRepository;

    @Autowired
    protected OptionRepository optionRepository;

    @Autowired
    protected CustomizationRepository customizationRepository;

    public void synchronize() {
        this.log.info("Synchronizing devices...");
        List<DeviceDTO> externalDevices = this.deviceClient.getDevices();
        List<Device> localDevices = this.deviceRepository.findAllWithEagerRelationshipsCustom();
        if (hasChanges(externalDevices, localDevices)) {
            this.log.info("No changes detected between local and external devices.");
            return;
        }
        for (Device localDevice : localDevices) {
            Optional<DeviceDTO> matchingDevice = externalDevices
                .stream()
                .filter(externalDevice -> externalDevice.getId().equals(localDevice.getSupplierForeignId()))
                .findFirst();
            if (matchingDevice.isPresent()) {
                this.updateDevice(localDevice, matchingDevice.get());
                externalDevices.remove(matchingDevice.get());
            } else {
                this.deactivateDevice(localDevice);
            }
        }
        for (DeviceDTO externalDevice : externalDevices) {
            Device newDevice = new Device();
            newDevice.setId(null);
            newDevice.setSupplierForeignId(externalDevice.getId());
            newDevice.setSupplier("APICatedraProgramacion2_2024");
            this.updateDevice(newDevice, externalDevice);
        }
        this.log.info("Device synchronization completed.");
    }

    private boolean hasChanges(List<DeviceDTO> externalDevices, List<Device> localDevices) {
        TreeSet<DeviceDTO> sortedExternalDevices = new TreeSet<>(Comparator.comparing(DeviceDTO::getId));
        sortedExternalDevices.addAll(externalDevices);
        TreeSet<DeviceDTO> sortedLocalDevices = localDevices
            .stream()
            .filter(Device::getActive)
            .map(device -> DeviceDTO.fromDevice(device, customizationRepository))
            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DeviceDTO::getSupplierForeignId))));
        if (sortedExternalDevices.size() != sortedLocalDevices.size()) {
            return false;
        }
        Iterator<DeviceDTO> externalIterator = sortedExternalDevices.iterator();
        Iterator<DeviceDTO> localIterator = sortedLocalDevices.iterator();
        while (externalIterator.hasNext() && localIterator.hasNext()) {
            DeviceDTO externalDevice = externalIterator.next();
            DeviceDTO localDevice = localIterator.next();
            if (!localDevice.equalsExternal(externalDevice)) {
                return false;
            }
        }
        return true;
    }

    private void updateDevice(Device localDevice, DeviceDTO externalDevice) {
        Device updatedDevice = DeviceDTO.toDevice(externalDevice);
        updatedDevice.setId(localDevice.getId());
        updatedDevice.setSupplierForeignId(localDevice.getSupplierForeignId());
        updatedDevice.setSupplier(localDevice.getSupplier());
        updatedDevice.setActive(true);
        this.saveDeviceDependencies(updatedDevice, externalDevice.getCustomizations());
        this.deviceRepository.save(updatedDevice);
    }

    private void saveDeviceDependencies(Device device, List<CustomizationDTO> customizations) {
        device
            .getCharacteristics()
            .forEach(characteristic -> {
                characteristic.setSupplierForeignId(characteristic.getId());
                Optional<Characteristic> existingCharacteristic = characteristicRepository.findBySupplierForeignId(
                    characteristic.getSupplierForeignId()
                );
                existingCharacteristic.ifPresent(value -> characteristic.setId(value.getId()));
            });
        device.setCharacteristics(new HashSet<>(this.characteristicRepository.saveAll(device.getCharacteristics())));

        device
            .getExtras()
            .forEach(extra -> {
                extra.setSupplierForeignId(extra.getId());
                Optional<Extra> existingExtra = extraRepository.findBySupplierForeignId(extra.getSupplierForeignId());
                existingExtra.ifPresent(value -> extra.setId(value.getId()));
            });
        device.setExtras(new HashSet<>(this.extraRepository.saveAll(device.getExtras())));

        device
            .getOptions()
            .forEach(option -> {
                option.setSupplierForeignId(option.getId());
                Optional<Option> existingOption = optionRepository.findBySupplierForeignId(option.getSupplierForeignId());
                existingOption.ifPresent(value -> option.setId(value.getId()));
            });
        device.setOptions(new HashSet<>(this.optionRepository.saveAll(device.getOptions())));

        for (CustomizationDTO customizationDTO : customizations) {
            Customization customization = CustomizationDTO.toCustomization(customizationDTO);
            customization.setSupplierForeignId(customization.getId());
            Optional<Customization> existingCustomization = customizationRepository.findBySupplierForeignId(
                customization.getSupplierForeignId()
            );
            existingCustomization.ifPresent(value -> customization.setId(value.getId()));
            Set<Option> newOptions = device
                .getOptions()
                .stream()
                .filter(option ->
                    customization
                        .getOptions()
                        .stream()
                        .anyMatch(existingOption -> existingOption.getId().equals(option.getSupplierForeignId()))
                )
                .collect(Collectors.toSet());
            customization.setOptions(newOptions);
            this.customizationRepository.save(customization);
        }
    }

    private void deactivateDevice(Device localDevice) {
        localDevice.setActive(false);
        this.deviceRepository.save(localDevice);
    }

    public void startThread(Long syncTimeLapse) {
        if (thread != null) {
            this.stopThread();
        }
        this.running = true;
        thread = new Thread(() -> {
            while (this.running) {
                try {
                    this.synchronize();
                    Thread.sleep(syncTimeLapse * 1000);
                } catch (InterruptedException e) {
                    this.log.debug("Device synchronization was interrupted");
                    break;
                } catch (Exception e) {
                    this.log.error("An unexpected error occurred during device synchronization", e);
                    this.log.trace("An unexpected error occurred during device synchronization", e);
                    break;
                }
            }
            this.log.info("Device synchronization has stopped.");
        });
        this.thread.start();
    }

    public void stopThread() {
        this.running = false;
        if (thread != null) {
            this.thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                this.log.debug("Thread join was interrupted", e);
            }
        }
    }
}
