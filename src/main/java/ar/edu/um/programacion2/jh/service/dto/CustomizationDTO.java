package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Customization;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomizationDTO {

    @JsonProperty("id")
    private Long id;

    private Long supplierForeignId;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("opciones")
    private List<OptionDTO> options = Collections.emptyList();

    public static Customization toCustomization(CustomizationDTO dto) {
        Customization customization = new Customization();
        customization.setId(dto.getId());
        customization.setSupplierForeignId(dto.getSupplierForeignId());
        customization.setName(dto.getName());
        customization.setDescription(dto.getDescription());
        customization.setOptions(
            dto.getOptions() != null
                ? dto.getOptions().stream().map(OptionDTO::toOption).collect(Collectors.toSet())
                : Collections.emptySet()
        );
        return customization;
    }

    public static CustomizationDTO fromCustomization(Customization customization) {
        CustomizationDTO dto = new CustomizationDTO();
        dto.setId(customization.getId());
        dto.setSupplierForeignId(customization.getSupplierForeignId());
        dto.setName(customization.getName());
        dto.setDescription(customization.getDescription());
        dto.setOptions(
            customization.getOptions() != null
                ? customization.getOptions().stream().map(OptionDTO::fromOption).collect(Collectors.toList())
                : Collections.emptyList()
        );
        return dto;
    }

    public boolean equalsExternal(CustomizationDTO externalCustomization) {
        if (this == externalCustomization) return true;
        if (externalCustomization == null || getClass() != externalCustomization.getClass()) return false;
        if (
            !Objects.equals(this.supplierForeignId, externalCustomization.getId()) ||
            !Objects.equals(this.name, externalCustomization.getName()) ||
            !Objects.equals(this.description, externalCustomization.getDescription())
        ) {
            return false;
        }
        if (this.options == null && externalCustomization.getOptions() == null) return true;
        if (this.options == null || externalCustomization.getOptions() == null) return false;
        if (this.options.size() != externalCustomization.getOptions().size()) return false;
        for (OptionDTO localOption : this.options) {
            boolean matchFound = externalCustomization
                .getOptions()
                .stream()
                .anyMatch(
                    externalOption ->
                        Objects.equals(localOption.getSupplierForeignId(), externalOption.getId()) &&
                        localOption.equalsExternal(externalOption)
                );
            if (!matchFound) {
                return false;
            }
        }
        return true;
    }
}
