package ar.edu.um.programacion2.jh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.domain.Customization;
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
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(properties = { "cliente-web.rootUrl=http://localhost" })
@ImportAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class, LiquibaseProperties.class })
@DataJpaTest
public class CustomizationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomizationRepository customizationRepository;

    @Test
    public void testFindBySupplierForeignId() {
        Customization customization = new Customization();
        customization.setSupplierForeignId(123L);
        customization.setName("Test Name");
        entityManager.persistAndFlush(customization);

        Optional<Customization> found = customizationRepository.findBySupplierForeignId(123L);
        assertThat(found).isPresent();
        assertThat(found.get().getSupplierForeignId()).isEqualTo(123L);
    }

    @Test
    public void testFindByOptionsContains() {
        Customization customization = new Customization();
        customization.setSupplierForeignId(123L);
        customization.setName("Test Name");
        Option option = new Option();
        option.setSupplierForeignId(1L);
        option.setCode("OptionCode");
        option.setAdditionalPrice(10.0);
        customization.addOptions(option);
        entityManager.persist(option);
        entityManager.persistAndFlush(customization);

        Customization found = customizationRepository.findByOptionsContains(option);
        found.getOptions();
        assertThat(found).isNotNull();
        assertThat(found.getOptions()).contains(option);
    }
}
