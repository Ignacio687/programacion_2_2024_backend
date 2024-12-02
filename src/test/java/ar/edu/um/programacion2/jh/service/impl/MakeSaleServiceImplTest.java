package ar.edu.um.programacion2.jh.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.*;
import ar.edu.um.programacion2.jh.service.*;
import ar.edu.um.programacion2.jh.service.client.SaleClient;
import ar.edu.um.programacion2.jh.service.dto.*;
import ar.edu.um.programacion2.jh.service.errors.InvalidSaleRequestException;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class MakeSaleServiceImplTest {

    private SaleService saleService;

    private DeviceRepository deviceRepository;

    private OptionRepository optionRepository;

    private ExtraRepository extraRepository;

    private SaleRepository saleRepository;

    private SaleClient saleClient;

    private UserService userService;

    private SaleItemService saleItemService;

    private MakeSaleService makeSaleService;

    private Logger log;

    private MockedStatic<LoggerFactory> mockedLoggerFactory;

    @BeforeEach
    void setUp() {
        this.saleService = mock(SaleService.class);
        this.deviceRepository = mock(DeviceRepository.class);
        this.optionRepository = mock(OptionRepository.class);
        this.extraRepository = mock(ExtraRepository.class);
        this.saleRepository = mock(SaleRepository.class);
        this.saleClient = mock(SaleClient.class);
        this.userService = mock(UserService.class);
        this.saleItemService = mock(SaleItemService.class);
        this.log = mock(Logger.class);

        this.mockedLoggerFactory = mockStatic(LoggerFactory.class);
        this.mockedLoggerFactory.when(() -> LoggerFactory.getLogger(MakeSaleServiceImpl.class)).thenReturn(this.log);

        this.makeSaleService = spy(
            new MakeSaleServiceImpl(
                this.saleService,
                this.deviceRepository,
                this.optionRepository,
                this.extraRepository,
                this.saleRepository,
                this.saleClient,
                this.userService,
                this.saleItemService
            )
        );
    }

    @AfterEach
    void tearDown() {
        this.mockedLoggerFactory.close();
    }

    @Test
    void saveValidSaleRequestSavesSale() throws InvalidSaleRequestException {
        SaleRequestDTO saleRequestDTO = spy(new SaleRequestDTO());
        saleRequestDTO.setDeviceId(2L);
        saleRequestDTO.setFinalPrice(100.0);

        Device device = new Device();
        device.setId(1L);
        device.setBasePrice(100.0);
        device.setActive(true);

        SaleItem saleItem = new SaleItem();
        saleItem.setId(10L);

        Sale sale = new Sale();
        sale.setId(1L);
        sale.setSupplierForeignId(2L);
        sale.setSaleItems(Set.of(saleItem));

        CompleteSaleDTO completeSaleDTO = new CompleteSaleDTO();
        completeSaleDTO.setSaleId(2L);

        User user = new User();
        user.setId(1L);

        MockedStatic<SaleRequestDTO> saleRequestDTOMockedStatic = mockStatic(SaleRequestDTO.class);

        doReturn(true).when(makeSaleService).verifyValidSale(any());
        doNothing().when(saleRequestDTO).setDeviceIdToExternalId(any(), any(), any());
        when(saleClient.createSale(any())).thenReturn(completeSaleDTO);
        when(userService.getUserWithAuthorities()).thenReturn(Optional.of(user));
        saleRequestDTOMockedStatic
            .when(() -> SaleRequestDTO.fromSaleRequestDTO(eq(saleRequestDTO), any(), eq(2L), eq(user)))
            .thenReturn(sale);
        when(saleService.save(any())).thenReturn(sale);
        when(saleItemService.save(any())).thenReturn(saleItem);

        SaleDTO result = makeSaleService.save(saleRequestDTO);

        assertNotNull(result);
        assertEquals(2L, result.getSupplierForeignId());

        ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);
        ArgumentCaptor<SaleRequestDTO> saleRequestCaptor = ArgumentCaptor.forClass(SaleRequestDTO.class);
        ArgumentCaptor<SaleItem> saleItemCaptor = ArgumentCaptor.forClass(SaleItem.class);

        verify(saleService, times(1)).save(saleCaptor.capture());
        verify(saleItemService, times(1)).save(saleItemCaptor.capture());
        verify(saleClient, times(1)).createSale(saleRequestCaptor.capture());
        verify(this.log).debug("Request to save Sale : {}", saleRequestDTO);
        verify(this.log).debug("Saved Sale : {}", sale);

        Sale capturedSale = saleCaptor.getValue();
        SaleRequestDTO capturedSaleRequest = saleRequestCaptor.getValue();
        SaleItem capturedSaleItem = saleItemCaptor.getValue();

        sale.setSaleItems(null);
        saleItem.setSale(sale);

        assertEquals(sale, capturedSale);
        assertEquals(saleRequestDTO, capturedSaleRequest);
        assertEquals(saleItem, capturedSaleItem);

        saleRequestDTOMockedStatic.close();
    }

    @Test
    void saveInvalidSaleRequestThrowsException() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);
        saleRequestDTO.setFinalPrice(100.0);

        doReturn(false).when(makeSaleService).verifyValidSale(any());

        assertThrows(InvalidSaleRequestException.class, () -> makeSaleService.save(saleRequestDTO));
        verify(this.log).warn("Invalid sale request: {}", saleRequestDTO);
    }

    @Test
    void saveSaleRequestWithInvalidUserThrowsException() {
        SaleRequestDTO saleRequestDTO = spy(new SaleRequestDTO());
        saleRequestDTO.setDeviceId(2L);
        saleRequestDTO.setFinalPrice(100.0);

        when(userService.getUserWithAuthorities()).thenReturn(Optional.empty());

        assertThrows(InvalidSaleRequestException.class, () -> makeSaleService.save(saleRequestDTO));
        verify(this.log).debug("Request to save Sale : {}", saleRequestDTO);
    }

    @Test
    void findAllLocalSales() {
        Pageable pageable = PageRequest.of(0, 10);
        Sale sale = new Sale();
        sale.setId(1L);
        Page<Sale> salesPage = new PageImpl<>(List.of(sale), pageable, 1);
        MockedStatic<SaleListDTO> saleListDTOMockedStatic = mockStatic(SaleListDTO.class);

        when(saleRepository.findByUserIsCurrentUser(pageable)).thenReturn(salesPage);
        saleListDTOMockedStatic.when(() -> SaleListDTO.fromSaleItem(sale)).thenReturn(new SaleListDTO());

        Page<SaleListDTO> result = makeSaleService.findAll(pageable, true);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(saleRepository, times(1)).findByUserIsCurrentUser(pageable);
        verify(this.log).debug("Request to get all Sales, local: {}", true);

        saleListDTOMockedStatic.close();
    }

    @Test
    void findAllExternalSales() {
        Pageable pageable = PageRequest.of(0, 10);
        SaleListDTO saleListDTO = new SaleListDTO();
        saleListDTO.setSaleId(1L);
        List<SaleListDTO> salesList = List.of(saleListDTO);

        when(saleClient.getSales()).thenReturn(salesList);

        Page<SaleListDTO> result = makeSaleService.findAll(pageable, false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(saleClient, times(1)).getSales();
        verify(this.log).debug("Request to get all Sales, local: {}", false);
        verify(this.log).debug("Found {} external sales", salesList.size());
    }

    @Test
    void findAllExternalSalesNoSalesFound() {
        Pageable pageable = PageRequest.of(0, 10);

        when(saleClient.getSales()).thenReturn(null);

        Page<SaleListDTO> result = makeSaleService.findAll(pageable, false);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(saleClient, times(1)).getSales();
        verify(this.log).debug("Request to get all Sales, local: {}", false);
        verify(this.log).debug("No external sales found");
    }

    @Test
    void findOneLocalSaleFound() {
        Long saleId = 1L;
        Sale sale = new Sale();
        sale.setId(saleId);
        User user = new User();
        user.setId(1L);
        sale.setUser(user);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(userService.getUserWithAuthorities()).thenReturn(Optional.of(user));
        MockedStatic<CompleteSaleDTO> completeSaleDTOMockedStatic = mockStatic(CompleteSaleDTO.class);
        completeSaleDTOMockedStatic.when(() -> CompleteSaleDTO.fromSaleItem(sale)).thenReturn(new CompleteSaleDTO());

        Optional<CompleteSaleDTO> result = makeSaleService.findOne(saleId, true);

        assertTrue(result.isPresent());
        verify(saleRepository, times(1)).findById(saleId);
        verify(userService, times(1)).getUserWithAuthorities();

        completeSaleDTOMockedStatic.close();
        verify(this.log).debug("Fetching local sale with id: {}", saleId);
    }

    @Test
    void findOneLocalSaleNotFound() {
        Long saleId = 1L;
        User user = new User();
        user.setId(1L);

        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());
        when(userService.getUserWithAuthorities()).thenReturn(Optional.of(user));

        Optional<CompleteSaleDTO> result = makeSaleService.findOne(saleId, true);

        assertFalse(result.isPresent());
        verify(saleRepository, times(1)).findById(saleId);
        verify(userService, times(1)).getUserWithAuthorities();
        verify(this.log).debug("Fetching local sale with id: {}", saleId);
    }

    @Test
    void findOneExternalSaleFound() {
        Long saleId = 1L;
        CompleteSaleDTO completeSaleDTO = new CompleteSaleDTO();
        completeSaleDTO.setSaleId(saleId);

        when(saleClient.getSaleById(saleId)).thenReturn(completeSaleDTO);

        Optional<CompleteSaleDTO> result = makeSaleService.findOne(saleId, false);

        assertTrue(result.isPresent());
        verify(saleClient, times(1)).getSaleById(saleId);
        verify(this.log).debug("Fetching local sale with id: {}", saleId);
    }

    @Test
    void findOneExternalSaleNotFound() {
        Long saleId = 1L;

        when(saleClient.getSaleById(saleId)).thenReturn(null);

        Optional<CompleteSaleDTO> result = makeSaleService.findOne(saleId, false);

        assertFalse(result.isPresent());
        verify(saleClient, times(1)).getSaleById(saleId);
        verify(this.log).warn("External sale not found for id: {}", saleId);
        verify(this.log).debug("Fetching local sale with id: {}", saleId);
    }

    @Test
    void findOneLocalSaleUserNotFound() {
        Long saleId = 1L;

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(new Sale()));
        when(userService.getUserWithAuthorities()).thenReturn(Optional.empty());

        assertThrows(InvalidSaleRequestException.class, () -> makeSaleService.findOne(saleId, true));
        verify(this.log).debug("Fetching local sale with id: {}", saleId);
    }

    @Test
    void findOneLocalSaleUserMismatch() {
        Long saleId = 1L;
        Sale sale = new Sale();
        sale.setId(saleId);
        User saleUser = new User();
        saleUser.setId(2L); // Different user ID
        sale.setUser(saleUser);
        User currentUser = new User();
        currentUser.setId(1L);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(userService.getUserWithAuthorities()).thenReturn(Optional.of(currentUser));

        Optional<CompleteSaleDTO> result = makeSaleService.findOne(saleId, true);

        assertFalse(result.isPresent());
        verify(saleRepository, times(1)).findById(saleId);
        verify(userService, times(1)).getUserWithAuthorities();
        verify(this.log).debug("Fetching local sale with id: {}", saleId);
    }

    @Test
    void verifyValidSaleWithValidRequestReturnsTrue() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);
        saleRequestDTO.setFinalPrice(150.0);

        Device device = new Device();
        device.setId(1L);
        device.setBasePrice(100.0);
        device.setActive(true);

        Option option = new Option();
        option.setId(1L);
        option.setAdditionalPrice(20.0);

        Extra extra = new Extra();
        extra.setId(1L);
        extra.setPrice(30.0);
        extra.setFreePrice(-1.0);

        Customization customization = new Customization();
        customization.setOptions(Set.of(option));

        device.setCustomizations(Set.of(customization));
        device.setExtras(Set.of(extra));

        SaleItemDTO optionDTO = new SaleItemDTO();
        optionDTO.setObjectId(1L);
        optionDTO.setPrice(20.0);

        SaleItemDTO extraDTO = new SaleItemDTO();
        extraDTO.setObjectId(1L);
        extraDTO.setPrice(30.0);

        saleRequestDTO.setOptions(List.of(optionDTO));
        saleRequestDTO.setExtras(List.of(extraDTO));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        boolean result = makeSaleService.verifyValidSale(saleRequestDTO);

        assertTrue(result);
        verify(this.log).debug("Verifying sale request: {}", saleRequestDTO);
    }

    @Test
    void verifyValidSaleWithInvalidDeviceReturnsFalse() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);

        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = makeSaleService.verifyValidSale(saleRequestDTO);

        assertFalse(result);
        verify(this.log).warn("Device not found for id: {}", saleRequestDTO.getDeviceId());
    }

    @Test
    void verifyValidSaleWithInactiveDeviceReturnsFalse() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);

        Device device = new Device();
        device.setId(1L);
        device.setActive(false);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        boolean result = makeSaleService.verifyValidSale(saleRequestDTO);

        assertFalse(result);
        verify(this.log).warn("Device is not active: {}", device);
    }

    @Test
    void verifyValidSaleWithInvalidOptionReturnsFalse() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);
        saleRequestDTO.setFinalPrice(120.0);

        Device device = new Device();
        device.setId(1L);
        device.setBasePrice(100.0);
        device.setActive(true);

        Option option = new Option();
        option.setId(1L);
        option.setAdditionalPrice(20.0);

        Customization customization = new Customization();
        customization.setOptions(Set.of(option));

        device.setCustomizations(Set.of(customization));

        SaleItemDTO optionDTO = new SaleItemDTO();
        optionDTO.setObjectId(2L); // Invalid option ID
        optionDTO.setPrice(20.0);

        saleRequestDTO.setOptions(List.of(optionDTO));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        boolean result = makeSaleService.verifyValidSale(saleRequestDTO);

        assertFalse(result);
        verify(this.log).warn("Invalid option in sale request: {}", optionDTO);
    }

    @Test
    void verifyValidSaleWithInvalidExtraReturnsFalse() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);
        saleRequestDTO.setFinalPrice(130.0);
        saleRequestDTO.setOptions(new ArrayList<>());

        Device device = new Device();
        device.setId(1L);
        device.setBasePrice(100.0);
        device.setActive(true);

        Extra extra = new Extra();
        extra.setId(1L);
        extra.setPrice(30.0);
        extra.setFreePrice(-1.0);

        device.setExtras(Set.of(extra));

        SaleItemDTO extraDTO = new SaleItemDTO();
        extraDTO.setObjectId(2L); // Invalid extra ID
        extraDTO.setPrice(30.0);

        saleRequestDTO.setExtras(List.of(extraDTO));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        boolean result = makeSaleService.verifyValidSale(saleRequestDTO);

        assertFalse(result);
        verify(this.log).warn("Invalid extra in sale request: {}", extraDTO);
    }

    @Test
    void verifyValidSaleWithPriceMismatchReturnsFalse() {
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setDeviceId(1L);
        saleRequestDTO.setFinalPrice(200.0); // Mismatched price

        Device device = new Device();
        device.setId(1L);
        device.setBasePrice(100.0);
        device.setActive(true);

        Option option = new Option();
        option.setId(1L);
        option.setAdditionalPrice(20.0);

        Extra extra = new Extra();
        extra.setId(1L);
        extra.setPrice(30.0);
        extra.setFreePrice(-1.0);

        Customization customization = new Customization();
        customization.setOptions(Set.of(option));

        device.setCustomizations(Set.of(customization));
        device.setExtras(Set.of(extra));

        SaleItemDTO optionDTO = new SaleItemDTO();
        optionDTO.setObjectId(1L);
        optionDTO.setPrice(20.0);

        SaleItemDTO extraDTO = new SaleItemDTO();
        extraDTO.setObjectId(1L);
        extraDTO.setPrice(30.0);

        saleRequestDTO.setOptions(List.of(optionDTO));
        saleRequestDTO.setExtras(List.of(extraDTO));

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        boolean result = makeSaleService.verifyValidSale(saleRequestDTO);

        assertFalse(result);
        verify(this.log).warn("Total price mismatch: calculated = {}, expected = {}", 150.0, saleRequestDTO.getFinalPrice());
    }
}
