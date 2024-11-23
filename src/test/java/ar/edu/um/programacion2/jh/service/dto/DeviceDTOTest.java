package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DeviceDTOTest {

    @Test
    void toDeviceMapsAllFieldsCorrectly() {
        CharacteristicDTO characteristicDTO = new CharacteristicDTO();
        characteristicDTO.setId(1L);
        CustomizationDTO customizationDTO = new CustomizationDTO();
        customizationDTO.setId(1L);
        ExtraDTO extraDTO = new ExtraDTO();
        extraDTO.setId(1L);
        DeviceDTO dto = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            List.of(characteristicDTO),
            List.of(customizationDTO),
            List.of(extraDTO)
        );

        Device device = DeviceDTO.toDevice(dto);

        assertEquals(dto.getId(), device.getId());
        assertEquals(dto.getSupplierForeignId(), device.getSupplierForeignId());
        assertEquals(dto.getSupplier(), device.getSupplier());
        assertEquals(dto.getCode(), device.getCode());
        assertEquals(dto.getName(), device.getName());
        assertEquals(dto.getDescription(), device.getDescription());
        assertEquals(dto.getBasePrice(), device.getBasePrice());
        assertEquals(dto.getCurrency(), device.getCurrency());
        assertEquals(dto.getActive(), device.getActive());
        assertEquals(dto.getCharacteristics().size(), device.getCharacteristics().size());
        assertEquals(dto.getExtras().size(), device.getExtras().size());
        assertEquals(dto.getCustomizations().size(), device.getCustomizations().size());

        // Verify IDs of the objects in the lists
        assertEquals(dto.getCharacteristics().get(0).getId(), device.getCharacteristics().iterator().next().getId());
        assertEquals(dto.getExtras().get(0).getId(), device.getExtras().iterator().next().getId());
        assertEquals(dto.getCustomizations().get(0).getId(), device.getCustomizations().iterator().next().getId());
    }

    @Test
    void toDeviceHandlesEmptyLists() {
        DeviceDTO dto = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );
        Device device = DeviceDTO.toDevice(dto);
        assertTrue(device.getCharacteristics().isEmpty());
        assertTrue(device.getExtras().isEmpty());
        assertTrue(device.getCustomizations().isEmpty());
    }

    @Test
    void toDeviceHandlesNullLists() {
        DeviceDTO dto = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);
        Device device = DeviceDTO.toDevice(dto);
        assertNotNull(device.getCharacteristics());
        assertTrue(device.getCharacteristics().isEmpty());
        assertNotNull(device.getExtras());
        assertTrue(device.getExtras().isEmpty());
        assertNotNull(device.getCustomizations());
        assertTrue(device.getCustomizations().isEmpty());
    }

    @Test
    void toDeviceHandlesMultipleEntries() {
        CharacteristicDTO characteristicDTO1 = new CharacteristicDTO();
        characteristicDTO1.setId(1L);
        CharacteristicDTO characteristicDTO2 = new CharacteristicDTO();
        characteristicDTO2.setId(2L);
        CustomizationDTO customizationDTO1 = new CustomizationDTO();
        customizationDTO1.setId(1L);
        CustomizationDTO customizationDTO2 = new CustomizationDTO();
        customizationDTO2.setId(2L);
        ExtraDTO extraDTO1 = new ExtraDTO();
        extraDTO1.setId(1L);
        ExtraDTO extraDTO2 = new ExtraDTO();
        extraDTO2.setId(2L);

        DeviceDTO dto = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            List.of(characteristicDTO1, characteristicDTO2),
            List.of(customizationDTO1, customizationDTO2),
            List.of(extraDTO1, extraDTO2)
        );

        Device device = DeviceDTO.toDevice(dto);

        assertEquals(2, device.getCharacteristics().size());
        assertEquals(2, device.getExtras().size());
        assertEquals(2, device.getCustomizations().size());

        assertTrue(device.getCharacteristics().stream().anyMatch(c -> c.getId().equals(1L)));
        assertTrue(device.getCharacteristics().stream().anyMatch(c -> c.getId().equals(2L)));
        assertTrue(device.getExtras().stream().anyMatch(e -> e.getId().equals(1L)));
        assertTrue(device.getExtras().stream().anyMatch(e -> e.getId().equals(2L)));
        assertTrue(device.getCustomizations().stream().anyMatch(c -> c.getId().equals(1L)));
        assertTrue(device.getCustomizations().stream().anyMatch(c -> c.getId().equals(2L)));
    }

    @Test
    void toDeviceHandlesCompleteJson() throws JsonProcessingException {
        String json =
            """
            {
              "id": 2,
              "codigo": "NTB02",
              "nombre": "Notebook 2",
              "descripcion": "Descripción detallada de la Notebook",
              "precioBase": 2250.0,
              "moneda": "USD",
              "caracteristicas": [
                { "id": 4, "nombre": "Pantalla", "descripcion": "Pantalla OLED 16\\"" },
                { "id": 5, "nombre": "Camara", "descripcion": "Camara Web 1080p" },
                { "id": 6, "nombre": "Batería", "descripcion": "Batería 80Wh" },
                { "id": 7, "nombre": "Adicional", "descripcion": "Lector de memoria múltiple" }
              ],
              "personalizaciones": [
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
              ],
              "adicionales": [
                { "id": 1, "nombre": "Mouse", "descripcion": "Mouse Bluetooth 3 teclas", "precio": 40.5, "precioGratis": 2000.0 },
                { "id": 2, "nombre": "Teclado", "descripcion": "Teclado bluetooth", "precio": 78.0, "precioGratis": -1.0 },
                { "id": 3, "nombre": "Funda", "descripcion": "Funda de silicona", "precio": 30.0, "precioGratis": 2650.0 },
                { "id": 4, "nombre": "Cargador", "descripcion": "Cargador rápido", "precio": 189.0, "precioGratis": 2400.0 }
              ]
            }
            """;

        ObjectMapper objectMapper = new ObjectMapper();
        DeviceDTO dto = objectMapper.readValue(json, DeviceDTO.class);

        Device device = DeviceDTO.toDevice(dto);

        assertEquals(dto.getId(), device.getId());
        assertEquals(dto.getSupplierForeignId(), device.getSupplierForeignId());
        assertEquals(dto.getSupplier(), device.getSupplier());
        assertEquals(dto.getCode(), device.getCode());
        assertEquals(dto.getName(), device.getName());
        assertEquals(dto.getDescription(), device.getDescription());
        assertEquals(dto.getBasePrice(), device.getBasePrice());
        assertEquals(dto.getCurrency(), device.getCurrency());
        assertEquals(dto.getActive(), device.getActive());
        assertEquals(dto.getCharacteristics().size(), device.getCharacteristics().size());
        assertEquals(dto.getExtras().size(), device.getExtras().size());
        assertEquals(dto.getCustomizations().size(), device.getCustomizations().size());

        // Verify IDs of the objects in the lists
        assertTrue(device.getCharacteristics().stream().anyMatch(c -> c.getId().equals(4L)));
        assertTrue(device.getCharacteristics().stream().anyMatch(c -> c.getId().equals(5L)));
        assertTrue(device.getCharacteristics().stream().anyMatch(c -> c.getId().equals(6L)));
        assertTrue(device.getCharacteristics().stream().anyMatch(c -> c.getId().equals(7L)));
        assertTrue(device.getExtras().stream().anyMatch(e -> e.getId().equals(1L)));
        assertTrue(device.getExtras().stream().anyMatch(e -> e.getId().equals(2L)));
        assertTrue(device.getExtras().stream().anyMatch(e -> e.getId().equals(3L)));
        assertTrue(device.getExtras().stream().anyMatch(e -> e.getId().equals(4L)));
        assertTrue(device.getCustomizations().stream().anyMatch(c -> c.getId().equals(3L)));
        assertTrue(device.getCustomizations().stream().anyMatch(c -> c.getId().equals(4L)));
        assertTrue(device.getCustomizations().stream().anyMatch(c -> c.getId().equals(6L)));
    }

    @Test
    void fromDeviceMapsAllFieldsCorrectly() {
        Characteristic characteristic1 = new Characteristic();
        characteristic1.setId(1L);
        Characteristic characteristic2 = new Characteristic();
        characteristic2.setId(2L);

        Extra extra1 = new Extra();
        extra1.setId(1L);
        Extra extra2 = new Extra();
        extra2.setId(2L);

        Customization customization1 = new Customization();
        customization1.setId(1L);
        Customization customization2 = new Customization();
        customization2.setId(2L);

        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(2L);
        device.setSupplier("Supplier");
        device.setCode("Code");
        device.setName("Name");
        device.setDescription("Description");
        device.setBasePrice(100.0);
        device.setCurrency("USD");
        device.setActive(true);
        device.setCharacteristics(Set.of(characteristic1, characteristic2));
        device.setExtras(Set.of(extra1, extra2));
        device.setCustomizations(Set.of(customization1, customization2));

        DeviceDTO dto = DeviceDTO.fromDevice(device);

        assertEquals(device.getId(), dto.getId());
        assertEquals(device.getSupplierForeignId(), dto.getSupplierForeignId());
        assertEquals(device.getSupplier(), dto.getSupplier());
        assertEquals(device.getCode(), dto.getCode());
        assertEquals(device.getName(), dto.getName());
        assertEquals(device.getDescription(), dto.getDescription());
        assertEquals(device.getBasePrice(), dto.getBasePrice());
        assertEquals(device.getCurrency(), dto.getCurrency());
        assertEquals(device.getActive(), dto.getActive());
        assertEquals(device.getCharacteristics().size(), dto.getCharacteristics().size());
        assertEquals(device.getExtras().size(), dto.getExtras().size());
        assertEquals(device.getCustomizations().size(), dto.getCustomizations().size());

        // Verify IDs of the objects in the lists
        assertTrue(dto.getCharacteristics().stream().anyMatch(c -> c.getId().equals(1L)));
        assertTrue(dto.getCharacteristics().stream().anyMatch(c -> c.getId().equals(2L)));
        assertTrue(dto.getExtras().stream().anyMatch(e -> e.getId().equals(1L)));
        assertTrue(dto.getExtras().stream().anyMatch(e -> e.getId().equals(2L)));
        assertTrue(dto.getCustomizations().stream().anyMatch(c -> c.getId().equals(1L)));
        assertTrue(dto.getCustomizations().stream().anyMatch(c -> c.getId().equals(2L)));
    }

    @Test
    void fromDeviceHandlesEmptyLists() {
        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(2L);
        device.setSupplier("Supplier");
        device.setCode("Code");
        device.setName("Name");
        device.setDescription("Description");
        device.setBasePrice(100.0);
        device.setCurrency("USD");
        device.setActive(true);
        device.setCharacteristics(Collections.emptySet());
        device.setExtras(Collections.emptySet());
        device.setCustomizations(Collections.emptySet());

        DeviceDTO dto = DeviceDTO.fromDevice(device);

        assertEquals(device.getId(), dto.getId());
        assertEquals(device.getSupplierForeignId(), dto.getSupplierForeignId());
        assertEquals(device.getSupplier(), dto.getSupplier());
        assertEquals(device.getCode(), dto.getCode());
        assertEquals(device.getName(), dto.getName());
        assertEquals(device.getDescription(), dto.getDescription());
        assertEquals(device.getBasePrice(), dto.getBasePrice());
        assertEquals(device.getCurrency(), dto.getCurrency());
        assertEquals(device.getActive(), dto.getActive());
        assertTrue(dto.getCharacteristics().isEmpty());
        assertTrue(dto.getExtras().isEmpty());
        assertTrue(dto.getCustomizations().isEmpty());
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalObjects() {
        DeviceDTO dto1 = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);
        DeviceDTO dto2 = new DeviceDTO(2L, null, null, "Code", "Name", "Description", 100.0, "USD", null, null, null, null);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentIds() {
        DeviceDTO dto1 = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);
        DeviceDTO dto2 = new DeviceDTO(1L, null, null, "Code", "Name", "Description", 100.0, "USD", null, null, null, null);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentFields() {
        DeviceDTO dto1 = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);
        DeviceDTO dto2 = new DeviceDTO(2L, null, null, "Code", "Name", "Different Description", 100.0, "USD", null, null, null, null);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForNullLists() {
        DeviceDTO dto1 = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);
        DeviceDTO dto2 = new DeviceDTO(2L, null, null, "Code", "Name", "Description", 100.0, "USD", null, null, null, null);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForEmptyLists() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentCharacteristics() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            List.of(new CharacteristicDTO(1L, 2L, "Name", "Description")),
            null,
            null
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            List.of(new CharacteristicDTO(1L, null, "Name", "Description")),
            null,
            null
        );
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentCustomizations() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            null,
            List.of(
                new CustomizationDTO(1L, 10L, "Name", "Description", List.of(new OptionDTO(1L, 10L, "Code", "Name", "Description", 0.0)))
            ),
            null
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            null,
            List.of(
                new CustomizationDTO(1L, null, "Name", "Description", List.of(new OptionDTO(1L, null, "Code", "Name", "Description", 0.0)))
            ),
            null
        );
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentExtras() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            null,
            null,
            List.of(new ExtraDTO(1L, 2L, "Name", "Description", 0.0, 0.0))
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            null,
            null,
            List.of(new ExtraDTO(1L, null, "Name", "Description", 0.0, 0.0))
        );
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalCharacteristics() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            List.of(new CharacteristicDTO(1L, 2L, "Name", "Description")),
            null,
            null
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            List.of(new CharacteristicDTO(2L, null, "Name", "Description")),
            null,
            null
        );
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalCustomizations() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            null,
            List.of(
                new CustomizationDTO(1L, 2L, "Name", "Description", List.of(new OptionDTO(1L, 2L, "Code", "Name", "Description", 0.0)))
            ),
            null
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            null,
            List.of(
                new CustomizationDTO(2L, null, "Name", "Description", List.of(new OptionDTO(2L, null, "Code", "Name", "Description", 0.0)))
            ),
            null
        );
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalExtras() {
        DeviceDTO dto1 = new DeviceDTO(
            1L,
            2L,
            "Supplier",
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            true,
            null,
            null,
            List.of(new ExtraDTO(1L, 2L, "Name", "Description", 0.0, 0.0))
        );
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            null,
            null,
            List.of(new ExtraDTO(2L, null, "Name", "Description", 0.0, 0.0))
        );
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForNullAndNonNullFields() {
        DeviceDTO dto1 = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);
        DeviceDTO dto2 = new DeviceDTO(
            2L,
            null,
            null,
            "Code",
            "Name",
            "Description",
            100.0,
            "USD",
            null,
            List.of(new CharacteristicDTO()),
            null,
            null
        );
        assertFalse(dto1.equalsExternal(dto2));
    }
}
