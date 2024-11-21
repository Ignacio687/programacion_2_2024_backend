package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.Extra;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.jh.domain.Extra}.
 */
public interface ExtraService {
    /**
     * Save a extra.
     *
     * @param extra the entity to save.
     * @return the persisted entity.
     */
    Extra save(Extra extra);

    /**
     * Updates a extra.
     *
     * @param extra the entity to update.
     * @return the persisted entity.
     */
    Extra update(Extra extra);

    /**
     * Partially updates a extra.
     *
     * @param extra the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Extra> partialUpdate(Extra extra);

    /**
     * Get all the extras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Extra> findAll(Pageable pageable);

    /**
     * Get the "id" extra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Extra> findOne(Long id);

    /**
     * Delete the "id" extra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
