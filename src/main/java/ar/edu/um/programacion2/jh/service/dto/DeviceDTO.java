package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {

    @JsonProperty("id")
    private Long id;

    private Long supplierForeignId;

    private String supplier;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("precioBase")
    private Double basePrice;

    @JsonProperty("moneda")
    private String currency;

    private Boolean active;

    @JsonProperty("caracteristicas")
    private List<CharacteristicDTO> characteristics;

    @JsonProperty("personalizaciones")
    private List<CustomizationDTO> customizations;

    @JsonProperty("adicionales")
    private List<ExtraDTO> extras;

    public static Device toDevice(DeviceDTO dto) {
        Device device = new Device();
        device.setId(dto.getId());
        device.setSupplierForeignId(dto.getSupplierForeignId());
        device.setSupplier(dto.getSupplier());
        device.setCode(dto.getCode());
        device.setName(dto.getName());
        device.setDescription(dto.getDescription());
        device.setBasePrice(dto.getBasePrice());
        device.setCurrency(dto.getCurrency());
        device.setActive(dto.getActive());
        device.setCharacteristics(
            dto.getCharacteristics() != null
                ? dto.getCharacteristics().stream().map(CharacteristicDTO::toCharacteristic).collect(Collectors.toSet())
                : Collections.emptySet()
        );
        device.setExtras(
            dto.getExtras() != null ? dto.getExtras().stream().map(ExtraDTO::toExtra).collect(Collectors.toSet()) : Collections.emptySet()
        );
        // Collect options from customizations
        device.setOptions(
            dto.getCustomizations() != null
                ? dto
                    .getCustomizations()
                    .stream()
                    .flatMap(customization -> customization.getOptions().stream())
                    .map(OptionDTO::toOption)
                    .collect(Collectors.toSet())
                : Collections.emptySet()
        );
        return device;
    }

    public static DeviceDTO fromDevice(Device device, CustomizationRepository customizationRepository) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(device.getId());
        dto.setSupplierForeignId(device.getSupplierForeignId());
        dto.setSupplier(device.getSupplier());
        dto.setCode(device.getCode());
        dto.setName(device.getName());
        dto.setDescription(device.getDescription());
        dto.setBasePrice(device.getBasePrice());
        dto.setCurrency(device.getCurrency());
        dto.setActive(device.getActive());
        dto.setCharacteristics(
            device.getCharacteristics() != null
                ? device.getCharacteristics().stream().map(CharacteristicDTO::fromCharacteristic).collect(Collectors.toList())
                : Collections.emptyList()
        );
        dto.setExtras(
            device.getExtras() != null
                ? device.getExtras().stream().map(ExtraDTO::fromExtra).collect(Collectors.toList())
                : Collections.emptyList()
        );
        // Group options by their customizations
        if (device.getOptions() != null) {
            Map<Long, CustomizationDTO> customizationMap = new HashMap<>();
            for (Option option : device.getOptions()) {
                Customization customization = customizationRepository.findByOptionsContains(option);
                if (customization != null) {
                    CustomizationDTO customizationDTO = customizationMap.computeIfAbsent(customization.getId(), id ->
                        CustomizationDTO.fromCustomization(customization)
                    );
                    customizationDTO.getOptions().add(OptionDTO.fromOption(option));
                }
            }
            dto.setCustomizations(new ArrayList<>(customizationMap.values()));
        } else {
            dto.setCustomizations(Collections.emptyList());
        }
        return dto;
    }

    public boolean equalsExternal(DeviceDTO externalDevice) {
        if (this == externalDevice) return true;
        if (externalDevice == null || getClass() != externalDevice.getClass()) return false;
        if (
            !Objects.equals(this.supplierForeignId, externalDevice.getId()) ||
            !Objects.equals(this.code, externalDevice.getCode()) ||
            !Objects.equals(this.name, externalDevice.getName()) ||
            !Objects.equals(this.description, externalDevice.getDescription()) ||
            !Objects.equals(this.basePrice, externalDevice.getBasePrice()) ||
            !Objects.equals(this.currency, externalDevice.getCurrency())
        ) {
            return false;
        }
        if (
            this.characteristics == null &&
            externalDevice.getCharacteristics() == null &&
            this.customizations == null &&
            externalDevice.getCustomizations() == null &&
            this.extras == null &&
            externalDevice.getExtras() == null
        ) return true;
        if (
            this.characteristics == null ||
            externalDevice.getCharacteristics() == null ||
            this.customizations == null ||
            externalDevice.getCustomizations() == null ||
            this.extras == null ||
            externalDevice.getExtras() == null
        ) return false;
        if (
            this.characteristics.size() != externalDevice.getCharacteristics().size() ||
            this.customizations.size() != externalDevice.getCustomizations().size() ||
            this.extras.size() != externalDevice.getExtras().size()
        ) return false;
        for (CharacteristicDTO localCharacteristic : this.characteristics) {
            boolean matchFound = externalDevice
                .getCharacteristics()
                .stream()
                .anyMatch(
                    externalCharacteristic ->
                        localCharacteristic.getSupplierForeignId().equals(externalCharacteristic.getId()) &&
                        localCharacteristic.equalsExternal(externalCharacteristic)
                );
            if (!matchFound) {
                return false;
            }
        }
        for (CustomizationDTO localCustomization : this.customizations) {
            boolean matchFound = externalDevice
                .getCustomizations()
                .stream()
                .anyMatch(
                    externalCustomization ->
                        localCustomization.getSupplierForeignId().equals(externalCustomization.getId()) &&
                        localCustomization.equalsExternal(externalCustomization)
                );
            if (!matchFound) {
                return false;
            }
        }
        for (ExtraDTO localExtra : this.extras) {
            boolean matchFound = externalDevice
                .getExtras()
                .stream()
                .anyMatch(
                    externalExtra ->
                        localExtra.getSupplierForeignId().equals(externalExtra.getId()) && localExtra.equalsExternal(externalExtra)
                );
            if (!matchFound) {
                return false;
            }
        }
        return true;
    }
}
