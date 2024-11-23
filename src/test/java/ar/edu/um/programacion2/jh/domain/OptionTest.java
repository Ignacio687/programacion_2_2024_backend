package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.CustomizationTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.OptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = getOptionSample1();
        Option option2 = new Option();
        assertThat(option1).isNotEqualTo(option2);

        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);

        option2 = getOptionSample2();
        assertThat(option1).isNotEqualTo(option2);
    }

    @Test
    void customizationTest() {
        Option option = getOptionRandomSampleGenerator();
        Customization customizationBack = getCustomizationRandomSampleGenerator();

        option.addCustomization(customizationBack);
        assertThat(option.getCustomizations()).containsOnly(customizationBack);
        assertThat(customizationBack.getOptions()).containsOnly(option);

        option.removeCustomization(customizationBack);
        assertThat(option.getCustomizations()).doesNotContain(customizationBack);
        assertThat(customizationBack.getOptions()).doesNotContain(option);

        option.customizations(new HashSet<>(Set.of(customizationBack)));
        assertThat(option.getCustomizations()).containsOnly(customizationBack);
        assertThat(customizationBack.getOptions()).containsOnly(option);

        option.setCustomizations(new HashSet<>());
        assertThat(option.getCustomizations()).doesNotContain(customizationBack);
        assertThat(customizationBack.getOptions()).doesNotContain(option);
    }
}
