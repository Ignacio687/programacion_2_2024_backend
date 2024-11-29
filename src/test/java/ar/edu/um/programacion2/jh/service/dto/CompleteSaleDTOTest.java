package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.domain.Sale;
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
        assertEquals(characteristic.getId(), dto.getCharacteristics().get(0).getId());
        assertEquals(customization.getId(), dto.getCustomizations().get(0).getId());
        assertEquals(extra.getId(), dto.getExtras().get(0).getId());
    }
}
