package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SaleItemDTOTest {

    @Test
    void toSaleItemConvertsCorrectlyOption() {
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setId(2L);
        SaleItemDTO dto = new SaleItemDTO(10L, 1L, 150.0, optionDTO);
        SaleItem saleItem = SaleItemDTO.toSaleItem(dto);

        assertEquals(10L, saleItem.getId());
        assertEquals(150.0, saleItem.getPrice());
        assertNotNull(saleItem.getOption());
        assertEquals(2L, saleItem.getOption().getId());
        assertNull(saleItem.getExtra());
    }

    @Test
    void toSaleItemConvertsCorrectlyExtra() {
        ExtraDTO extraDTO = new ExtraDTO();
        extraDTO.setId(2L);
        SaleItemDTO dto = new SaleItemDTO(10L, 1L, 150.0, extraDTO);
        SaleItem saleItem = SaleItemDTO.toSaleItem(dto);

        assertEquals(10L, saleItem.getId());
        assertEquals(150.0, saleItem.getPrice());
        assertNotNull(saleItem.getExtra());
        assertEquals(2L, saleItem.getExtra().getId());
        assertNull(saleItem.getOption());
    }

    @Test
    void fromSaleItemConvertsCorrectlyOption() {
        Option option = new Option();
        option.setId(2L);
        SaleItem saleItem = new SaleItem();
        saleItem.setId(10L);
        saleItem.setPrice(150.0);
        saleItem.setOption(option);

        SaleItemDTO dto = SaleItemDTO.fromSaleItem(saleItem);

        assertEquals(10L, dto.getId());
        assertEquals(150.0, dto.getPrice());
        assertEquals(2L, dto.getObjectId());
        assertNotNull(dto.getOptional());
        assertInstanceOf(OptionDTO.class, dto.getOptional());
        assertEquals(2L, dto.getObjectId());
    }

    @Test
    void fromSaleItemConvertsCorrectlyExtra() {
        Extra extra = new Extra();
        extra.setId(2L);
        SaleItem saleItem = new SaleItem();
        saleItem.setId(10L);
        saleItem.setPrice(150.0);
        saleItem.setExtra(extra);

        SaleItemDTO dto = SaleItemDTO.fromSaleItem(saleItem);

        assertEquals(10L, dto.getId());
        assertEquals(150.0, dto.getPrice());
        assertEquals(2L, dto.getObjectId());
        assertNotNull(dto.getOptional());
        assertInstanceOf(ExtraDTO.class, dto.getOptional());
        assertEquals(2L, dto.getObjectId());
    }

    @Test
    void setObjectIdToExternalIdConvertsCorrectlyOption() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Option option = new Option();
        option.setId(1L);
        option.setSupplierForeignId(100L);
        Mockito.when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        SaleItemDTO dto = new SaleItemDTO(10L, 1L, 150.0, new OptionDTO());
        dto.setObjectIdToExternalId(optionRepository, extraRepository);

        assertEquals(100L, dto.getObjectId());
        assertEquals(dto.getOptional().getId(), 1L);
    }

    @Test
    void setObjectIdToExternalIdConvertsCorrectlyExtra() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Extra extra = new Extra();
        extra.setId(1L);
        extra.setSupplierForeignId(100L);
        Mockito.when(extraRepository.findById(1L)).thenReturn(Optional.of(extra));

        SaleItemDTO dto = new SaleItemDTO(10L, 1L, 150.0, new ExtraDTO());
        dto.setObjectIdToExternalId(optionRepository, extraRepository);

        assertEquals(100L, dto.getObjectId());
        assertEquals(dto.getOptional().getId(), 1L);
    }

    @Test
    void setObjectIdToExternalIdThrowsExceptionForInvalidOption() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Mockito.when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        SaleItemDTO dto = new SaleItemDTO(10L, 1L, 150.0, new OptionDTO());

        assertThrows(IllegalArgumentException.class, () -> {
            dto.setObjectIdToExternalId(optionRepository, extraRepository);
        });
    }

    @Test
    void setObjectIdToExternalIdThrowsExceptionForInvalidExtra() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Mockito.when(extraRepository.findById(1L)).thenReturn(Optional.empty());

        SaleItemDTO dto = new SaleItemDTO(10L, 1L, 150.0, new ExtraDTO());

        assertThrows(IllegalArgumentException.class, () -> {
            dto.setObjectIdToExternalId(optionRepository, extraRepository);
        });
    }

    @Test
    void setObjectIdToLocalIdConvertsCorrectlyOption() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Option option = new Option();
        option.setId(1L);
        option.setSupplierForeignId(100L);
        Mockito.when(optionRepository.findBySupplierForeignId(100L)).thenReturn(Optional.of(option));

        SaleItemDTO dto = new SaleItemDTO(10L, 100L, 150.0, new OptionDTO());
        dto.setObjectIdToLocalId(optionRepository, extraRepository);

        assertEquals(1L, dto.getObjectId());
        assertEquals(dto.getOptional().getId(), 1L);
    }

    @Test
    void setObjectIdToLocalIdConvertsCorrectlyExtra() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Extra extra = new Extra();
        extra.setId(1L);
        extra.setSupplierForeignId(100L);
        Mockito.when(extraRepository.findBySupplierForeignId(100L)).thenReturn(Optional.of(extra));

        SaleItemDTO dto = new SaleItemDTO(10L, 100L, 150.0, new ExtraDTO());
        dto.setObjectIdToLocalId(optionRepository, extraRepository);

        assertEquals(1L, dto.getObjectId());
        assertEquals(dto.getOptional().getId(), 1L);
    }

    @Test
    void setObjectIdToLocalIdThrowsExceptionForInvalidOption() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Mockito.when(optionRepository.findBySupplierForeignId(100L)).thenReturn(Optional.empty());

        SaleItemDTO dto = new SaleItemDTO(10L, 100L, 150.0, new OptionDTO());

        assertThrows(IllegalArgumentException.class, () -> {
            dto.setObjectIdToLocalId(optionRepository, extraRepository);
        });
    }

    @Test
    void setObjectIdToLocalIdThrowsExceptionForInvalidExtra() {
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Mockito.when(extraRepository.findBySupplierForeignId(100L)).thenReturn(Optional.empty());

        SaleItemDTO dto = new SaleItemDTO(10L, 100L, 150.0, new ExtraDTO());

        assertThrows(IllegalArgumentException.class, () -> {
            dto.setObjectIdToLocalId(optionRepository, extraRepository);
        });
    }
}
