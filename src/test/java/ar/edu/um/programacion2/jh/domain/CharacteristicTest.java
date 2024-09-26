package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.CharacteristicTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.DeviceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CharacteristicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Characteristic.class);
        Characteristic characteristic1 = getCharacteristicSample1();
        Characteristic characteristic2 = new Characteristic();
        assertThat(characteristic1).isNotEqualTo(characteristic2);

        characteristic2.setId(characteristic1.getId());
        assertThat(characteristic1).isEqualTo(characteristic2);

        characteristic2 = getCharacteristicSample2();
        assertThat(characteristic1).isNotEqualTo(characteristic2);
    }

    @Test
    void devicesTest() {
        Characteristic characteristic = getCharacteristicRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        characteristic.addDevices(deviceBack);
        assertThat(characteristic.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getCharacteristics()).containsOnly(characteristic);

        characteristic.removeDevices(deviceBack);
        assertThat(characteristic.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getCharacteristics()).doesNotContain(characteristic);

        characteristic.devices(new HashSet<>(Set.of(deviceBack)));
        assertThat(characteristic.getDevices()).containsOnly(deviceBack);
        assertThat(deviceBack.getCharacteristics()).containsOnly(characteristic);

        characteristic.setDevices(new HashSet<>());
        assertThat(characteristic.getDevices()).doesNotContain(deviceBack);
        assertThat(deviceBack.getCharacteristics()).doesNotContain(characteristic);
    }
}
