package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Sale;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleListDTO implements Serializable {

    @JsonProperty("idVenta")
    private Long saleId;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("precio")
    private Double price;

    public static SaleListDTO fromSaleItem(Sale sale) {
        SaleListDTO dto = new SaleListDTO();
        dto.setSaleId(sale.getId());
        dto.setId(sale.getDevice().getId());
        dto.setCode(sale.getDevice().getCode());
        dto.setName(sale.getDevice().getName());
        dto.setDescription(sale.getDevice().getDescription());
        dto.setPrice(sale.getDevicePrice());
        return dto;
    }
}
