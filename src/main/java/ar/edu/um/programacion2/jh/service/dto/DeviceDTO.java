package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonIgnore
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
        device.setCustomizations(
            dto.getCustomizations() != null
                ? dto.getCustomizations().stream().map(CustomizationDTO::toCustomization).collect(Collectors.toSet())
                : Collections.emptySet()
        );
        return device;
    }

    public static DeviceDTO fromDevice(Device device) {
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
        dto.setCustomizations(
            device.getCustomizations() != null
                ? device.getCustomizations().stream().map(CustomizationDTO::fromCustomization).collect(Collectors.toList())
                : Collections.emptyList()
        );
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
            (this.characteristics == null) != (externalDevice.getCharacteristics() == null) ||
            (this.customizations == null) != (externalDevice.getCustomizations() == null) ||
            (this.extras == null) != (externalDevice.getExtras() == null)
        ) return false;
        if (
            (this.characteristics != null && this.characteristics.size() != externalDevice.getCharacteristics().size()) ||
            (this.customizations != null && this.customizations.size() != externalDevice.getCustomizations().size()) ||
            (this.extras != null && this.extras.size() != externalDevice.getExtras().size())
        ) return false;
        if (this.characteristics != null) {
            for (CharacteristicDTO localCharacteristic : this.characteristics) {
                boolean matchFound = externalDevice
                    .getCharacteristics()
                    .stream()
                    .anyMatch(
                        externalCharacteristic ->
                            Objects.equals(localCharacteristic.getSupplierForeignId(), externalCharacteristic.getId()) &&
                            localCharacteristic.equalsExternal(externalCharacteristic)
                    );
                if (!matchFound) {
                    return false;
                }
            }
        }
        if (this.customizations != null) {
            for (CustomizationDTO localCustomization : this.customizations) {
                boolean matchFound = externalDevice
                    .getCustomizations()
                    .stream()
                    .anyMatch(
                        externalCustomization ->
                            Objects.equals(localCustomization.getSupplierForeignId(), externalCustomization.getId()) &&
                            localCustomization.equalsExternal(externalCustomization)
                    );
                if (!matchFound) {
                    return false;
                }
            }
        }
        if (this.extras != null) {
            for (ExtraDTO localExtra : this.extras) {
                boolean matchFound = externalDevice
                    .getExtras()
                    .stream()
                    .anyMatch(
                        externalExtra ->
                            Objects.equals(localExtra.getSupplierForeignId(), externalExtra.getId()) &&
                            localExtra.equalsExternal(externalExtra)
                    );
                if (!matchFound) {
                    return false;
                }
            }
        }
        return true;
    }
}
