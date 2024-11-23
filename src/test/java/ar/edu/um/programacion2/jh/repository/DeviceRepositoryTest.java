package ar.edu.um.programacion2.jh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.programacion2.jh.domain.Device;
import java.util.List;
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
@TestPropertySource(properties = { "cliente-web.rootUrl=http://localhost" })
@ImportAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class, LiquibaseProperties.class })
@DataJpaTest
public class DeviceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    @Transactional
    public void testFindBySupplierForeignId() {
        Device device = new Device();
        device.setSupplierForeignId(123L);
        device.setName("Test Device");
        device.setSupplier("Test Supplier");
        device.setBasePrice((100.00));
        device.setCode("TEST123");
        entityManager.persistAndFlush(device);

        Optional<Device> found = deviceRepository.findBySupplierForeignId(123L);
        assertThat(found).isPresent();
        assertThat(found.get().getSupplierForeignId()).isEqualTo(123L);
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NotFound() {
        Optional<Device> found = deviceRepository.findBySupplierForeignId(999L);
        assertThat(found).isNotPresent();
    }

    @Test
    @Transactional
    public void findBySupplierForeignId_NullId() {
        Optional<Device> found = deviceRepository.findBySupplierForeignId(null);
        assertThat(found).isNotPresent();
    }

    @Test
    @Transactional
    public void testFindByActiveTrue() {
        Device activeDevice = new Device();
        activeDevice.setSupplierForeignId(124L);
        activeDevice.setName("Active Device");
        activeDevice.setSupplier("Active Supplier");
        activeDevice.setBasePrice(150.00);
        activeDevice.setCode("ACTIVE123");
        activeDevice.setActive(true);
        entityManager.persistAndFlush(activeDevice);

        Device inactiveDevice = new Device();
        inactiveDevice.setSupplierForeignId(125L);
        inactiveDevice.setName("Inactive Device");
        inactiveDevice.setSupplier("Inactive Supplier");
        inactiveDevice.setBasePrice(200.00);
        inactiveDevice.setCode("INACTIVE123");
        inactiveDevice.setActive(false);
        entityManager.persistAndFlush(inactiveDevice);

        List<Device> foundDevices = deviceRepository.findByActiveTrue();
        assertThat(foundDevices).isNotEmpty();
        assertThat(foundDevices).hasSize(1);
        assertThat(foundDevices.get(0).getName()).isEqualTo("Active Device");
    }
}
