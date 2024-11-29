package ar.edu.um.programacion2.jh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.domain.Sale;
import ar.edu.um.programacion2.jh.domain.User;
import java.time.Instant;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@ImportAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class, LiquibaseProperties.class })
@WithMockUser(username = "testuser")
public class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void findByUserIsCurrentUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setCreatedBy("system");
        userRepository.saveAndFlush(user);

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setFinalPrice(100.0);
        sale.setSaleDate(Instant.now());
        sale.setDevicePrice(50.5);
        saleRepository.saveAndFlush(sale);

        List<Sale> foundSales = saleRepository.findByUserIsCurrentUser();
        assertThat(foundSales).isNotEmpty();
        assertThat(foundSales.get(0).getUser().getLogin()).isEqualTo("testuser");
    }

    @Test
    @Transactional
    public void findByUserIsCurrentUserPageable() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setCreatedBy("system");
        userRepository.saveAndFlush(user);

        Sale sale1 = new Sale();
        sale1.setUser(user);
        sale1.setFinalPrice(100.5);
        sale1.setSaleDate(Instant.now());
        sale1.setDevicePrice(50.0);
        saleRepository.saveAndFlush(sale1);

        Sale sale2 = new Sale();
        sale2.setUser(user);
        sale2.setFinalPrice(200.8);
        sale2.setSaleDate(Instant.now());
        sale2.setDevicePrice(100.2);
        saleRepository.saveAndFlush(sale2);

        Page<Sale> foundSales = saleRepository.findByUserIsCurrentUser(PageRequest.of(0, 1));
        assertThat(foundSales).isNotEmpty();
        assertThat(foundSales.getContent().get(0).getUser().getLogin()).isEqualTo("testuser");
        assertThat(foundSales.getContent().get(0).getFinalPrice()).isEqualTo(100.5);
        assertThat(foundSales.getNumberOfElements()).isEqualTo(1);
        assertThat(foundSales.getTotalElements()).isEqualTo(2);
        Page<Sale> foundSales1 = saleRepository.findByUserIsCurrentUser(PageRequest.of(1, 1));
        assertThat(foundSales1).isNotEmpty();
        assertThat(foundSales1.getContent().get(0).getUser().getLogin()).isEqualTo("testuser");
        assertThat(foundSales1.getContent().get(0).getFinalPrice()).isEqualTo(200.8);
        assertThat(foundSales.getNumberOfElements()).isEqualTo(1);
        assertThat(foundSales1.getTotalElements()).isEqualTo(2);
    }
}
