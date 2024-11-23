package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.CustomizationTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.DeviceTestSamples.*;
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

        customization.removeOptions(optionBack);
        assertThat(customization.getOptions()).doesNotContain(optionBack);

        customization.options(new HashSet<>(Set.of(optionBack)));
        assertThat(customization.getOptions()).containsOnly(optionBack);

        customization.setOptions(new HashSet<>());
        assertThat(customization.getOptions()).doesNotContain(optionBack);
    }

    @Test
    void devicesTest() {
        Customization customization = getCustomizationRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        customization.addDevices(deviceBack);
        assertThat(customization.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getCustomizations()).containsOnly(customization);

        customization.removeDevices(deviceBack);
        assertThat(customization.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getCustomizations()).doesNotContain(customization);

        customization.devices(new HashSet<>(Set.of(deviceBack)));
        assertThat(customization.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getCustomizations()).containsOnly(customization);

        customization.setDevices(new HashSet<>());
        assertThat(customization.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getCustomizations()).doesNotContain(customization);
    }
}
