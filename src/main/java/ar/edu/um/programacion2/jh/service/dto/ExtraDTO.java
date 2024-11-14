package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Extra;
import com.fasterxml.jackson.annotation.JsonProperty;
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
}
