package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.domain.Option;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomizationRepository extends JpaRepository<Customization, Long> {
    Optional<Customization> findBySupplierForeignId(Long supplierForeignId);

    Customization findByOptionsContains(Option option);
}
