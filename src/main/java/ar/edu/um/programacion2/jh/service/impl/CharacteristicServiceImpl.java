package ar.edu.um.programacion2.jh.service.impl;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import ar.edu.um.programacion2.jh.repository.CharacteristicRepository;
import ar.edu.um.programacion2.jh.service.CharacteristicService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.programacion2.jh.domain.Characteristic}.
 */
@Service
@Transactional
public class CharacteristicServiceImpl implements CharacteristicService {

    private static final Logger LOG = LoggerFactory.getLogger(CharacteristicServiceImpl.class);

    private final CharacteristicRepository characteristicRepository;

    public CharacteristicServiceImpl(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    @Override
    public Characteristic save(Characteristic characteristic) {
        LOG.debug("Request to save Characteristic : {}", characteristic);
        return characteristicRepository.save(characteristic);
    }

    @Override
    public Characteristic update(Characteristic characteristic) {
        LOG.debug("Request to update Characteristic : {}", characteristic);
        return characteristicRepository.save(characteristic);
    }

    @Override
    public Optional<Characteristic> partialUpdate(Characteristic characteristic) {
        LOG.debug("Request to partially update Characteristic : {}", characteristic);

        return characteristicRepository
            .findById(characteristic.getId())
            .map(existingCharacteristic -> {
                if (characteristic.getSupplierForeignId() != null) {
                    existingCharacteristic.setSupplierForeignId(characteristic.getSupplierForeignId());
                }
                if (characteristic.getName() != null) {
                    existingCharacteristic.setName(characteristic.getName());
                }
                if (characteristic.getDescription() != null) {
                    existingCharacteristic.setDescription(characteristic.getDescription());
                }

                return existingCharacteristic;
            })
            .map(characteristicRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Characteristic> findAll() {
        LOG.debug("Request to get all Characteristics");
        return characteristicRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Characteristic> findOne(Long id) {
        LOG.debug("Request to get Characteristic : {}", id);
        return characteristicRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Characteristic : {}", id);
        characteristicRepository.deleteById(id);
    }
}
