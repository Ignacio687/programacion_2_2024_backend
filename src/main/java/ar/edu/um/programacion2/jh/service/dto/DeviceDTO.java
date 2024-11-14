package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Device;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
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

    private Long supplierForeignKey;

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
        device.setSupplierForeignKey(dto.getSupplierForeignKey());
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
}
