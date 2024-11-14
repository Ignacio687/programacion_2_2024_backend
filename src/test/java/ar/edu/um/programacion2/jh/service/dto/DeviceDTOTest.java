package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Device;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DeviceDTOTest {

    @Test
    void toDeviceMapsAllFieldsCorrectly() {
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
            List.of(new CharacteristicDTO()),
            List.of(new CustomizationDTO()),
            List.of(new ExtraDTO())
        );

        Device device = DeviceDTO.toDevice(dto);

        assertEquals(dto.getId(), device.getId());
        assertEquals(dto.getSupplierForeignKey(), device.getSupplierForeignKey());
        assertEquals(dto.getSupplier(), device.getSupplier());
        assertEquals(dto.getCode(), device.getCode());
        assertEquals(dto.getName(), device.getName());
        assertEquals(dto.getDescription(), device.getDescription());
        assertEquals(dto.getBasePrice(), device.getBasePrice());
        assertEquals(dto.getCurrency(), device.getCurrency());
        assertEquals(dto.getActive(), device.getActive());
        assertEquals(dto.getCharacteristics().size(), device.getCharacteristics().size());
        assertEquals(dto.getExtras().size(), device.getExtras().size());
        assertEquals(dto.getCustomizations().get(0).getOptions().size(), device.getOptions().size());
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
        assertTrue(device.getOptions().isEmpty());
    }

    @Test
    void toDeviceHandlesNullLists() {
        DeviceDTO dto = new DeviceDTO(1L, 2L, "Supplier", "Code", "Name", "Description", 100.0, "USD", true, null, null, null);

        Device device = DeviceDTO.toDevice(dto);

        assertNotNull(device.getCharacteristics());
        assertTrue(device.getCharacteristics().isEmpty());
        assertNotNull(device.getExtras());
        assertTrue(device.getExtras().isEmpty());
        assertNotNull(device.getOptions());
        assertTrue(device.getOptions().isEmpty());
    }

    @Test
    void toDeviceHandlesMultipleCustomizations() {
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
            List.of(new CharacteristicDTO()),
            List.of(
                new CustomizationDTO(1L, "Name", "Description", List.of(new OptionDTO())),
                new CustomizationDTO(2L, "Name2", "Description2", List.of(new OptionDTO()))
            ),
            List.of(new ExtraDTO())
        );

        Device device = DeviceDTO.toDevice(dto);

        assertEquals(2, device.getOptions().size());
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

        assertEquals(4, device.getCharacteristics().size());
        assertEquals(4, device.getExtras().size());
        assertEquals(7, device.getOptions().size());
    }
}
