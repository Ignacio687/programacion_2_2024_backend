package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import org.junit.jupiter.api.Test;

public class SaleItemDTOTest {

    @Test
    void toSaleItemConvertsCorrectly() {
        SaleItemDTO dto = new SaleItemDTO(1L, 150.0);
        SaleItem saleItem = SaleItemDTO.toSaleItem(dto);

        assertEquals(1L, saleItem.getId());
        assertEquals(150.0, saleItem.getPrice());
    }

    @Test
    void toSaleItemHandlesNullValues() {
        SaleItemDTO dto = new SaleItemDTO(null, null);
        SaleItem saleItem = SaleItemDTO.toSaleItem(dto);

        assertNull(saleItem.getId());
        assertNull(saleItem.getPrice());
    }

    @Test
    void toSaleItemHandlesZeroPrice() {
        SaleItemDTO dto = new SaleItemDTO(1L, 0.0);
        SaleItem saleItem = SaleItemDTO.toSaleItem(dto);

        assertEquals(1L, saleItem.getId());
        assertEquals(0.0, saleItem.getPrice());
    }
}
