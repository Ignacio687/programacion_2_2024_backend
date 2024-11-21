package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
import ar.edu.um.programacion2.jh.service.OptionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.Option}.
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService {

    private static final Logger LOG = LoggerFactory.getLogger(OptionServiceImpl.class);

    private final OptionRepository optionRepository;

    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option save(Option option) {
        LOG.debug("Request to save Option : {}", option);
        return optionRepository.save(option);
    }

    @Override
    public Option update(Option option) {
        LOG.debug("Request to update Option : {}", option);
        return optionRepository.save(option);
    }

    @Override
    public Optional<Option> partialUpdate(Option option) {
        LOG.debug("Request to partially update Option : {}", option);

        return optionRepository
            .findById(option.getId())
            .map(existingOption -> {
                if (option.getSupplierForeignId() != null) {
                    existingOption.setSupplierForeignId(option.getSupplierForeignId());
                }
                if (option.getCode() != null) {
                    existingOption.setCode(option.getCode());
                }
                if (option.getName() != null) {
                    existingOption.setName(option.getName());
                }
                if (option.getDescription() != null) {
                    existingOption.setDescription(option.getDescription());
                }
                if (option.getAdditionalPrice() != null) {
                    existingOption.setAdditionalPrice(option.getAdditionalPrice());
                }

                return existingOption;
            })
            .map(optionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Option> findAll() {
        LOG.debug("Request to get all Options");
        return optionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Option> findOne(Long id) {
        LOG.debug("Request to get Option : {}", id);
        return optionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}
