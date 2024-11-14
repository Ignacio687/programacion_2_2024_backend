package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Customization;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CustomizationDTOTest {

    @Test
    void toCustomizationConvertsCorrectly() {
        OptionDTO option1 = new OptionDTO(1L, "Option1", "name1", "Description1", 100.0);
        OptionDTO option2 = new OptionDTO(2L, "Option2", "name2", "Description2", 200.0);
        CustomizationDTO dto = new CustomizationDTO(1L, "Customization", "Description", List.of(option1, option2));
        Customization customization = CustomizationDTO.toCustomization(dto);

        assertEquals(1L, customization.getId());
        assertEquals("Customization", customization.getName());
        assertEquals("Description", customization.getDescription());
        assertEquals(2, customization.getOptions().size());
    }

    @Test
    void toCustomizationHandlesNullValues() {
        CustomizationDTO dto = new CustomizationDTO(null, null, null, null);
        Customization customization = CustomizationDTO.toCustomization(dto);

        assertNull(customization.getId());
        assertNull(customization.getName());
        assertNull(customization.getDescription());
        assertTrue(customization.getOptions().isEmpty());
    }

    @Test
    void toCustomizationHandlesEmptyOptions() {
        CustomizationDTO dto = new CustomizationDTO(1L, "Customization", "Description", Collections.emptyList());
        Customization customization = CustomizationDTO.toCustomization(dto);

        assertEquals(1L, customization.getId());
        assertEquals("Customization", customization.getName());
        assertEquals("Description", customization.getDescription());
        assertTrue(customization.getOptions().isEmpty());
    }
}
