package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Option;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDTO {

    @JsonProperty("id")
    private Long id;

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
        option.setCode(dto.getCode());
        option.setName(dto.getName());
        option.setDescription(dto.getDescription());
        option.setAdditionalPrice(dto.getAdditionalPrice());
        return option;
    }
}
