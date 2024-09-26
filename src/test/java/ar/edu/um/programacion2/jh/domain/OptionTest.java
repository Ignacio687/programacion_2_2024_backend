package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.CustomizationTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.DeviceTestSamples.*;
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

        option.setCustomization(customizationBack);
        assertThat(option.getCustomization()).isEqualTo(customizationBack);

        option.customization(null);
        assertThat(option.getCustomization()).isNull();
    }

    @Test
    void devicesTest() {
        Option option = getOptionRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        option.addDevices(deviceBack);
        assertThat(option.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getOptions()).containsOnly(option);

        option.removeDevices(deviceBack);
        assertThat(option.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getOptions()).doesNotContain(option);

        option.devices(new HashSet<>(Set.of(deviceBack)));
        assertThat(option.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getOptions()).containsOnly(option);

        option.setDevices(new HashSet<>());
        assertThat(option.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getOptions()).doesNotContain(option);
    }
}
