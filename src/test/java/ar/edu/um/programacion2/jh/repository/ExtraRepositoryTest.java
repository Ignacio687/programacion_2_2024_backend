package ar.edu.um.programacion2.jh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.domain.Extra;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class, LiquibaseProperties.class })
@DataJpaTest
public class ExtraRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExtraRepository extraRepository;

    @Test
    @Transactional
    public void testFindBySupplierForeignId() {
        Extra extra = new Extra();
        extra.setSupplierForeignId(123L);
        extra.setName("Test Extra");
        extra.setFreePrice((0.00));
        extra.setPrice((50.00));
        entityManager.persistAndFlush(extra);

        Optional<Extra> found = extraRepository.findBySupplierForeignId(123L);
        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getSupplierForeignId()).isEqualTo(123L);
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NotFound() {
        Optional<Extra> found = extraRepository.findBySupplierForeignId(999L);
        assertThat(found).isNotPresent();
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NullId() {
        Optional<Extra> found = extraRepository.findBySupplierForeignId(null);
        assertThat(found).isNotPresent();
    }
}
