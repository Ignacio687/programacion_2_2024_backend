package ar.edu.um.programacion2.jh.service.dto;

import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @JsonProperty("id")
    private Long objectId;

    @NotNull
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

    public void setObjectIdToExternalId(OptionRepository optionRepository, ExtraRepository extraRepository)
        throws IllegalArgumentException {
        if (this.optional instanceof OptionDTO) {
            Option option = optionRepository
                .findById(this.objectId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found with id: " + this.objectId));
            this.objectId = option.getSupplierForeignId();
            this.optional = OptionDTO.fromOption(option);
        } else if (this.optional instanceof ExtraDTO) {
            Extra extra = extraRepository
                .findById(this.objectId)
                .orElseThrow(() -> new IllegalArgumentException("Extra not found with id: " + this.objectId));
            this.objectId = extra.getSupplierForeignId();
            this.optional = ExtraDTO.fromExtra(extra);
        }
    }

    public void setObjectIdToLocalId(OptionRepository optionRepository, ExtraRepository extraRepository) throws IllegalArgumentException {
        if (this.optional instanceof OptionDTO) {
            Option option = optionRepository
                .findBySupplierForeignId(this.objectId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found with supplier foreign id: " + this.objectId));
            this.objectId = option.getId();
            this.optional = OptionDTO.fromOption(option);
        } else if (this.optional instanceof ExtraDTO) {
            Extra extra = extraRepository
                .findBySupplierForeignId(this.objectId)
                .orElseThrow(() -> new IllegalArgumentException("Extra not found with supplier foreign id: " + this.objectId));
            this.objectId = extra.getId();
            this.optional = ExtraDTO.fromExtra(extra);
        }
    }
}
