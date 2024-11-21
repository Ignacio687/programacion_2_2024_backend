package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.repository.SaleItemRepository;
import ar.edu.um.programacion2.jh.service.SaleItemService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.SaleItem}.
 */
@Service
@Transactional
public class SaleItemServiceImpl implements SaleItemService {

    private static final Logger LOG = LoggerFactory.getLogger(SaleItemServiceImpl.class);

    private final SaleItemRepository saleItemRepository;

    public SaleItemServiceImpl(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    @Override
    public SaleItem save(SaleItem saleItem) {
        LOG.debug("Request to save SaleItem : {}", saleItem);
        return saleItemRepository.save(saleItem);
    }

    @Override
    public SaleItem update(SaleItem saleItem) {
        LOG.debug("Request to update SaleItem : {}", saleItem);
        return saleItemRepository.save(saleItem);
    }

    @Override
    public Optional<SaleItem> partialUpdate(SaleItem saleItem) {
        LOG.debug("Request to partially update SaleItem : {}", saleItem);

        return saleItemRepository
            .findById(saleItem.getId())
            .map(existingSaleItem -> {
                if (saleItem.getPrice() != null) {
                    existingSaleItem.setPrice(saleItem.getPrice());
                }

                return existingSaleItem;
            })
            .map(saleItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleItem> findAll() {
        LOG.debug("Request to get all SaleItems");
        return saleItemRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SaleItem> findOne(Long id) {
        LOG.debug("Request to get SaleItem : {}", id);
        return saleItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SaleItem : {}", id);
        saleItemRepository.deleteById(id);
    }
}
