package ar.edu.um.programacion2.jh.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequestDTO implements Serializable {

    @JsonProperty("idDispositivo")
    private Long deviceId;

    @JsonProperty("personalizaciones")
    private List<SaleItemDTO> customizations;

    @JsonProperty("adicionales")
    private List<SaleItemDTO> extras;

    @JsonProperty("precioFinal")
    private Double finalPrice;

    @JsonProperty("fechaVenta")
    private String saleDate;
}
