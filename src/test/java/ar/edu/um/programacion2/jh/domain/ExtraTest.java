package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.DeviceTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.ExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExtraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extra.class);
        Extra extra1 = getExtraSample1();
        Extra extra2 = new Extra();
        assertThat(extra1).isNotEqualTo(extra2);

        extra2.setId(extra1.getId());
        assertThat(extra1).isEqualTo(extra2);

        extra2 = getExtraSample2();
        assertThat(extra1).isNotEqualTo(extra2);
    }

    @Test
    void devicesTest() {
        Extra extra = getExtraRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        extra.addDevices(deviceBack);
        assertThat(extra.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getExtras()).containsOnly(extra);

        extra.removeDevices(deviceBack);
        assertThat(extra.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getExtras()).doesNotContain(extra);

        extra.devices(new HashSet<>(Set.of(deviceBack)));
        assertThat(extra.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getExtras()).containsOnly(extra);

        extra.setDevices(new HashSet<>());
        assertThat(extra.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getExtras()).doesNotContain(extra);
    }
}
