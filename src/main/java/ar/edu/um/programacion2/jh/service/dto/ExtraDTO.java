package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Extra;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraDTO {

    @JsonProperty("id")
    private Long id;

    private Long supplierForeignId;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("precio")
    private Double price;

    @JsonProperty("precioGratis")
    private Double freePrice;

    public static Extra toExtra(ExtraDTO dto) {
        Extra extra = new Extra();
        extra.setId(dto.getId());
        extra.setSupplierForeignId(dto.getSupplierForeignId());
        extra.setName(dto.getName());
        extra.setDescription(dto.getDescription());
        extra.setPrice(dto.getPrice());
        extra.setFreePrice(dto.getFreePrice());
        return extra;
    }

    public static ExtraDTO fromExtra(Extra extra) {
        ExtraDTO dto = new ExtraDTO();
        dto.setId(extra.getId());
        dto.setSupplierForeignId(extra.getSupplierForeignId());
        dto.setName(extra.getName());
        dto.setDescription(extra.getDescription());
        dto.setPrice(extra.getPrice());
        dto.setFreePrice(extra.getFreePrice());
        return dto;
    }

    public boolean equalsExternal(ExtraDTO externalExtra) {
        if (this == externalExtra) return true;
        if (externalExtra == null || getClass() != externalExtra.getClass()) return false;
        return (
            Objects.equals(this.supplierForeignId, externalExtra.getId()) &&
            Objects.equals(this.name, externalExtra.getName()) &&
            Objects.equals(this.description, externalExtra.getDescription()) &&
            Objects.equals(this.price, externalExtra.getPrice()) &&
            Objects.equals(this.freePrice, externalExtra.getFreePrice())
        );
    }
}
