package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import java.time.Instant;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompleteSaleDTOTest {

    @Test
    void testFromSaleItemShouldMapSaleToCompleteSaleDTO() {
        Sale sale = new Sale();
        sale.setId(1L);
        Device device = new Device();
        device.setId(2L);
        device.setCode("CODE123");
        device.setName("DeviceName");
        device.setDescription("DeviceDescription");

        Characteristic characteristic = new Characteristic();
        characteristic.setId(10L);
        Customization customization = new Customization();
        customization.setId(20L);
        Option option = new Option();
        option.setId(100L);
        customization.setOptions(Set.of(option));
        Extra extra = new Extra();
        extra.setId(30L);

        device.setCharacteristics(Set.of(characteristic));
        device.setCustomizations(Set.of(customization));
        device.setExtras(Set.of(extra));
        sale.setDevice(device);
        sale.setSupplierForeignId(3L);
        sale.setDevicePrice(100.0);
        sale.setCurrency("USD");
        sale.setFinalPrice(150.0);
        sale.setSaleDate(Instant.now());

        SaleItem saleItemOption = new SaleItem();
        saleItemOption.setOption(option);
        saleItemOption.setPrice(20.0);
        SaleItem saleItemExtra = new SaleItem();
        saleItemExtra.setExtra(extra);
        saleItemExtra.setPrice(30.0);
        sale.setSaleItems(Set.of(saleItemOption, saleItemExtra));

        CompleteSaleDTO dto = CompleteSaleDTO.fromSaleItem(sale);

        assertEquals(sale.getId(), dto.getSaleId());
        assertEquals(device.getId(), dto.getDeviceId());
        assertEquals(sale.getSupplierForeignId(), dto.getSupplierForeignId());
        assertEquals(device.getCode(), dto.getCode());
        assertEquals(device.getName(), dto.getName());
        assertEquals(device.getDescription(), dto.getDescription());
        assertEquals(sale.getDevicePrice(), dto.getBasePrice());
        assertEquals(sale.getCurrency(), dto.getCurrency());
        assertEquals(sale.getFinalPrice(), dto.getFinalPrice());
        assertEquals(sale.getSaleDate(), dto.getSaleDate());
        assertEquals(1, dto.getCharacteristics().size());
        assertEquals(characteristic.getId(), dto.getCharacteristics().get(0).getId());
        assertEquals(1, dto.getCustomizations().size());
        assertEquals(customization.getId(), dto.getCustomizations().get(0).getId());
        assertEquals(1, dto.getCustomizations().get(0).getOptions().size());
        assertEquals(option.getId(), dto.getCustomizations().get(0).getOptions().get(0).getId());
        assertEquals(20.0, dto.getCustomizations().get(0).getOptions().get(0).getAdditionalPrice());
        assertEquals(1, dto.getExtras().size());
        assertEquals(extra.getId(), dto.getExtras().get(0).getId());
        assertEquals(30.0, dto.getExtras().get(0).getPrice());
    }

    @Test
    void testFromSaleItemShouldOnlyIncludeRelevantExtrasAndOptions() {
        Sale sale = new Sale();
        sale.setId(1L);
        Device device = new Device();
        device.setId(2L);
        device.setCode("CODE123");
        device.setName("DeviceName");
        device.setDescription("DeviceDescription");

        Characteristic characteristic = new Characteristic();
        characteristic.setId(10L);
        Customization customization = new Customization();
        customization.setId(20L);
        Option option1 = new Option();
        option1.setId(100L);
        Option option2 = new Option();
        option2.setId(101L);
        customization.setOptions(Set.of(option1, option2));
        Extra extra1 = new Extra();
        extra1.setId(30L);
        Extra extra2 = new Extra();
        extra2.setId(31L);

        device.setCharacteristics(Set.of(characteristic));
        device.setCustomizations(Set.of(customization));
        device.setExtras(Set.of(extra1, extra2));
        sale.setDevice(device);
        sale.setSupplierForeignId(3L);
        sale.setDevicePrice(100.0);
        sale.setCurrency("USD");
        sale.setFinalPrice(150.0);
        sale.setSaleDate(Instant.now());

        SaleItem saleItemOption = new SaleItem();
        saleItemOption.setOption(option1);
        saleItemOption.setPrice(20.0);
        SaleItem saleItemExtra = new SaleItem();
        saleItemExtra.setExtra(extra1);
        saleItemExtra.setPrice(30.0);
        sale.setSaleItems(Set.of(saleItemOption, saleItemExtra));

        CompleteSaleDTO dto = CompleteSaleDTO.fromSaleItem(sale);

        assertEquals(1, dto.getCustomizations().get(0).getOptions().size());
        assertEquals(option1.getId(), dto.getCustomizations().get(0).getOptions().get(0).getId());
        assertEquals(20.0, dto.getCustomizations().get(0).getOptions().get(0).getAdditionalPrice());
        assertEquals(1, dto.getExtras().size());
        assertEquals(extra1.getId(), dto.getExtras().get(0).getId());
        assertEquals(30.0, dto.getExtras().get(0).getPrice());
    }
}
