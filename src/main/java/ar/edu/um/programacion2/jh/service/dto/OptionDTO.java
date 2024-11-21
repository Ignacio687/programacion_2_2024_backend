package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Option;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    private Long supplierForeignId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("precioAdicional")
    private Double additionalPrice;

    public static Option toOption(OptionDTO dto) {
        Option option = new Option();
        option.setId(dto.getId());
        option.setSupplierForeignId(dto.getSupplierForeignId());
        option.setCode(dto.getCode());
        option.setName(dto.getName());
        option.setDescription(dto.getDescription());
        option.setAdditionalPrice(dto.getAdditionalPrice());
        return option;
    }

    public static OptionDTO fromOption(Option option) {
        OptionDTO dto = new OptionDTO();
        dto.setId(option.getId());
        dto.setSupplierForeignId(option.getSupplierForeignId());
        dto.setCode(option.getCode());
        dto.setName(option.getName());
        dto.setDescription(option.getDescription());
        dto.setAdditionalPrice(option.getAdditionalPrice());
        return dto;
    }

    public boolean equalsExternal(OptionDTO externalOption) {
        if (this == externalOption) return true;
        if (externalOption == null || getClass() != externalOption.getClass()) return false;
        return (
            Objects.equals(this.supplierForeignId, externalOption.getId()) &&
            Objects.equals(this.code, externalOption.getCode()) &&
            Objects.equals(this.name, externalOption.getName()) &&
            Objects.equals(this.description, externalOption.getDescription()) &&
            Objects.equals(this.additionalPrice, externalOption.getAdditionalPrice())
        );
    }
}
