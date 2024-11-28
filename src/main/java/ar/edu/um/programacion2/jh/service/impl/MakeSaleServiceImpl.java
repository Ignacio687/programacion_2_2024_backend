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
        if (!verifyValidSale(saleRequestDTO)) {
            throw new InvalidSaleRequestException("Invalid sale request");
        }
        saleRequestDTO.setDeviceIdToExternalId(this.deviceRepository, this.optionRepository, this.extraRepository);
        //        CompleteSaleDTO externalSale = this.saleClient.createSale(saleRequestDTO);

        CompleteSaleDTO externalSale = new CompleteSaleDTO();
        externalSale.setSaleId(3L);

        Optional<User> currentUser = this.userService.getUserWithAuthorities();
        if (!currentUser.isPresent()) {
            throw new InvalidSaleRequestException("User not found");
        }
        Sale sale = SaleRequestDTO.fromSaleRequestDTO(saleRequestDTO, this.deviceRepository, externalSale.getSaleId(), currentUser.get());
        Set<SaleItem> saleItemSet = sale.getSaleItems();
        sale.setSaleItems(null);
        Sale savedSale = this.saleService.save(sale);
        for (SaleItem saleItem : saleItemSet) {
            saleItem.setSale(savedSale);
            this.saleItemService.save(saleItem);
        }
        savedSale.setSaleItems(saleItemSet);
        return SaleDTO.toSaleDTO(savedSale);
    }

    @Override
    public Page<SaleListDTO> findAll(Pageable pageable, boolean local) {
        return null;
    }

    @Override
    public Optional<CompleteSaleDTO> findOne(Long id, boolean local) {
        return Optional.empty();
    }

    @Override
    public Boolean verifyValidSale(SaleRequestDTO saleRequestDTO) {
        Device device = deviceRepository.findById(saleRequestDTO.getDeviceId()).orElse(null);
        if (device == null) {
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
                return false;
            }
            totalPrice += localOption.getAdditionalPrice();
        }
        double freePrice = totalPrice;
        for (SaleItemDTO extra : saleRequestDTO.getExtras()) {
            Extra localExtra = device.getExtras().stream().filter(ext -> ext.getId().equals(extra.getObjectId())).findFirst().orElse(null);
            if (localExtra == null || !extra.getPrice().equals(localExtra.getPrice())) {
                return false;
            }
            if (localExtra.getFreePrice() >= freePrice || localExtra.getFreePrice() == -1) {
                totalPrice += localExtra.getPrice();
            }
        }
        return totalPrice == saleRequestDTO.getFinalPrice();
    }
}
