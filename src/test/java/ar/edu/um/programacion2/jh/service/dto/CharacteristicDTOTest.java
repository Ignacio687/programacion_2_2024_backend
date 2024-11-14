package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import org.junit.jupiter.api.Test;

public class CharacteristicDTOTest {

    @Test
    void toCharacteristicConvertsCorrectly() {
        CharacteristicDTO dto = new CharacteristicDTO(1L, 10L, "Screen", "OLED Screen");
        Characteristic characteristic = CharacteristicDTO.toCharacteristic(dto);

        assertEquals(1L, characteristic.getId());
        assertEquals(10L, characteristic.getSupplierForeignId());
        assertEquals("Screen", characteristic.getName());
        assertEquals("OLED Screen", characteristic.getDescription());
    }

    @Test
    void toCharacteristicHandlesNullValues() {
        CharacteristicDTO dto = new CharacteristicDTO(null, null, null, null);
        Characteristic characteristic = CharacteristicDTO.toCharacteristic(dto);

        assertNull(characteristic.getId());
        assertNull(characteristic.getSupplierForeignId());
        assertNull(characteristic.getName());
        assertNull(characteristic.getDescription());
    }

    @Test
    void toCharacteristicHandlesEmptyStrings() {
        CharacteristicDTO dto = new CharacteristicDTO(1L, 10L, "", "");
        Characteristic characteristic = CharacteristicDTO.toCharacteristic(dto);

        assertEquals(1L, characteristic.getId());
        assertEquals(10L, characteristic.getSupplierForeignId());
        assertEquals("", characteristic.getName());
        assertEquals("", characteristic.getDescription());
    }
}
