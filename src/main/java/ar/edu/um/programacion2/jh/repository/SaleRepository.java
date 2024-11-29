package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Sale;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("select sale from Sale sale where sale.user.login = ?#{authentication.name}")
    List<Sale> findByUserIsCurrentUser();

    @Query("select sale from Sale sale where sale.user.login = ?#{authentication.name}")
    Page<Sale> findByUserIsCurrentUser(Pageable pageable);
}
