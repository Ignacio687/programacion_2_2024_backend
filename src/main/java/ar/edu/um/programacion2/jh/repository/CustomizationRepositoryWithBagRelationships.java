package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Customization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CustomizationRepositoryWithBagRelationships {
    Optional<Customization> fetchBagRelationships(Optional<Customization> customization);

    List<Customization> fetchBagRelationships(List<Customization> customizations);

    Page<Customization> fetchBagRelationships(Page<Customization> customizations);
}
