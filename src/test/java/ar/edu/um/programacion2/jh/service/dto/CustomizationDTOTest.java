package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Customization;
import java.util.List;
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
}
