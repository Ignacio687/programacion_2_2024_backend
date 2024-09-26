package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SaleItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {}
