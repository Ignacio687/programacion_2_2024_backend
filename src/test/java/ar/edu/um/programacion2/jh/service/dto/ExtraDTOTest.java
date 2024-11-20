package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Extra;
import org.junit.jupiter.api.Test;

public class ExtraDTOTest {

    @Test
    void toExtraConvertsCorrectly() {
        ExtraDTO dto = new ExtraDTO(1L, 10L, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        Extra extra = ExtraDTO.toExtra(dto);

        assertEquals(1L, extra.getId());
        assertEquals(10L, extra.getSupplierForeignId());
        assertEquals("Mouse", extra.getName());
        assertEquals("Mouse Bluetooth 3 teclas", extra.getDescription());
        assertEquals(40.5, extra.getPrice());
        assertEquals(2000.0, extra.getFreePrice());
    }

    @Test
    void toExtraHandlesNullValues() {
        ExtraDTO dto = new ExtraDTO(null, null, null, null, null, null);
        Extra extra = ExtraDTO.toExtra(dto);

        assertNull(extra.getId());
        assertNull(extra.getSupplierForeignId());
        assertNull(extra.getName());
        assertNull(extra.getDescription());
        assertNull(extra.getPrice());
        assertNull(extra.getFreePrice());
    }

    @Test
    void toExtraHandlesEmptyStrings() {
        ExtraDTO dto = new ExtraDTO(1L, 10L, "", "", 0.0, 0.0);
        Extra extra = ExtraDTO.toExtra(dto);

        assertEquals(1L, extra.getId());
        assertEquals(10L, extra.getSupplierForeignId());
        assertEquals("", extra.getName());
        assertEquals("", extra.getDescription());
        assertEquals(0.0, extra.getPrice());
        assertEquals(0.0, extra.getFreePrice());
    }

    @Test
    void fromExtraConvertsCorrectly() {
        Extra extra = new Extra();
        extra.setId(1L);
        extra.setSupplierForeignId(10L);
        extra.setName("Mouse");
        extra.setDescription("Mouse Bluetooth 3 teclas");
        extra.setPrice(40.5);
        extra.setFreePrice(2000.0);

        ExtraDTO dto = ExtraDTO.fromExtra(extra);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("Mouse", dto.getName());
        assertEquals("Mouse Bluetooth 3 teclas", dto.getDescription());
        assertEquals(40.5, dto.getPrice());
        assertEquals(2000.0, dto.getFreePrice());
    }

    @Test
    void fromExtraHandlesNullValues() {
        Extra extra = new Extra();
        extra.setId(null);
        extra.setSupplierForeignId(null);
        extra.setName(null);
        extra.setDescription(null);
        extra.setPrice(null);
        extra.setFreePrice(null);

        ExtraDTO dto = ExtraDTO.fromExtra(extra);

        assertNull(dto.getId());
        assertNull(dto.getSupplierForeignId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertNull(dto.getPrice());
        assertNull(dto.getFreePrice());
    }

    @Test
    void fromExtraHandlesEmptyStrings() {
        Extra extra = new Extra();
        extra.setId(1L);
        extra.setSupplierForeignId(10L);
        extra.setName("");
        extra.setDescription("");
        extra.setPrice(0.0);
        extra.setFreePrice(0.0);

        ExtraDTO dto = ExtraDTO.fromExtra(extra);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getSupplierForeignId());
        assertEquals("", dto.getName());
        assertEquals("", dto.getDescription());
        assertEquals(0.0, dto.getPrice());
        assertEquals(0.0, dto.getFreePrice());
    }

    @Test
    void equalsExternalReturnsTrueForIdenticalObjects() {
        ExtraDTO dto1 = new ExtraDTO(1L, 10L, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        ExtraDTO dto2 = new ExtraDTO(10L, null, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentIds() {
        ExtraDTO dto1 = new ExtraDTO(1L, 10L, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        ExtraDTO dto2 = new ExtraDTO(20L, null, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForDifferentFields() {
        ExtraDTO dto1 = new ExtraDTO(1L, 10L, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        ExtraDTO dto2 = new ExtraDTO(10L, null, "Mouse", "Mouse Bluetooth 2 teclas", 40.5, 2000.0);
        assertFalse(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsTrueForNullFields() {
        ExtraDTO dto1 = new ExtraDTO(1L, 10L, null, null, null, null);
        ExtraDTO dto2 = new ExtraDTO(10L, null, null, null, null, null);
        assertTrue(dto1.equalsExternal(dto2));
    }

    @Test
    void equalsExternalReturnsFalseForNullAndNonNullFields() {
        ExtraDTO dto1 = new ExtraDTO(1L, 10L, "Mouse", "Mouse Bluetooth 3 teclas", 40.5, 2000.0);
        ExtraDTO dto2 = new ExtraDTO(10L, null, null, null, null, null);
        assertFalse(dto1.equalsExternal(dto2));
    }
}
