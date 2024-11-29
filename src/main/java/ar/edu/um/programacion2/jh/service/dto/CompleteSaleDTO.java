package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Sale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteSaleDTO implements Serializable {

    @JsonProperty("idVenta")
    private Long saleId;

    @JsonProperty("idDispositivo")
    private Long deviceId;

    @JsonIgnore
    private Long supplierForeignId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("precioBase")
    private Double basePrice;

    @JsonProperty("moneda")
    private String currency;

    @JsonProperty("caracteristicas")
    private List<CharacteristicDTO> characteristics;

    @JsonProperty("personalizaciones")
    private List<CustomizationDTO> customizations;

    @JsonProperty("adicionales")
    private List<ExtraDTO> extras;

    @JsonProperty("precioFinal")
    private Double finalPrice;

    @JsonProperty("fechaVenta")
    private Instant saleDate;

    public static CompleteSaleDTO fromSaleItem(Sale sale) {
        CompleteSaleDTO dto = new CompleteSaleDTO();
        dto.setSaleId(sale.getId());
        dto.setDeviceId(sale.getDevice().getId());
        dto.setSupplierForeignId(sale.getSupplierForeignId());
        dto.setCode(sale.getDevice().getCode());
        dto.setName(sale.getDevice().getName());
        dto.setDescription(sale.getDevice().getDescription());
        dto.setBasePrice(sale.getDevicePrice());
        dto.setCurrency(sale.getCurrency());
        dto.setCharacteristics(
            sale.getDevice().getCharacteristics().stream().map(CharacteristicDTO::fromCharacteristic).collect(Collectors.toList())
        );
        dto.setCustomizations(
            sale.getDevice().getCustomizations().stream().map(CustomizationDTO::fromCustomization).collect(Collectors.toList())
        );
        dto.setExtras(sale.getDevice().getExtras().stream().map(ExtraDTO::fromExtra).collect(Collectors.toList()));
        dto.setFinalPrice(sale.getFinalPrice());
        dto.setSaleDate(sale.getSaleDate());
        return dto;
    }
}
