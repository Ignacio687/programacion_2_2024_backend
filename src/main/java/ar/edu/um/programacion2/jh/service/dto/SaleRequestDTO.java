package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.domain.User;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import ar.edu.um.programacion2.jh.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty("idDispositivo")
    private Long deviceId;

    @NotNull
    @JsonProperty("personalizaciones")
    private List<SaleItemDTO> options;

    @NotNull
    @JsonProperty("adicionales")
    private List<SaleItemDTO> extras;

    @NotNull
    @JsonProperty("precioFinal")
    private Double finalPrice;

    @NotNull
    @JsonProperty("fechaVenta")
    private Instant saleDate;

    @JsonIgnore
    private DeviceDTO device;

    public static SaleRequestDTO toSaleRequestDTO(Sale sale) {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setId(sale.getId());
        saleRequestDTO.setDeviceId(sale.getDevice().getSupplierForeignId());
        saleRequestDTO.setOptions(
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

    public static Sale fromSaleRequestDTO(
        SaleRequestDTO saleRequestDTO,
        DeviceRepository deviceRepository,
        Long supplierForeignId,
        User user
    ) {
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
        saleItems.addAll(saleRequestDTO.getOptions().stream().map(SaleItemDTO::toSaleItem).collect(Collectors.toSet()));
        saleItems.addAll(saleRequestDTO.getExtras().stream().map(SaleItemDTO::toSaleItem).collect(Collectors.toSet()));
        sale.setSaleItems(saleItems);
        sale.setUser(user);
        return sale;
    }

    public void setDeviceIdToExternalId(
        DeviceRepository deviceRepository,
        OptionRepository optionRepository,
        ExtraRepository extraRepository
    ) {
        Device device = deviceRepository
            .findById(this.deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + this.deviceId));
        this.deviceId = device.getSupplierForeignId();
        this.device = DeviceDTO.fromDevice(device);
        if (this.options != null) {
            this.options.forEach(option -> {
                    option.setOptional(new OptionDTO());
                    option.setObjectIdToExternalId(optionRepository, extraRepository);
                });
        }
        if (this.extras != null) {
            this.extras.forEach(extra -> {
                    extra.setOptional(new ExtraDTO());
                    extra.setObjectIdToExternalId(optionRepository, extraRepository);
                });
        }
    }

    public void setDeviceIdToLocalId(
        DeviceRepository deviceRepository,
        OptionRepository optionRepository,
        ExtraRepository extraRepository
    ) {
        Device device = deviceRepository
            .findBySupplierForeignId(this.deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Device not found with supplier foreign id: " + this.deviceId));
        this.deviceId = device.getId();
        this.device = DeviceDTO.fromDevice(device);
        if (this.options != null) {
            this.options.forEach(option -> {
                    option.setOptional(new OptionDTO());
                    option.setObjectIdToLocalId(optionRepository, extraRepository);
                });
        }
        if (this.extras != null) {
            this.extras.forEach(extra -> {
                    extra.setOptional(new ExtraDTO());
                    extra.setObjectIdToLocalId(optionRepository, extraRepository);
                });
        }
    }
}
