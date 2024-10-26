package ar.edu.um.programacion2.jh.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("precio")
    private Double price;
}
