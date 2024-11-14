package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Option;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findBySupplierForeignId(Long supplierForeignId);
}
