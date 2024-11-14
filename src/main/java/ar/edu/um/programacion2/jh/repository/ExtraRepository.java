package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Extra;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Extra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraRepository extends JpaRepository<Extra, Long> {
    Optional<Extra> findBySupplierForeignId(Long supplierForeignId);
}
