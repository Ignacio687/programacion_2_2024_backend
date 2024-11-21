package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
import ar.edu.um.programacion2.jh.service.CustomizationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.Customization}.
 */
@Service
@Transactional
public class CustomizationServiceImpl implements CustomizationService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomizationServiceImpl.class);

    private final CustomizationRepository customizationRepository;

    public CustomizationServiceImpl(CustomizationRepository customizationRepository) {
        this.customizationRepository = customizationRepository;
    }

    @Override
    public Customization save(Customization customization) {
        LOG.debug("Request to save Customization : {}", customization);
        return customizationRepository.save(customization);
    }

    @Override
    public Customization update(Customization customization) {
        LOG.debug("Request to update Customization : {}", customization);
        return customizationRepository.save(customization);
    }

    @Override
    public Optional<Customization> partialUpdate(Customization customization) {
        LOG.debug("Request to partially update Customization : {}", customization);

        return customizationRepository
            .findById(customization.getId())
            .map(existingCustomization -> {
                if (customization.getSupplierForeignId() != null) {
                    existingCustomization.setSupplierForeignId(customization.getSupplierForeignId());
                }
                if (customization.getName() != null) {
                    existingCustomization.setName(customization.getName());
                }
                if (customization.getDescription() != null) {
                    existingCustomization.setDescription(customization.getDescription());
                }

                return existingCustomization;
            })
            .map(customizationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customization> findAll() {
        LOG.debug("Request to get all Customizations");
        return customizationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customization> findOne(Long id) {
        LOG.debug("Request to get Customization : {}", id);
        return customizationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Customization : {}", id);
        customizationRepository.deleteById(id);
    }
}
