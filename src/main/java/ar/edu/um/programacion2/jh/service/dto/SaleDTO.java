package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDTO implements Serializable {

    private Long id;

    private Long supplierForeignId;

    private Double devicePrice;

    private Double finalPrice;

    private Instant saleDate;

    private String currency;

    private Boolean finalized;

    List<SaleItem> saleItems;

    private Device device;

    public static SaleDTO toSaleDTO(Sale sale) {
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setSupplierForeignId(sale.getSupplierForeignId());
        dto.setDevicePrice(sale.getDevicePrice());
        dto.setFinalPrice(sale.getFinalPrice());
        dto.setSaleDate(sale.getSaleDate());
        dto.setCurrency(sale.getCurrency());
        dto.setFinalized(sale.getFinalized());
        dto.setSaleItems(new ArrayList<>(sale.getSaleItems()));
        dto.setDevice(sale.getDevice());
        return dto;
    }

    public static Sale fromSaleDTO(SaleDTO dto) {
        Sale sale = new Sale();
        sale.setId(dto.getId());
        sale.setSupplierForeignId(dto.getSupplierForeignId());
        sale.setDevicePrice(dto.getDevicePrice());
        sale.setFinalPrice(dto.getFinalPrice());
        sale.setSaleDate(dto.getSaleDate());
        sale.setCurrency(dto.getCurrency());
        sale.setFinalized(dto.getFinalized());
        sale.setSaleItems(new HashSet<>(dto.getSaleItems()));
        sale.setDevice(dto.getDevice());
        return sale;
    }
}
