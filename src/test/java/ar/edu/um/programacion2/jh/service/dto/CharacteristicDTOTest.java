package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class CharacteristicDTOTest {

    @Test
    void verifyMapsCharacteristicJsonCorrectly() throws IOException {
        String jsonData =
            """
            [
                { "id": 4, "nombre": "Pantalla", "descripcion": "Pantalla OLED 16\\"" },
                { "id": 5, "nombre": "Camara", "descripcion": "Camara Web 1080p" },
                { "id": 6, "nombre": "Batería", "descripcion": "Batería 80Wh" },
                { "id": 7, "nombre": "Adicional", "descripcion": "Lector de memoria múltiple" }
            ]
            """;

        ObjectMapper objectMapper = new ObjectMapper();
        List<CharacteristicDTO> characteristics = objectMapper.readValue(jsonData, new TypeReference<List<CharacteristicDTO>>() {});

        assertEquals(4, characteristics.size());

        assertTrue(
            characteristics
                .stream()
                .anyMatch(c -> c.getId().equals(4L) && c.getName().equals("Pantalla") && c.getDescription().equals("Pantalla OLED 16\""))
        );
        assertTrue(
            characteristics
                .stream()
                .anyMatch(c -> c.getId().equals(5L) && c.getName().equals("Camara") && c.getDescription().equals("Camara Web 1080p"))
        );
        assertTrue(
            characteristics
                .stream()
                .anyMatch(c -> c.getId().equals(6L) && c.getName().equals("Batería") && c.getDescription().equals("Batería 80Wh"))
        );
        assertTrue(
            characteristics
                .stream()
                .anyMatch(
                    c -> c.getId().equals(7L) && c.getName().equals("Adicional") && c.getDescription().equals("Lector de memoria múltiple")
                )
        );
    }

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

    @Test
    void fromCharacteristicConvertsCorrectly() {
        Characteristic characteristic = new Characteristic();
        characteristic.setId(1L);
        characteristic.setSupplierForeignId(10L);
        characteristic.setName("Screen");
        characteristic.setDescription("OLED Screen");

        CharacteristicDTO dto = CharacteristicDTO.fromCharacteristic(characteristic);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("Screen", dto.getName());
        assertEquals("OLED Screen", dto.getDescription());
    }

    @Test
    void fromCharacteristicHandlesNullValues() {
        Characteristic characteristic = new Characteristic();
        characteristic.setId(null);
        characteristic.setSupplierForeignId(null);
        characteristic.setName(null);
        characteristic.setDescription(null);

        CharacteristicDTO dto = CharacteristicDTO.fromCharacteristic(characteristic);

        assertNull(dto.getId());
        assertNull(dto.getSupplierForeignId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
    }

    @Test
    void fromCharacteristicHandlesEmptyStrings() {
        Characteristic characteristic = new Characteristic();
        characteristic.setId(1L);
        characteristic.setSupplierForeignId(10L);
        characteristic.setName("");
        characteristic.setDescription("");

        CharacteristicDTO dto = CharacteristicDTO.fromCharacteristic(characteristic);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("", dto.getName());
        assertEquals("", dto.getDescription());
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalObjects() {
        CharacteristicDTO dto1 = new CharacteristicDTO(1L, 10L, "Screen", "OLED Screen");
        CharacteristicDTO dto2 = new CharacteristicDTO(10L, null, "Screen", "OLED Screen");
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentIds() {
        CharacteristicDTO dto1 = new CharacteristicDTO(1L, 10L, "Screen", "OLED Screen");
        CharacteristicDTO dto2 = new CharacteristicDTO(20L, null, "Screen", "OLED Screen");
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentFields() {
        CharacteristicDTO dto1 = new CharacteristicDTO(1L, 10L, "Screen", "OLED Screen");
        CharacteristicDTO dto2 = new CharacteristicDTO(10L, null, "Screen", "LCD Screen");
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForNullFields() {
        CharacteristicDTO dto1 = new CharacteristicDTO(1L, 10L, null, null);
        CharacteristicDTO dto2 = new CharacteristicDTO(10L, null, null, null);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForNullAndNonNullFields() {
        CharacteristicDTO dto1 = new CharacteristicDTO(1L, 10L, "Screen", "OLED Screen");
        CharacteristicDTO dto2 = new CharacteristicDTO(10L, null, null, null);
        assertFalse(dto1.equalsExternal(dto2));
    }
}
