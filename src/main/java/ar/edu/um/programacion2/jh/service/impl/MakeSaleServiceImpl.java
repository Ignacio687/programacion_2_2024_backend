package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.*;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import ar.edu.um.programacion2.jh.repository.SaleRepository;
import ar.edu.um.programacion2.jh.service.MakeSaleService;
import ar.edu.um.programacion2.jh.service.SaleItemService;
import ar.edu.um.programacion2.jh.service.SaleService;
import ar.edu.um.programacion2.jh.service.client.SaleClient;
import ar.edu.um.programacion2.jh.service.dto.*;
import ar.edu.um.programacion2.jh.service.errors.InvalidSaleRequestException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MakeSaleServiceImpl implements MakeSaleService {

    private static final Logger LOG = LoggerFactory.getLogger(MakeSaleServiceImpl.class);

    private final SaleService saleService;

    private final DeviceRepository deviceRepository;

    private final OptionRepository optionRepository;

    private final ExtraRepository extraRepository;

    private final SaleRepository saleRepository;

    private final SaleClient saleClient;

    private final UserService userService;

    private final SaleItemService saleItemService;

    public MakeSaleServiceImpl(
        SaleService saleService,
        DeviceRepository deviceRepository,
        OptionRepository optionRepository,
        ExtraRepository extraRepository,
        SaleRepository saleRepository,
        SaleClient saleClient,
        UserService userService,
        SaleItemService saleItemService
    ) {
        this.saleService = saleService;
        this.deviceRepository = deviceRepository;
        this.optionRepository = optionRepository;
        this.extraRepository = extraRepository;
        this.saleRepository = saleRepository;
        this.saleClient = saleClient;
        this.userService = userService;
        this.saleItemService = saleItemService;
    }

    @Override
    public SaleDTO save(SaleRequestDTO saleRequestDTO) throws InvalidSaleRequestException {
        LOG.debug("Request to save Sale : {}", saleRequestDTO);
        if (!verifyValidSale(saleRequestDTO)) {
            LOG.warn("Invalid sale request: {}", saleRequestDTO);
            throw new InvalidSaleRequestException("Invalid sale request");
        }
        Optional<User> currentUser = this.userService.getUserWithAuthorities();
        if (currentUser.isEmpty()) {
            throw new InvalidSaleRequestException("User not found");
        }
        saleRequestDTO.setDeviceIdToExternalId(this.deviceRepository, this.optionRepository, this.extraRepository);
        CompleteSaleDTO externalSale = this.saleClient.createSale(saleRequestDTO);
        Sale sale = SaleRequestDTO.fromSaleRequestDTO(saleRequestDTO, this.deviceRepository, externalSale.getSaleId(), currentUser.get());
        Set<SaleItem> saleItemSet = sale.getSaleItems();
        sale.setSaleItems(null);
        Sale savedSale = this.saleService.save(sale);
        for (SaleItem saleItem : saleItemSet) {
            saleItem.setSale(savedSale);
            this.saleItemService.save(saleItem);
        }
        savedSale.setSaleItems(saleItemSet);
        LOG.debug("Saved Sale : {}", savedSale);
        return SaleDTO.toSaleDTO(savedSale);
    }

    @Override
    public Page<SaleListDTO> findAll(Pageable pageable, boolean local) {
        LOG.debug("Request to get all Sales, local: {}", local);
        if (local) {
            Page<Sale> salesPage = this.saleRepository.findByUserIsCurrentUser(pageable);
            return salesPage.map(SaleListDTO::fromSaleItem);
        }
        List<SaleListDTO> salesList = this.saleClient.getSales();
        if (salesList == null) {
            LOG.debug("No external sales found");
            return new PageImpl<>(List.of(), pageable, 0);
        }
        LOG.debug("Found {} external sales", salesList.size());
        return new PageImpl<>(salesList, pageable, salesList.size());
    }

    @Override
    public Optional<CompleteSaleDTO> findOne(Long id, boolean local) {
        if (local) {
            LOG.debug("Fetching local sale with id: {}", id);
            Optional<Sale> sale = this.saleRepository.findById(id);
            Optional<User> currentUser = this.userService.getUserWithAuthorities();
            if (currentUser.isEmpty()) {
                throw new InvalidSaleRequestException("User not found");
            }
            if (sale.isEmpty() || !sale.get().getUser().getId().equals(currentUser.get().getId())) {
                return Optional.empty();
            }
            CompleteSaleDTO saleDTO = CompleteSaleDTO.fromSaleItem(sale.get());
            return Optional.of(saleDTO);
        } else {
            CompleteSaleDTO sale = this.saleClient.getSaleById(id);
            if (sale == null) {
                LOG.warn("External sale not found for id: {}", id);
                return Optional.empty();
            }
            return Optional.of(sale);
        }
    }

    @Override
    public Boolean verifyValidSale(SaleRequestDTO saleRequestDTO) {
        LOG.debug("Verifying sale request: {}", saleRequestDTO);
        Device device = deviceRepository.findById(saleRequestDTO.getDeviceId()).orElse(null);
        if (device == null) {
            LOG.warn("Device not found for id: {}", saleRequestDTO.getDeviceId());
            return false;
        } else if (!device.getActive()) {
            LOG.warn("Device is not active: {}", device);
            return false;
        }
        double totalPrice = device.getBasePrice();
        Set<Option> localOptions = device
            .getCustomizations()
            .stream()
            .flatMap(customization -> customization.getOptions().stream())
            .collect(Collectors.toSet());
        for (SaleItemDTO option : saleRequestDTO.getOptions()) {
            Option localOption = localOptions.stream().filter(opt -> opt.getId().equals(option.getObjectId())).findFirst().orElse(null);
            if (localOption == null || !option.getPrice().equals(localOption.getAdditionalPrice())) {
                LOG.warn("Invalid option in sale request: {}", option);
                return false;
            }
            totalPrice += localOption.getAdditionalPrice();
        }
        double freePrice = totalPrice;
        for (SaleItemDTO extra : saleRequestDTO.getExtras()) {
            Extra localExtra = device.getExtras().stream().filter(ext -> ext.getId().equals(extra.getObjectId())).findFirst().orElse(null);
            if (localExtra == null || !extra.getPrice().equals(localExtra.getPrice())) {
                LOG.warn("Invalid extra in sale request: {}", extra);
                return false;
            }
            if (localExtra.getFreePrice() >= freePrice || localExtra.getFreePrice() == -1) {
                totalPrice += localExtra.getPrice();
            }
        }
        boolean isValid = totalPrice == saleRequestDTO.getFinalPrice();
        if (!isValid) {
            LOG.warn("Total price mismatch: calculated = {}, expected = {}", totalPrice, saleRequestDTO.getFinalPrice());
        }
        return isValid;
    }
}
