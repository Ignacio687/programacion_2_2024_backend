package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.DeviceTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.SaleItemTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.SaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sale.class);
        Sale sale1 = getSaleSample1();
        Sale sale2 = new Sale();
        assertThat(sale1).isNotEqualTo(sale2);

        sale2.setId(sale1.getId());
        assertThat(sale1).isEqualTo(sale2);

        sale2 = getSaleSample2();
        assertThat(sale1).isNotEqualTo(sale2);
    }

    @Test
    void saleItemsTest() {
        Sale sale = getSaleRandomSampleGenerator();
        SaleItem saleItemBack = getSaleItemRandomSampleGenerator();

        sale.addSaleItems(saleItemBack);
        assertThat(sale.getSaleItems()).containsOnly(saleItemBack);
        assertThat(saleItemBack.getSale()).isEqualTo(sale);

        sale.removeSaleItems(saleItemBack);
        assertThat(sale.getSaleItems()).doesNotContain(saleItemBack);
        assertThat(saleItemBack.getSale()).isNull();

        sale.saleItems(new HashSet<>(Set.of(saleItemBack)));
        assertThat(sale.getSaleItems()).containsOnly(saleItemBack);
        assertThat(saleItemBack.getSale()).isEqualTo(sale);

        sale.setSaleItems(new HashSet<>());
        assertThat(sale.getSaleItems()).doesNotContain(saleItemBack);
        assertThat(saleItemBack.getSale()).isNull();
    }

    @Test
    void deviceTest() {
        Sale sale = getSaleRandomSampleGenerator();
        Device deviceBack = getDeviceRandomSampleGenerator();

        sale.setDevice(deviceBack);
        assertThat(sale.getDevice()).isEqualTo(deviceBack);

        sale.device(null);
        assertThat(sale.getDevice()).isNull();
    }
}
