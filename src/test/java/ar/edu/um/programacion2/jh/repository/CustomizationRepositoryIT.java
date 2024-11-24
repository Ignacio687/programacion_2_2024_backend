package ar.edu.um.programacion2.jh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.domain.Customization;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class, LiquibaseProperties.class })
@DataJpaTest
public class CustomizationRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomizationRepository customizationRepository;

    @Test
    @Transactional
    public void testFindBySupplierForeignId() {
        Customization customization = new Customization();
        customization.setSupplierForeignId(123L);
        customization.setName("Test Name");
        entityManager.persistAndFlush(customization);

        Optional<Customization> found = customizationRepository.findBySupplierForeignId(123L);
        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getSupplierForeignId()).isEqualTo(123L);
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NotFound() {
        Optional<Customization> found = customizationRepository.findBySupplierForeignId(999L);
        assertThat(found).isNotPresent();
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NullId() {
        Optional<Customization> found = customizationRepository.findBySupplierForeignId(null);
        assertThat(found).isNotPresent();
    }
}
