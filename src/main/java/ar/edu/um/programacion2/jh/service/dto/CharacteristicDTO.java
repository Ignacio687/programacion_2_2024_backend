package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
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

    public static CharacteristicDTO fromCharacteristic(Characteristic characteristic) {
        CharacteristicDTO dto = new CharacteristicDTO();
        dto.setId(characteristic.getId());
        dto.setSupplierForeignId(characteristic.getSupplierForeignId());
        dto.setName(characteristic.getName());
        dto.setDescription(characteristic.getDescription());
        return dto;
    }

    public boolean equalsExternal(CharacteristicDTO externalCharacteristic) {
        if (this == externalCharacteristic) return true;
        if (externalCharacteristic == null || getClass() != externalCharacteristic.getClass()) return false;
        return (
            Objects.equals(this.supplierForeignId, externalCharacteristic.getId()) &&
            Objects.equals(this.name, externalCharacteristic.getName()) &&
            Objects.equals(this.description, externalCharacteristic.getDescription())
        );
    }
}
