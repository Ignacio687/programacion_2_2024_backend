package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Option;
import org.junit.jupiter.api.Test;

public class OptionDTOTest {

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
}
