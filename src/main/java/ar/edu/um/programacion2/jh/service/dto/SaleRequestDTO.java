package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequestDTO implements Serializable {

    @JsonIgnore
    private Long id;

    @JsonProperty("idDispositivo")
    private Long deviceId;

    @JsonProperty("personalizaciones")
    private List<SaleItemDTO> customizations;

    @JsonProperty("adicionales")
    private List<SaleItemDTO> extras;

    @JsonProperty("precioFinal")
    private Double finalPrice;

    @JsonProperty("fechaVenta")
    private Instant saleDate;

    public static SaleRequestDTO toSaleRequestDTO(Sale sale) {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setId(sale.getId());
        saleRequestDTO.setDeviceId(sale.getDevice().getSupplierForeignId());
        saleRequestDTO.setCustomizations(
            sale
                .getSaleItems()
                .stream()
                .filter(item -> item.getOption() != null)
                .map(SaleItemDTO::fromSaleItem)
                .collect(Collectors.toList())
        );
        saleRequestDTO.setExtras(
            sale.getSaleItems().stream().filter(item -> item.getExtra() != null).map(SaleItemDTO::fromSaleItem).collect(Collectors.toList())
        );
        saleRequestDTO.setFinalPrice(sale.getFinalPrice());
        saleRequestDTO.setSaleDate(sale.getSaleDate());
        return saleRequestDTO;
    }

    public static Sale fromSaleRequestDTO(SaleRequestDTO saleRequestDTO, DeviceRepository deviceRepository, Long supplierForeignId) {
        Sale sale = new Sale();
        sale.setId(saleRequestDTO.getId());
        sale.setSupplierForeignId(supplierForeignId);
        Device device = deviceRepository
            .findBySupplierForeignId(saleRequestDTO.getDeviceId())
            .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + saleRequestDTO.getDeviceId()));
        sale.setDevice(device);
        sale.setDevicePrice(device.getBasePrice());
        sale.setFinalPrice(saleRequestDTO.getFinalPrice());
        sale.setSaleDate(saleRequestDTO.getSaleDate());
        sale.setCurrency(device.getCurrency());
        sale.setFinalized(true);
        Set<SaleItem> saleItems = new HashSet<>();
        saleItems.addAll(saleRequestDTO.getCustomizations().stream().map(SaleItemDTO::toSaleItem).collect(Collectors.toSet()));
        saleItems.addAll(saleRequestDTO.getExtras().stream().map(SaleItemDTO::toSaleItem).collect(Collectors.toSet()));
        sale.setSaleItems(saleItems);
        return sale;
    }
}
