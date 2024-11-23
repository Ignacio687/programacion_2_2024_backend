package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Option;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class CustomizationDTOTest {

    @Test
    void verifyMapsCustomizationsJsonCorrectly() throws IOException {
        String jsonData =
            """
            [
              {
                "id": 3,
                "nombre": "CPU",
                "descripcion": "Procesadores Disponibles",
                "opciones": [
                  { "id": 3, "codigo": "PROC03", "nombre": "Core Y1", "descripcion": "Procesador 1.2 GHz- 10 Cores", "precioAdicional": 0.0 },
                  { "id": 4, "codigo": "PROC04", "nombre": "Core Y2", "descripcion": "Procesador 1.7 GHz- 24 Cores", "precioAdicional": 700.0 }
                ]
              },
              {
                "id": 4,
                "nombre": "Memoria",
                "descripcion": "Memorias Disponibles",
                "opciones": [
                  { "id": 7, "codigo": "MEM03", "nombre": "DDR4-8", "descripcion": "Memoria DDR4- 8GB", "precioAdicional": 0.0 },
                  { "id": 8, "codigo": "MEM04", "nombre": "DDR4-16", "descripcion": "Memoria DDR4- 16GB", "precioAdicional": 300.0 },
                  { "id": 9, "codigo": "MEM05", "nombre": "DDR4-32", "descripcion": "Memoria DDR4- 32GB", "precioAdicional": 900.0 }
                ]
              },
              {
                "id": 6,
                "nombre": "Video",
                "descripcion": "Video Disponible",
                "opciones": [
                  { "id": 12, "codigo": "VID01", "nombre": "Integrado", "descripcion": "Video Integrado Memoria Compartida", "precioAdicional": 0.0 },
                  { "id": 13, "codigo": "VID02", "nombre": "Aceleradora 3D", "descripcion": "Nvidia XYZ-200 12GB", "precioAdicional": 900.0 }
                ]
              }
            ]
            """;

        ObjectMapper objectMapper = new ObjectMapper();
        List<CustomizationDTO> customizations = objectMapper.readValue(jsonData, new TypeReference<List<CustomizationDTO>>() {});

        assertEquals(3, customizations.size());

        assertTrue(
            customizations
                .stream()
                .anyMatch(
                    c ->
                        c.getId().equals(3L) &&
                        c.getName().equals("CPU") &&
                        c.getDescription().equals("Procesadores Disponibles") &&
                        c.getOptions().size() == 2 &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(3L)) &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(4L))
                )
        );

        assertTrue(
            customizations
                .stream()
                .anyMatch(
                    c ->
                        c.getId().equals(4L) &&
                        c.getName().equals("Memoria") &&
                        c.getDescription().equals("Memorias Disponibles") &&
                        c.getOptions().size() == 3 &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(7L)) &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(8L)) &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(9L))
                )
        );

        assertTrue(
            customizations
                .stream()
                .anyMatch(
                    c ->
                        c.getId().equals(6L) &&
                        c.getName().equals("Video") &&
                        c.getDescription().equals("Video Disponible") &&
                        c.getOptions().size() == 2 &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(12L)) &&
                        c.getOptions().stream().anyMatch(o -> o.getId().equals(13L))
                )
        );
    }

    @Test
    void toCustomizationConvertsCorrectly() {
        OptionDTO option1 = new OptionDTO(1L, 10L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(2L, 10L, "CODE02", "Option2", "Description2", 200.0);
        CustomizationDTO dto = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1, option2));
        Customization customization = CustomizationDTO.toCustomization(dto);

        assertEquals(1L, customization.getId());
        assertEquals(10L, customization.getSupplierForeignId());
        assertEquals("Customization1", customization.getName());
        assertEquals("Description1", customization.getDescription());
        assertEquals(2, customization.getOptions().size());
    }

    @Test
    void toCustomizationHandlesNullValues() {
        CustomizationDTO dto = new CustomizationDTO(null, null, null, null, null);
        Customization customization = CustomizationDTO.toCustomization(dto);

        assertNull(customization.getId());
        assertNull(customization.getSupplierForeignId());
        assertNull(customization.getName());
        assertNull(customization.getDescription());
        assertTrue(customization.getOptions().isEmpty());
    }

    @Test
    void toCustomizationHandlesEmptyOptions() {
        CustomizationDTO dto = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of());
        Customization customization = CustomizationDTO.toCustomization(dto);

        assertEquals(1L, customization.getId());
        assertEquals(10L, customization.getSupplierForeignId());
        assertEquals("Customization1", customization.getName());
        assertEquals("Description1", customization.getDescription());
        assertTrue(customization.getOptions().isEmpty());
    }

    @Test
    void fromCustomizationConvertsCorrectly() {
        Customization customization = new Customization();
        customization.setId(1L);
        customization.setSupplierForeignId(10L);
        customization.setName("Customization1");
        customization.setDescription("Description1");
        customization.setOptions(Set.of(new Option()));

        CustomizationDTO dto = CustomizationDTO.fromCustomization(customization);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("Customization1", dto.getName());
        assertEquals("Description1", dto.getDescription());
        assertEquals(1, dto.getOptions().size());
    }

    @Test
    void fromCustomizationHandlesNullValues() {
        Customization customization = new Customization();
        customization.setId(null);
        customization.setSupplierForeignId(null);
        customization.setName(null);
        customization.setDescription(null);
        customization.setOptions(null);

        CustomizationDTO dto = CustomizationDTO.fromCustomization(customization);

        assertNull(dto.getId());
        assertNull(dto.getSupplierForeignId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertTrue(dto.getOptions().isEmpty());
    }

    @Test
    void fromCustomizationHandlesEmptyOptions() {
        Customization customization = new Customization();
        customization.setId(1L);
        customization.setSupplierForeignId(10L);
        customization.setName("Customization1");
        customization.setDescription("Description1");
        customization.setOptions(Collections.emptySet());

        CustomizationDTO dto = CustomizationDTO.fromCustomization(customization);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("Customization1", dto.getName());
        assertEquals("Description1", dto.getDescription());
        assertTrue(dto.getOptions().isEmpty());
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalObjects() {
        OptionDTO option1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(2L, null, "CODE01", "Option1", "Description1", 100.0);
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1));
        CustomizationDTO dto2 = new CustomizationDTO(10L, null, "Customization1", "Description1", List.of(option2));
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentIds() {
        OptionDTO option1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(2L, null, "CODE02", "Option2", "Description2", 200.0);
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1));
        CustomizationDTO dto2 = new CustomizationDTO(20L, null, "Customization1", "Description1", List.of(option2));
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentFields() {
        OptionDTO option1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(2L, null, "CODE02", "Option2", "Description2", 200.0);
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1));
        CustomizationDTO dto2 = new CustomizationDTO(10L, null, "Customization1", "Different Description", List.of(option2));
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForNullFields() {
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, null, null, null);
        CustomizationDTO dto2 = new CustomizationDTO(10L, null, null, null, null);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForNullAndNonNullFields() {
        OptionDTO option1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1));
        CustomizationDTO dto2 = new CustomizationDTO(10L, null, null, null, null);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalOptions() {
        OptionDTO option1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(2L, null, "CODE01", "Option1", "Description1", 100.0);
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1));
        CustomizationDTO dto2 = new CustomizationDTO(10L, null, "Customization1", "Description1", List.of(option2));
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentOptions() {
        OptionDTO option1 = new OptionDTO(1L, 2L, "CODE01", "Option1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(3L, 2L, "CODE03", "Option3", "Description3", 300.0);
        CustomizationDTO dto1 = new CustomizationDTO(1L, 10L, "Customization1", "Description1", List.of(option1));
        CustomizationDTO dto2 = new CustomizationDTO(10L, null, "Customization1", "Description1", List.of(option2));
        assertFalse(dto1.equalsExternal(dto2));
    }
}
