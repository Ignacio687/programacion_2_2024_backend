package ar.edu.um.programacion2.jh.service;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ar.edu.um.programacion2.jh.domain.SaleItem}.
 */
public interface SaleItemService {
    /**
     * Save a saleItem.
     *
     * @param saleItem the entity to save.
     * @return the persisted entity.
     */
    SaleItem save(SaleItem saleItem);

    /**
     * Updates a saleItem.
     *
     * @param saleItem the entity to update.
     * @return the persisted entity.
     */
    SaleItem update(SaleItem saleItem);

    /**
     * Partially updates a saleItem.
     *
     * @param saleItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SaleItem> partialUpdate(SaleItem saleItem);

    /**
     * Get all the saleItems.
     *
     * @return the list of entities.
     */
    List<SaleItem> findAll();

    /**
     * Get the "id" saleItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SaleItem> findOne(Long id);

    /**
     * Delete the "id" saleItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
