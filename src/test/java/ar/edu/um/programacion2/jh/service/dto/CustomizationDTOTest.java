package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Option;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class CustomizationDTOTest {

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
