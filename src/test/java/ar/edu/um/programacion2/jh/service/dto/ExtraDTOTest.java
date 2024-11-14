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
}
