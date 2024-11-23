package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Device;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Device entity.
 *
 * When extending this class, extend DeviceRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DeviceRepository extends DeviceRepositoryWithBagRelationships, JpaRepository<Device, Long> {
    default Optional<Device> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Device> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Device> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    Optional<Device> findBySupplierForeignId(Long supplierForeignId);
}
