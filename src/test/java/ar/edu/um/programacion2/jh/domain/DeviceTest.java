package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.CharacteristicTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.DeviceTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.ExtraTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.OptionTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.SaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Device.class);
        Device device1 = getDeviceSample1();
        Device device2 = new Device();
        assertThat(device1).isNotEqualTo(device2);

        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);

        device2 = getDeviceSample2();
        assertThat(device1).isNotEqualTo(device2);
    }

    @Test
    void salesTest() {
        Device device = getDeviceRandomSampleGenerator();
        Sale saleBack = getSaleRandomSampleGenerator();

        device.addSales(saleBack);
        assertThat(device.getSales()).containsOnly(saleBack);
        assertThat(saleBack.getDevice()).isEqualTo(device);

        device.removeSales(saleBack);
        assertThat(device.getSales()).doesNotContain(saleBack);
        assertThat(saleBack.getDevice()).isNull();

        device.sales(new HashSet<>(Set.of(saleBack)));
        assertThat(device.getSales()).containsOnly(saleBack);
        assertThat(saleBack.getDevice()).isEqualTo(device);

        device.setSales(new HashSet<>());
        assertThat(device.getSales()).doesNotContain(saleBack);
        assertThat(saleBack.getDevice()).isNull();
    }

    @Test
    void characteristicsTest() {
        Device device = getDeviceRandomSampleGenerator();
        Characteristic characteristicBack = getCharacteristicRandomSampleGenerator();

        device.addCharacteristics(characteristicBack);
        assertThat(device.getCharacteristics()).containsOnly(characteristicBack);

        device.removeCharacteristics(characteristicBack);
        assertThat(device.getCharacteristics()).doesNotContain(characteristicBack);

        device.characteristics(new HashSet<>(Set.of(characteristicBack)));
        assertThat(device.getCharacteristics()).containsOnly(characteristicBack);

        device.setCharacteristics(new HashSet<>());
        assertThat(device.getCharacteristics()).doesNotContain(characteristicBack);
    }

    @Test
    void optionsTest() {
        Device device = getDeviceRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        device.addOptions(optionBack);
        assertThat(device.getOptions()).containsOnly(optionBack);

        device.removeOptions(optionBack);
        assertThat(device.getOptions()).doesNotContain(optionBack);

        device.options(new HashSet<>(Set.of(optionBack)));
        assertThat(device.getOptions()).containsOnly(optionBack);

        device.setOptions(new HashSet<>());
        assertThat(device.getOptions()).doesNotContain(optionBack);
    }

    @Test
    void extrasTest() {
        Device device = getDeviceRandomSampleGenerator();
        Extra extraBack = getExtraRandomSampleGenerator();

        device.addExtras(extraBack);
        assertThat(device.getExtras()).containsOnly(extraBack);

        device.removeExtras(extraBack);
        assertThat(device.getExtras()).doesNotContain(extraBack);

        device.extras(new HashSet<>(Set.of(extraBack)));
        assertThat(device.getExtras()).containsOnly(extraBack);

        device.setExtras(new HashSet<>());
        assertThat(device.getExtras()).doesNotContain(extraBack);
    }
}
