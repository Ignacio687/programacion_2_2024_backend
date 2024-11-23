package ar.edu.um.programacion2.jh.repository;

import ar.edu.um.programacion2.jh.domain.Customization;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CustomizationRepositoryWithBagRelationshipsImpl implements CustomizationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CUSTOMIZATIONS_PARAMETER = "customizations";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Customization> fetchBagRelationships(Optional<Customization> customization) {
        return customization.map(this::fetchOptions);
    }

    @Override
    public Page<Customization> fetchBagRelationships(Page<Customization> customizations) {
        return new PageImpl<>(
            fetchBagRelationships(customizations.getContent()),
            customizations.getPageable(),
            customizations.getTotalElements()
        );
    }

    @Override
    public List<Customization> fetchBagRelationships(List<Customization> customizations) {
        return Optional.of(customizations).map(this::fetchOptions).orElse(Collections.emptyList());
    }

    Customization fetchOptions(Customization result) {
        return entityManager
            .createQuery(
                "select customization from Customization customization left join fetch customization.options where customization.id = :id",
                Customization.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Customization> fetchOptions(List<Customization> customizations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, customizations.size()).forEach(index -> order.put(customizations.get(index).getId(), index));
        List<Customization> result = entityManager
            .createQuery(
                "select customization from Customization customization left join fetch customization.options where customization in :customizations",
                Customization.class
            )
            .setParameter(CUSTOMIZATIONS_PARAMETER, customizations)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
