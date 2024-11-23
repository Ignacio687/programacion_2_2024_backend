package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Customization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customization entity.
 *
 * When extending this class, extend CustomizationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CustomizationRepository extends CustomizationRepositoryWithBagRelationships, JpaRepository<Customization, Long> {
    default Optional<Customization> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Customization> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Customization> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Optional<Customization> findBySupplierForeignId(Long supplierForeignId);
}
