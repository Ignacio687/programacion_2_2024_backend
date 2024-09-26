package ar.edu.um.programacion2.jh.domain;

import static ar.edu.um.programacion2.jh.domain.ExtraTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.OptionTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.SaleItemTestSamples.*;
import static ar.edu.um.programacion2.jh.domain.SaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaleItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleItem.class);
        SaleItem saleItem1 = getSaleItemSample1();
        SaleItem saleItem2 = new SaleItem();
        assertThat(saleItem1).isNotEqualTo(saleItem2);

        saleItem2.setId(saleItem1.getId());
        assertThat(saleItem1).isEqualTo(saleItem2);

        saleItem2 = getSaleItemSample2();
        assertThat(saleItem1).isNotEqualTo(saleItem2);
    }

    @Test
    void optionTest() {
        SaleItem saleItem = getSaleItemRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        saleItem.setOption(optionBack);
        assertThat(saleItem.getOption()).isEqualTo(optionBack);

        saleItem.option(null);
        assertThat(saleItem.getOption()).isNull();
    }

    @Test
    void extraTest() {
        SaleItem saleItem = getSaleItemRandomSampleGenerator();
        Extra extraBack = getExtraRandomSampleGenerator();

        saleItem.setExtra(extraBack);
        assertThat(saleItem.getExtra()).isEqualTo(extraBack);

        saleItem.extra(null);
        assertThat(saleItem.getExtra()).isNull();
    }

    @Test
    void saleTest() {
        SaleItem saleItem = getSaleItemRandomSampleGenerator();
        Sale saleBack = getSaleRandomSampleGenerator();

        saleItem.setSale(saleBack);
        assertThat(saleItem.getSale()).isEqualTo(saleBack);

        saleItem.sale(null);
        assertThat(saleItem.getSale()).isNull();
    }
}
