package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.Customization;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.jh.domain.Customization}.
 */
public interface CustomizationService {
    /**
     * Save a customization.
     *
     * @param customization the entity to save.
     * @return the persisted entity.
     */
    Customization save(Customization customization);

    /**
     * Updates a customization.
     *
     * @param customization the entity to update.
     * @return the persisted entity.
     */
    Customization update(Customization customization);

    /**
     * Partially updates a customization.
     *
     * @param customization the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Customization> partialUpdate(Customization customization);

    /**
     * Get all the customizations.
     *
     * @return the list of entities.
     */
    List<Customization> findAll();

    /**
     * Get the "id" customization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Customization> findOne(Long id);

    /**
     * Delete the "id" customization.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
