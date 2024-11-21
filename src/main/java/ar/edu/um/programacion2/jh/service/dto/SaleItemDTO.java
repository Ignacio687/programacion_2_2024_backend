package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemDTO implements Serializable {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("precio")
    private Double price;

    public static SaleItem toSaleItem(SaleItemDTO dto) {
        SaleItem saleItem = new SaleItem();
        saleItem.setId(dto.getId());
        saleItem.setPrice(dto.getPrice());
        return saleItem;
    }
}
