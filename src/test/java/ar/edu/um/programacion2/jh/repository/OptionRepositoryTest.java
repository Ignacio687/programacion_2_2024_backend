package ar.edu.um.programacion2.jh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.domain.Option;
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
public class OptionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @Transactional
    public void testFindBySupplierForeignId() {
        Option option = new Option();
        option.setSupplierForeignId(123L);
        option.setName("Test Option");
        option.setAdditionalPrice(10.00);
        option.setCode("OPT123");
        entityManager.persistAndFlush(option);

        Optional<Option> found = optionRepository.findBySupplierForeignId(123L);
        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getSupplierForeignId()).isEqualTo(123L);
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NotFound() {
        Optional<Option> found = optionRepository.findBySupplierForeignId(999L);
        assertThat(found).isNotPresent();
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NullId() {
        Optional<Option> found = optionRepository.findBySupplierForeignId(null);
        assertThat(found).isNotPresent();
    }
}
