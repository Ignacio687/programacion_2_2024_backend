package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Customization;
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
public class CustomizationDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("opciones")
    private List<OptionDTO> options = Collections.emptyList();

    public static Customization toCustomization(CustomizationDTO dto) {
        Customization customization = new Customization();
        customization.setId(dto.getId());
        customization.setName(dto.getName());
        customization.setDescription(dto.getDescription());
        customization.setOptions(
            dto.getOptions() != null
                ? dto.getOptions().stream().map(OptionDTO::toOption).collect(Collectors.toSet())
                : Collections.emptySet()
        );
        return customization;
    }
}
