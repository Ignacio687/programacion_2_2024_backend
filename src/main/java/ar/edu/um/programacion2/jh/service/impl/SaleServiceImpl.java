package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.repository.SaleRepository;
import ar.edu.um.programacion2.jh.service.SaleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.Sale}.
 */
@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private static final Logger LOG = LoggerFactory.getLogger(SaleServiceImpl.class);

    private final SaleRepository saleRepository;

    public SaleServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale save(Sale sale) {
        LOG.debug("Request to save Sale : {}", sale);
        return saleRepository.save(sale);
    }

    @Override
    public Sale update(Sale sale) {
        LOG.debug("Request to update Sale : {}", sale);
        return saleRepository.save(sale);
    }

    @Override
    public Optional<Sale> partialUpdate(Sale sale) {
        LOG.debug("Request to partially update Sale : {}", sale);

        return saleRepository
            .findById(sale.getId())
            .map(existingSale -> {
                if (sale.getSupplierForeignId() != null) {
                    existingSale.setSupplierForeignId(sale.getSupplierForeignId());
                }
                if (sale.getDevicePrice() != null) {
                    existingSale.setDevicePrice(sale.getDevicePrice());
                }
                if (sale.getFinalPrice() != null) {
                    existingSale.setFinalPrice(sale.getFinalPrice());
                }
                if (sale.getSaleDate() != null) {
                    existingSale.setSaleDate(sale.getSaleDate());
                }
                if (sale.getCurrency() != null) {
                    existingSale.setCurrency(sale.getCurrency());
                }
                if (sale.getFinalized() != null) {
                    existingSale.setFinalized(sale.getFinalized());
                }

                return existingSale;
            })
            .map(saleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Sale> findAll(Pageable pageable) {
        LOG.debug("Request to get all Sales");
        return saleRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Sale> findOne(Long id) {
        LOG.debug("Request to get Sale : {}", id);
        return saleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Sale : {}", id);
        saleRepository.deleteById(id);
    }
}
