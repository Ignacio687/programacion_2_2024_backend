package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.service.ExtraService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.Extra}.
 */
@Service
@Transactional
public class ExtraServiceImpl implements ExtraService {

    private static final Logger LOG = LoggerFactory.getLogger(ExtraServiceImpl.class);

    private final ExtraRepository extraRepository;

    public ExtraServiceImpl(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    @Override
    public Extra save(Extra extra) {
        LOG.debug("Request to save Extra : {}", extra);
        return extraRepository.save(extra);
    }

    @Override
    public Extra update(Extra extra) {
        LOG.debug("Request to update Extra : {}", extra);
        return extraRepository.save(extra);
    }

    @Override
    public Optional<Extra> partialUpdate(Extra extra) {
        LOG.debug("Request to partially update Extra : {}", extra);

        return extraRepository
            .findById(extra.getId())
            .map(existingExtra -> {
                if (extra.getSupplierForeignId() != null) {
                    existingExtra.setSupplierForeignId(extra.getSupplierForeignId());
                }
                if (extra.getName() != null) {
                    existingExtra.setName(extra.getName());
                }
                if (extra.getDescription() != null) {
                    existingExtra.setDescription(extra.getDescription());
                }
                if (extra.getPrice() != null) {
                    existingExtra.setPrice(extra.getPrice());
                }
                if (extra.getFreePrice() != null) {
                    existingExtra.setFreePrice(extra.getFreePrice());
                }

                return existingExtra;
            })
            .map(extraRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Extra> findAll(Pageable pageable) {
        LOG.debug("Request to get all Extras");
        return extraRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Extra> findOne(Long id) {
        LOG.debug("Request to get Extra : {}", id);
        return extraRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Extra : {}", id);
        extraRepository.deleteById(id);
    }
}
