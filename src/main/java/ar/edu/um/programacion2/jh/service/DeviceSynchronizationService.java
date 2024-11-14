package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.client.DeviceClient;
import ar.edu.um.programacion2.jh.service.dto.CustomizationDTO;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Device> localDevices = this.deviceRepository.findAll();
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
        this.characteristicRepository.saveAll(device.getCharacteristics());
        this.extraRepository.saveAll(device.getExtras());
        this.optionRepository.saveAll(device.getOptions());
        for (CustomizationDTO customizationDTO : customizations) {
            Customization customization = CustomizationDTO.toCustomization(customizationDTO);
            this.customizationRepository.save(customization);
        }
    }

    private void deactivateDevice(Device localDevice) {
        localDevice.setActive(false);
        this.deviceRepository.save(localDevice);
    }

    public void startThread(Long syncTimeLapse) {
        if (thread != null && thread.isAlive()) {
            this.stopThread();
        }
        this.running = true;
        thread = new Thread(() -> {
            while (this.running) {
                this.synchronize();
                try {
                    Thread.sleep(syncTimeLapse);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    this.log.info("Device synchronization was interrupted");
                } catch (Exception e) {
                    this.log.error("An unexpected error occurred during device synchronization", e);
                    this.log.trace("An unexpected error occurred during device synchronization", e);
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
        }
    }
}
