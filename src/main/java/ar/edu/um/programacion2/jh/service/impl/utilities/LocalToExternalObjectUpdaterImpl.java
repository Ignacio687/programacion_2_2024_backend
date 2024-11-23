package ar.edu.um.programacion2.jh.service.impl.utilities;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.dto.CustomizationDTO;
import ar.edu.um.programacion2.jh.service.dto.DeviceDTO;
import ar.edu.um.programacion2.jh.service.utilities.LocalToExternalObjectUpdater;
import java.util.*;
import java.util.stream.Collectors;
import liquibase.command.core.MarkNextChangesetRanCommandStep;
import lombok.Data;
import org.hibernate.ObjectNotFoundException;
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
        Optional<Device> localDevice = this.deviceRepository.findBySupplierForeignId(externalDevice.getSupplierForeignId());
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
        this.saveDeviceDependencies(updatedDevice, externalDevice.getCustomizations());
        return this.deviceRepository.save(updatedDevice);
    }

    private Device saveDeviceDependencies(Device device, List<CustomizationDTO> customizations) {
        device
            .getCharacteristics()
            .forEach(characteristic -> {
                Optional<Characteristic> existingCharacteristic = characteristicRepository.findBySupplierForeignId(
                    characteristic.getSupplierForeignId()
                );
                existingCharacteristic.ifPresent(value -> characteristic.setId(value.getId()));
            });
        return device;
    }

    @Override
    public Characteristic updateAndSaveCharacteristic(Characteristic externalCharacteristic) {
        Optional<Characteristic> localCharacteristic =
            this.characteristicRepository.findBySupplierForeignId(externalCharacteristic.getSupplierForeignId());
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
        Optional<Extra> localExtra = this.extraRepository.findBySupplierForeignId(externalExtra.getSupplierForeignId());
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
        Optional<Option> localOption = this.optionRepository.findBySupplierForeignId(externalOption.getSupplierForeignId());
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
        Optional<Customization> localCustomization =
            this.customizationRepository.findBySupplierForeignId(externalCustomization.getSupplierForeignId());
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
        Set<Option> updatedOptions = new TreeSet<>(Comparator.comparing(Option::getId));
        externalCustomization.setSupplierForeignId(externalCustomization.getId());
        externalCustomization.setId(localCustomization.getId());
        //Completar
        return localCustomization;
    }
}
