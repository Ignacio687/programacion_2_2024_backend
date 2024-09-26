package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.CustomizationTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.OptionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CustomizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customization.class);
        Customization customization1 = getCustomizationSample1();
        Customization customization2 = new Customization();
        assertThat(customization1).isNotEqualTo(customization2);

        customization2.setId(customization1.getId());
        assertThat(customization1).isEqualTo(customization2);

        customization2 = getCustomizationSample2();
        assertThat(customization1).isNotEqualTo(customization2);
    }

    @Test
    void optionsTest() {
        Customization customization = getCustomizationRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        customization.addOptions(optionBack);
        assertThat(customization.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getCustomization()).isEqualTo(customization);

        customization.removeOptions(optionBack);
        assertThat(customization.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getCustomization()).isNull();

        customization.options(new HashSet<>(Set.of(optionBack)));
        assertThat(customization.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getCustomization()).isEqualTo(customization);

        customization.setOptions(new HashSet<>());
        assertThat(customization.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getCustomization()).isNull();
    }
}
