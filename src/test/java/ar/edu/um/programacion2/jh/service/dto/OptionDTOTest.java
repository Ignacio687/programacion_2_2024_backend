package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Option;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class OptionDTOTest {

    @Test
    void verifyMapsOptionsJsonCorrectly() throws IOException {
        String jsonData =
            """
            [
                { "id": 3, "codigo": "PROC03", "nombre": "Core Y1", "descripcion": "Procesador 1.2 GHz- 10 Cores", "precioAdicional": 0.0 },
                { "id": 4, "codigo": "PROC04", "nombre": "Core Y2", "descripcion": "Procesador 1.7 GHz- 24 Cores", "precioAdicional": 700.0 },
                { "id": 7, "codigo": "MEM03", "nombre": "DDR4-8", "descripcion": "Memoria DDR4- 8GB", "precioAdicional": 0.0 },
                { "id": 8, "codigo": "MEM04", "nombre": "DDR4-16", "descripcion": "Memoria DDR4- 16GB", "precioAdicional": 300.0 },
                { "id": 9, "codigo": "MEM05", "nombre": "DDR4-32", "descripcion": "Memoria DDR4- 32GB", "precioAdicional": 900.0 },
                { "id": 12, "codigo": "VID01", "nombre": "Integrado", "descripcion": "Video Integrado Memoria Compartida", "precioAdicional": 0.0 },
                { "id": 13, "codigo": "VID02", "nombre": "Aceleradora 3D", "descripcion": "Nvidia XYZ-200 12GB", "precioAdicional": 900.0 }
            ]
            """;

        ObjectMapper objectMapper = new ObjectMapper();
        List<OptionDTO> options = objectMapper.readValue(jsonData, new TypeReference<List<OptionDTO>>() {});

        assertEquals(7, options.size());

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(3L) &&
                        o.getCode().equals("PROC03") &&
                        o.getName().equals("Core Y1") &&
                        o.getDescription().equals("Procesador 1.2 GHz- 10 Cores") &&
                        o.getAdditionalPrice().equals(0.0)
                )
        );

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(4L) &&
                        o.getCode().equals("PROC04") &&
                        o.getName().equals("Core Y2") &&
                        o.getDescription().equals("Procesador 1.7 GHz- 24 Cores") &&
                        o.getAdditionalPrice().equals(700.0)
                )
        );

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(7L) &&
                        o.getCode().equals("MEM03") &&
                        o.getName().equals("DDR4-8") &&
                        o.getDescription().equals("Memoria DDR4- 8GB") &&
                        o.getAdditionalPrice().equals(0.0)
                )
        );

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(8L) &&
                        o.getCode().equals("MEM04") &&
                        o.getName().equals("DDR4-16") &&
                        o.getDescription().equals("Memoria DDR4- 16GB") &&
                        o.getAdditionalPrice().equals(300.0)
                )
        );

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(9L) &&
                        o.getCode().equals("MEM05") &&
                        o.getName().equals("DDR4-32") &&
                        o.getDescription().equals("Memoria DDR4- 32GB") &&
                        o.getAdditionalPrice().equals(900.0)
                )
        );

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(12L) &&
                        o.getCode().equals("VID01") &&
                        o.getName().equals("Integrado") &&
                        o.getDescription().equals("Video Integrado Memoria Compartida") &&
                        o.getAdditionalPrice().equals(0.0)
                )
        );

        assertTrue(
            options
                .stream()
                .anyMatch(
                    o ->
                        o.getId().equals(13L) &&
                        o.getCode().equals("VID02") &&
                        o.getName().equals("Aceleradora 3D") &&
                        o.getDescription().equals("Nvidia XYZ-200 12GB") &&
                        o.getAdditionalPrice().equals(900.0)
                )
        );
    }

    @Test
    void toOptionConvertsCorrectly() {
        OptionDTO dto = new OptionDTO(1L, 10L, "CODE01", "Option1", "Description1", 100.0);
        Option option = OptionDTO.toOption(dto);

        assertEquals(1L, option.getId());
        assertEquals(10L, option.getSupplierForeignId());
        assertEquals("CODE01", option.getCode());
        assertEquals("Option1", option.getName());
        assertEquals("Description1", option.getDescription());
        assertEquals(100.0, option.getAdditionalPrice());
    }

    @Test
    void toOptionHandlesNullValues() {
        OptionDTO dto = new OptionDTO(null, null, null, null, null, null);
        Option option = OptionDTO.toOption(dto);

        assertNull(option.getId());
        assertNull(option.getSupplierForeignId());
        assertNull(option.getCode());
        assertNull(option.getName());
        assertNull(option.getDescription());
        assertNull(option.getAdditionalPrice());
    }

    @Test
    void toOptionHandlesEmptyStrings() {
        OptionDTO dto = new OptionDTO(1L, 10L, "", "", "", 0.0);
        Option option = OptionDTO.toOption(dto);

        assertEquals(1L, option.getId());
        assertEquals(10L, option.getSupplierForeignId());
        assertEquals("", option.getCode());
        assertEquals("", option.getName());
        assertEquals("", option.getDescription());
        assertEquals(0.0, option.getAdditionalPrice());
    }

    @Test
    void fromOptionConvertsCorrectly() {
        Option option = new Option();
        option.setId(1L);
        option.setSupplierForeignId(10L);
        option.setCode("CODE01");
        option.setName("Option1");
        option.setDescription("Description1");
        option.setAdditionalPrice(100.0);

        OptionDTO dto = OptionDTO.fromOption(option);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("CODE01", dto.getCode());
        assertEquals("Option1", dto.getName());
        assertEquals("Description1", dto.getDescription());
        assertEquals(100.0, dto.getAdditionalPrice());
    }

    @Test
    void fromOptionHandlesNullValues() {
        Option option = new Option();
        option.setId(null);
        option.setSupplierForeignId(null);
        option.setCode(null);
        option.setName(null);
        option.setDescription(null);
        option.setAdditionalPrice(null);

        OptionDTO dto = OptionDTO.fromOption(option);

        assertNull(dto.getId());
        assertNull(dto.getSupplierForeignId());
        assertNull(dto.getCode());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getAdditionalPrice());
    }

    @Test
    void fromOptionHandlesEmptyStrings() {
        Option option = new Option();
        option.setId(1L);
        option.setSupplierForeignId(10L);
        option.setCode("");
        option.setName("");
        option.setDescription("");
        option.setAdditionalPrice(0.0);

        OptionDTO dto = OptionDTO.fromOption(option);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("", dto.getCode());
        assertEquals("", dto.getName());
        assertEquals("", dto.getDescription());
        assertEquals(0.0, dto.getAdditionalPrice());
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalObjects() {
        OptionDTO dto1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO dto2 = new OptionDTO(2L, null, "CODE01", "Option1", "Description1", 100.0);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentIds() {
        OptionDTO dto1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO dto2 = new OptionDTO(3L, null, "CODE01", "Option1", "Description1", 100.0);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentFields() {
        OptionDTO dto1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO dto2 = new OptionDTO(2L, null, "CODE02", "Option2", "Description2", 200.0);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForNullFields() {
        OptionDTO dto1 = new OptionDTO(1L, 2L, null, null, null, null);
        OptionDTO dto2 = new OptionDTO(2L, null, null, null, null, null);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForNullAndNonNullFields() {
        OptionDTO dto1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO dto2 = new OptionDTO(2L, null, null, null, null, null);
        assertFalse(dto1.equalsExternal(dto2));
    }
}
