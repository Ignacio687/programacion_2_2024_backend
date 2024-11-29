package ar.edu.um.programacion2.jh.service.dto;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class SaleRequestDTOTest {

    @Test
    void toSaleRequestDTOConvertsCorrectly() {
        Sale sale = new Sale();
        sale.setId(40L);
        Device device = new Device();
        device.setId(10L);
        device.setSupplierForeignId(70L);
        sale.setDevice(device);
        sale.setFinalPrice(200.0);
        sale.setSaleDate(Instant.now());
        SaleItem saleItem1 = new SaleItem();
        saleItem1.setId(1L);
        saleItem1.setOption(new Option());
        SaleItem saleItem2 = new SaleItem();
        saleItem2.setId(2L);
        saleItem2.setExtra(new Extra());
        sale.setSaleItems(Set.of(saleItem1, saleItem2));

        SaleRequestDTO dto = SaleRequestDTO.toSaleRequestDTO(sale);

        assertEquals(40L, dto.getId());
        assertEquals(dto.getDeviceId(), 70L);
        assertEquals(2, dto.getOptions().size() + dto.getExtras().size());
        assertTrue(dto.getOptions().stream().anyMatch(item -> item.getId().equals(1L)));
        assertTrue(dto.getExtras().stream().anyMatch(item -> item.getId().equals(2L)));
        assertEquals(200.0, dto.getFinalPrice());
        assertEquals(sale.getSaleDate(), dto.getSaleDate());
        assertEquals(10L, dto.getDevice().getId());
    }

    @Test
    void fromSaleRequestDTOConvertsCorrectly() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        User user = new User();
        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(100L);
        device.setBasePrice(150.0);
        device.setCurrency("USD");
        Mockito.when(deviceRepository.findBySupplierForeignId(100L)).thenReturn(Optional.of(device));

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setId(20L);
        dto.setDeviceId(100L);
        dto.setFinalPrice(200.0);
        dto.setSaleDate(Instant.now());
        dto.setOptions(List.of(new SaleItemDTO(1L, null, null, null)));
        dto.setExtras(List.of(new SaleItemDTO(2L, null, null, null)));

        MockedStatic<SaleItemDTO> toSaleItemMock = Mockito.mockStatic(SaleItemDTO.class);
        toSaleItemMock
            .when(() -> SaleItemDTO.toSaleItem(Mockito.any(SaleItemDTO.class)))
            .thenAnswer(invocation -> {
                SaleItemDTO saleItemDTO = invocation.getArgument(0);
                SaleItem saleItem = new SaleItem();
                saleItem.setId(saleItemDTO.getId());
                return saleItem;
            });

        Sale sale = SaleRequestDTO.fromSaleRequestDTO(dto, deviceRepository, 1L, user);
        toSaleItemMock.close();

        assertEquals(20L, sale.getId());
        assertEquals(1L, sale.getSupplierForeignId());
        assertEquals(200.0, sale.getFinalPrice());
        assertEquals(dto.getSaleDate(), sale.getSaleDate());
        assertEquals(2, sale.getSaleItems().size());
        assertTrue(sale.getSaleItems().stream().anyMatch(item -> item.getId().equals(1L)));
        assertTrue(sale.getSaleItems().stream().anyMatch(item -> item.getId().equals(2L)));
        assertEquals(device, sale.getDevice());
        assertEquals("USD", sale.getCurrency());
        assertEquals(150.0, sale.getDevicePrice());
        assertTrue(sale.getFinalized());
        assertEquals(user, sale.getUser());
    }

    @Test
    void fromSaleRequestDTOThrowsExceptionForInvalidDevice() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        Mockito.when(deviceRepository.findBySupplierForeignId(100L)).thenReturn(Optional.empty());

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(100L);
        MockedStatic<SaleItemDTO> toSaleItemMock = Mockito.mockStatic(SaleItemDTO.class);

        assertThrows(IllegalArgumentException.class, () -> SaleRequestDTO.fromSaleRequestDTO(dto, deviceRepository, 1L, new User()));
        toSaleItemMock.close();
    }

    @Test
    void setDeviceIdToExternalIdConvertsCorrectly() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(100L);
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(1L);
        SaleItemDTO option = Mockito.mock(SaleItemDTO.class);
        SaleItemDTO extra = Mockito.mock(SaleItemDTO.class);
        dto.setOptions(List.of(option));
        dto.setExtras(List.of(extra));

        dto.setDeviceIdToExternalId(deviceRepository, optionRepository, extraRepository);

        Mockito.verify(option).setObjectIdToExternalId(optionRepository, extraRepository);
        Mockito.verify(extra).setObjectIdToExternalId(optionRepository, extraRepository);
        assertEquals(100L, dto.getDeviceId());
    }

    @Test
    void setDeviceIdToExternalIdAcceptsNullLists() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(100L);
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(1L);
        dto.setOptions(null);
        dto.setExtras(null);

        dto.setDeviceIdToExternalId(deviceRepository, optionRepository, extraRepository);

        assertEquals(100L, dto.getDeviceId());
    }

    @Test
    void setDeviceIdToExternalIdThrowsExceptionForInvalidDevice() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Mockito.when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(1L);
        SaleItemDTO option = Mockito.mock(SaleItemDTO.class);
        SaleItemDTO extra = Mockito.mock(SaleItemDTO.class);
        dto.setOptions(List.of(option));
        dto.setExtras(List.of(extra));

        assertThrows(IllegalArgumentException.class, () -> dto.setDeviceIdToExternalId(deviceRepository, optionRepository, extraRepository)
        );
    }

    @Test
    void setDeviceIdToLocalIdConvertsCorrectly() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(100L);
        Mockito.when(deviceRepository.findBySupplierForeignId(100L)).thenReturn(Optional.of(device));

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(100L);
        SaleItemDTO option = Mockito.mock(SaleItemDTO.class);
        SaleItemDTO extra = Mockito.mock(SaleItemDTO.class);
        dto.setOptions(List.of(option));
        dto.setExtras(List.of(extra));

        dto.setDeviceIdToLocalId(deviceRepository, optionRepository, extraRepository);

        Mockito.verify(option).setObjectIdToLocalId(optionRepository, extraRepository);
        Mockito.verify(extra).setObjectIdToLocalId(optionRepository, extraRepository);
        assertEquals(1L, dto.getDeviceId());
    }

    @Test
    void setDeviceIdToLocalIdAcceptsNullLists() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Device device = new Device();
        device.setId(1L);
        device.setSupplierForeignId(100L);
        Mockito.when(deviceRepository.findBySupplierForeignId(100L)).thenReturn(Optional.of(device));

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(100L);
        dto.setOptions(null);
        dto.setExtras(null);

        dto.setDeviceIdToLocalId(deviceRepository, optionRepository, extraRepository);

        assertEquals(1L, dto.getDeviceId());
    }

    @Test
    void setDeviceIdToLocalIdThrowsExceptionForInvalidDevice() {
        DeviceRepository deviceRepository = Mockito.mock(DeviceRepository.class);
        OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
        ExtraRepository extraRepository = Mockito.mock(ExtraRepository.class);
        Mockito.when(deviceRepository.findBySupplierForeignId(100L)).thenReturn(Optional.empty());

        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setDeviceId(100L);
        SaleItemDTO option = Mockito.mock(SaleItemDTO.class);
        SaleItemDTO extra = Mockito.mock(SaleItemDTO.class);
        dto.setOptions(List.of(option));
        dto.setExtras(List.of(extra));

        assertThrows(IllegalArgumentException.class, () -> dto.setDeviceIdToLocalId(deviceRepository, optionRepository, extraRepository));
    }
}
