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
