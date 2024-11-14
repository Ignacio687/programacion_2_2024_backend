package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacteristicDTO {

    @JsonProperty("id")
    private Long id;

    private Long supplierForeignId;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    public static Characteristic toCharacteristic(CharacteristicDTO dto) {
        Characteristic characteristic = new Characteristic();
        characteristic.setId(dto.getId());
        characteristic.setSupplierForeignId(dto.getSupplierForeignId());
        characteristic.setName(dto.getName());
        characteristic.setDescription(dto.getDescription());
        return characteristic;
    }
}
