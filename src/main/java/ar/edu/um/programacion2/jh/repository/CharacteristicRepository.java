package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Characteristic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {}
