package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemDTO implements Serializable {

    @JsonIgnore
    private Long id;

    @JsonProperty("id")
    private Long objectId;

    @JsonProperty("precio")
    private Double price;

    // Option or Extra
    @JsonIgnore
    private OptionalDTO optional;

    public static SaleItem toSaleItem(SaleItemDTO dto) {
        SaleItem saleItem = new SaleItem();
        saleItem.setId(dto.getId());
        saleItem.setPrice(dto.getPrice());
        if (dto.getOptional().getClass() == OptionDTO.class) {
            saleItem.setOption(OptionDTO.toOption((OptionDTO) dto.getOptional()));
        } else {
            saleItem.setExtra(ExtraDTO.toExtra((ExtraDTO) dto.getOptional()));
        }
        return saleItem;
    }

    public static SaleItemDTO fromSaleItem(SaleItem saleItem) {
        SaleItemDTO dto = new SaleItemDTO();
        dto.setId(saleItem.getId());
        dto.setPrice(saleItem.getPrice());
        if (saleItem.getOption() != null) {
            dto.setOptional(OptionDTO.fromOption(saleItem.getOption()));
            dto.setObjectId(saleItem.getOption().getId());
        } else if (saleItem.getExtra() != null) {
            dto.setOptional(ExtraDTO.fromExtra(saleItem.getExtra()));
            dto.setObjectId(saleItem.getExtra().getId());
        }
        return dto;
    }
}
