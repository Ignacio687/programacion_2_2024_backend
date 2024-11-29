package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Sale;
import org.junit.jupiter.api.Test;

class SaleListDTOTest {

    @Test
    void fromSaleItemConvertsCorrectly() {
        Sale sale = new Sale();
        sale.setId(1L);
        Device device = new Device();
        device.setId(2L);
        device.setCode("D123");
        device.setName("DeviceName");
        device.setDescription("DeviceDescription");
        sale.setDevice(device);
        sale.setDevicePrice(300.0);

        SaleListDTO dto = SaleListDTO.fromSaleItem(sale);

        assertEquals(1L, dto.getSaleId());
        assertEquals(2L, dto.getId());
        assertEquals("D123", dto.getCode());
        assertEquals("DeviceName", dto.getName());
        assertEquals("DeviceDescription", dto.getDescription());
        assertEquals(300.0, dto.getPrice());
    }
}
