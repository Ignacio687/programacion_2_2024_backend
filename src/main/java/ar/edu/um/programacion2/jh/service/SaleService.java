package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.Sale;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.jh.domain.Sale}.
 */
public interface SaleService {
    /**
     * Save a sale.
     *
     * @param sale the entity to save.
     * @return the persisted entity.
     */
    Sale save(Sale sale);

    /**
     * Updates a sale.
     *
     * @param sale the entity to update.
     * @return the persisted entity.
     */
    Sale update(Sale sale);

    /**
     * Partially updates a sale.
     *
     * @param sale the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sale> partialUpdate(Sale sale);

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sale> findAll(Pageable pageable);

    /**
     * Get the "id" sale.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sale> findOne(Long id);

    /**
     * Delete the "id" sale.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
