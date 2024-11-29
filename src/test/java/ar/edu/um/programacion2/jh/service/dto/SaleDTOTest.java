package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class SaleDTOTest {

    @Test
    void testToSaleDTOShouldMapSaleToSaleDTO() {
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setSupplierForeignId(2L);
        sale.setDevicePrice(100.0);
        sale.setFinalPrice(150.0);
        sale.setSaleDate(Instant.now());
        sale.setCurrency("USD");
        sale.setFinalized(true);

        SaleItem saleItem = new SaleItem();
        saleItem.setId(4L);
        sale.setSaleItems(new HashSet<>(List.of(saleItem)));

        Device device = new Device();
        device.setId(3L);
        sale.setDevice(device);

        SaleDTO dto = SaleDTO.toSaleDTO(sale);

        assertEquals(sale.getId(), dto.getId());
        assertEquals(sale.getSupplierForeignId(), dto.getSupplierForeignId());
        assertEquals(sale.getDevicePrice(), dto.getDevicePrice());
        assertEquals(sale.getFinalPrice(), dto.getFinalPrice());
        assertEquals(sale.getSaleDate(), dto.getSaleDate());
        assertEquals(sale.getCurrency(), dto.getCurrency());
        assertEquals(sale.getFinalized(), dto.getFinalized());
        assertEquals(sale.getSaleItems().size(), dto.getSaleItems().size());
        assertEquals(saleItem.getId(), dto.getSaleItems().get(0).getId());
        assertEquals(sale.getDevice().getId(), dto.getDevice().getId());
    }

    @Test
    void testFromSaleDTOShouldMapSaleDTOToSale() {
        SaleItem saleItem = new SaleItem();
        saleItem.setId(4L);

        SaleDTO dto = new SaleDTO();
        dto.setId(1L);
        dto.setSupplierForeignId(2L);
        dto.setDevicePrice(100.0);
        dto.setFinalPrice(150.0);
        dto.setSaleDate(Instant.now());
        dto.setCurrency("USD");
        dto.setFinalized(true);
        dto.setSaleItems(List.of(saleItem));

        Device device = new Device();
        device.setId(3L);
        dto.setDevice(device);

        Sale sale = SaleDTO.fromSaleDTO(dto);

        assertEquals(dto.getId(), sale.getId());
        assertEquals(dto.getSupplierForeignId(), sale.getSupplierForeignId());
        assertEquals(dto.getDevicePrice(), sale.getDevicePrice());
        assertEquals(dto.getFinalPrice(), sale.getFinalPrice());
        assertEquals(dto.getSaleDate(), sale.getSaleDate());
        assertEquals(dto.getCurrency(), sale.getCurrency());
        assertEquals(dto.getFinalized(), sale.getFinalized());
        assertEquals(dto.getSaleItems().size(), sale.getSaleItems().size());
        assertEquals(saleItem.getId(), sale.getSaleItems().iterator().next().getId());
        assertEquals(dto.getDevice().getId(), sale.getDevice().getId());
    }
}
