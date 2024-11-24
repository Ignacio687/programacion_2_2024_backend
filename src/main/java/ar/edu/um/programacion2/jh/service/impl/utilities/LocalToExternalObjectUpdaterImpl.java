package ar.edu.um.programacion2.jh.service.impl.utilities;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.dto.CustomizationDTO;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import ar.edu.um.programacion2.jh.service.utilities.LocalToExternalObjectUpdater;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class LocalToExternalObjectUpdaterImpl implements LocalToExternalObjectUpdater {

    private final DeviceRepository deviceRepository;
    private final CharacteristicRepository characteristicRepository;
    private final ExtraRepository extraRepository;
    private final OptionRepository optionRepository;
    private final CustomizationRepository customizationRepository;

    @Autowired
    public LocalToExternalObjectUpdaterImpl(
        DeviceRepository deviceRepository,
        CharacteristicRepository characteristicRepository,
        ExtraRepository extraRepository,
        OptionRepository optionRepository,
        CustomizationRepository customizationRepository
    ) {
        this.deviceRepository = deviceRepository;
        this.characteristicRepository = characteristicRepository;
        this.extraRepository = extraRepository;
        this.optionRepository = optionRepository;
        this.customizationRepository = customizationRepository;
    }

    @Override
    public Device updateAndSaveDevice(DeviceDTO externalDevice, String supplier) {
        Optional<Device> localDevice = this.deviceRepository.findBySupplierForeignId(externalDevice.getId());
        if (localDevice.isPresent()) {
            return this.updateAndSaveDevice(localDevice.get(), externalDevice);
        } else {
            Device newDevice = new Device();
            newDevice.setId(null);
            newDevice.setSupplierForeignId(externalDevice.getId());
            newDevice.setSupplier(supplier);
            return this.updateAndSaveDevice(newDevice, externalDevice);
        }
    }

    @Override
    public Device updateAndSaveDevice(Device localDevice, DeviceDTO externalDevice) {
        Device updatedDevice = DeviceDTO.toDevice(externalDevice);
        updatedDevice.setId(localDevice.getId());
        updatedDevice.setSupplierForeignId(localDevice.getSupplierForeignId());
        updatedDevice.setSupplier(localDevice.getSupplier());
        updatedDevice.setActive(true);
        Device newDevice = this.saveDeviceDependencies(updatedDevice);
        return this.deviceRepository.save(newDevice);
    }

    private Device saveDeviceDependencies(Device device) {
        Set<Characteristic> savedCharacteristics = device
            .getCharacteristics()
            .stream()
            .map(this::updateAndSaveCharacteristic)
            .collect(Collectors.toSet());
        device.setCharacteristics(savedCharacteristics);

        Set<Extra> savedExtras = device.getExtras().stream().map(this::updateAndSaveExtra).collect(Collectors.toSet());
        device.setExtras(savedExtras);

        Set<Customization> savedCustomizations = device
            .getCustomizations()
            .stream()
            .map(this::updateAndSaveCustomization)
            .collect(Collectors.toSet());
        device.setCustomizations(savedCustomizations);

        return device;
    }

    @Override
    public Characteristic updateAndSaveCharacteristic(Characteristic externalCharacteristic) {
        Optional<Characteristic> localCharacteristic =
            this.characteristicRepository.findBySupplierForeignId(externalCharacteristic.getId());
        if (localCharacteristic.isPresent()) {
            return this.updateAndSaveCharacteristic(localCharacteristic.get(), externalCharacteristic);
        } else {
            Characteristic newCharacteristic = new Characteristic();
            newCharacteristic.setId(null);
            newCharacteristic.setSupplierForeignId(externalCharacteristic.getId());
            return this.updateAndSaveCharacteristic(newCharacteristic, externalCharacteristic);
        }
    }

    @Override
    public Characteristic updateAndSaveCharacteristic(Characteristic localCharacteristic, Characteristic externalCharacteristic) {
        externalCharacteristic.setSupplierForeignId(externalCharacteristic.getId());
        externalCharacteristic.setId(localCharacteristic.getId());
        return this.characteristicRepository.save(externalCharacteristic);
    }

    @Override
    public Extra updateAndSaveExtra(Extra externalExtra) {
        Optional<Extra> localExtra = this.extraRepository.findBySupplierForeignId(externalExtra.getId());
        if (localExtra.isPresent()) {
            return this.updateAndSaveExtra(localExtra.get(), externalExtra);
        } else {
            Extra newExtra = new Extra();
            newExtra.setId(null);
            newExtra.setSupplierForeignId(externalExtra.getId());
            return this.updateAndSaveExtra(newExtra, externalExtra);
        }
    }

    @Override
    public Extra updateAndSaveExtra(Extra localExtra, Extra externalExtra) {
        externalExtra.setSupplierForeignId(externalExtra.getId());
        externalExtra.setId(localExtra.getId());
        return this.extraRepository.save(externalExtra);
    }

    @Override
    public Option updateAndSaveOption(Option externalOption) {
        Optional<Option> localOption = this.optionRepository.findBySupplierForeignId(externalOption.getId());
        if (localOption.isPresent()) {
            return this.updateAndSaveOption(localOption.get(), externalOption);
        } else {
            Option newOption = new Option();
            newOption.setId(null);
            newOption.setSupplierForeignId(externalOption.getId());
            return this.updateAndSaveOption(newOption, externalOption);
        }
    }

    @Override
    public Option updateAndSaveOption(Option localOption, Option externalOption) {
        externalOption.setSupplierForeignId(externalOption.getId());
        externalOption.setId(localOption.getId());
        return this.optionRepository.save(externalOption);
    }

    @Override
    public Customization updateAndSaveCustomization(Customization externalCustomization) {
        Optional<Customization> localCustomization = this.customizationRepository.findBySupplierForeignId(externalCustomization.getId());
        if (localCustomization.isPresent()) {
            return this.updateAndSaveCustomization(localCustomization.get(), externalCustomization);
        } else {
            Customization newCustomization = new Customization();
            newCustomization.setId(null);
            newCustomization.setSupplierForeignId(externalCustomization.getId());
            return this.updateAndSaveCustomization(newCustomization, externalCustomization);
        }
    }

    @Override
    public Customization updateAndSaveCustomization(Customization localCustomization, Customization externalCustomization) {
        externalCustomization.setSupplierForeignId(externalCustomization.getId());
        externalCustomization.setId(localCustomization.getId());
        Set<Option> updatedOptions = externalCustomization.getOptions().stream().map(this::updateAndSaveOption).collect(Collectors.toSet());
        externalCustomization.setOptions(updatedOptions);
        return this.customizationRepository.save(externalCustomization);
    }
}
