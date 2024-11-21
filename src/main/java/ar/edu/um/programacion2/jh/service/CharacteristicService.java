package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.jh.domain.Characteristic}.
 */
public interface CharacteristicService {
    /**
     * Save a characteristic.
     *
     * @param characteristic the entity to save.
     * @return the persisted entity.
     */
    Characteristic save(Characteristic characteristic);

    /**
     * Updates a characteristic.
     *
     * @param characteristic the entity to update.
     * @return the persisted entity.
     */
    Characteristic update(Characteristic characteristic);

    /**
     * Partially updates a characteristic.
     *
     * @param characteristic the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Characteristic> partialUpdate(Characteristic characteristic);

    /**
     * Get all the characteristics.
     *
     * @return the list of entities.
     */
    List<Characteristic> findAll();

    /**
     * Get the "id" characteristic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Characteristic> findOne(Long id);

    /**
     * Delete the "id" characteristic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
