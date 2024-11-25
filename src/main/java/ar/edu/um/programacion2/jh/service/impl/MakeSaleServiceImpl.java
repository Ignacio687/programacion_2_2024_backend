package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import ar.edu.um.programacion2.jh.repository.SaleRepository;
import ar.edu.um.programacion2.jh.service.MakeSaleService;
import ar.edu.um.programacion2.jh.service.SaleService;
import ar.edu.um.programacion2.jh.service.dto.CompleteSaleDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleListDTO;
import ar.edu.um.programacion2.jh.service.dto.SaleRequestDTO;
import ar.edu.um.programacion2.jh.service.errors.InvalidSaleRequestException;
import java.util.Optional;
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

    public MakeSaleServiceImpl(
        SaleService saleService,
        DeviceRepository deviceRepository,
        OptionRepository optionRepository,
        ExtraRepository extraRepository,
        SaleRepository saleRepository
    ) {
        this.saleService = saleService;
        this.deviceRepository = deviceRepository;
        this.optionRepository = optionRepository;
        this.extraRepository = extraRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale save(SaleRequestDTO saleRequestDTO) throws InvalidSaleRequestException {
        return null;
    }

    @Override
    public Page<SaleListDTO> findAll(Pageable pageable, boolean local) {
        return null;
        // Recordar que solo hay que traer las sales que correspondan al usuario logueado.
        // Recordar hacer pequeño cambio en el controlador de dispositivos para que solo traiga los activos pasando un
        // parametro en el request.
    }

    @Override
    public Optional<CompleteSaleDTO> findOne(Long id, boolean local) {
        return Optional.empty();
    }

    @Override
    public Boolean verifyValidSale(SaleRequestDTO saleRequestDTO) {
        return null;
    }
}
