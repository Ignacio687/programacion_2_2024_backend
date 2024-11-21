package ar.edu.um.programacion2.jh.web.rest;

import static ar.edu.um.programacion2.jh.domain.DeviceAsserts.*;
import static ar.edu.um.programacion2.jh.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final Long DEFAULT_SUPPLIER_FOREIGN_ID = 1L;
    private static final Long UPDATED_SUPPLIER_FOREIGN_ID = 2L;

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_BASE_PRICE = 1D;
    private static final Double UPDATED_BASE_PRICE = 2D;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceRepository deviceRepositoryMock;

    @Mock
    private DeviceService deviceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    private Device insertedDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity() {
        return new Device()
            .supplierForeignId(DEFAULT_SUPPLIER_FOREIGN_ID)
            .supplier(DEFAULT_SUPPLIER)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .basePrice(DEFAULT_BASE_PRICE)
            .currency(DEFAULT_CURRENCY)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity() {
        return new Device()
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .supplier(UPDATED_SUPPLIER)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .currency(UPDATED_CURRENCY)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        device = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDevice != null) {
            deviceRepository.delete(insertedDevice);
            insertedDevice = null;
        }
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Device
        var returnedDevice = om.readValue(
            restDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Device.class
        );

        // Validate the Device in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDeviceUpdatableFieldsEquals(returnedDevice, getPersistedDevice(returnedDevice));

        insertedDevice = returnedDevice;
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSupplierForeignIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setSupplierForeignId(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSupplierIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setSupplier(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setCode(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBasePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setBasePrice(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierForeignId").value(hasItem(DEFAULT_SUPPLIER_FOREIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].basePrice").value(hasItem(DEFAULT_BASE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDevicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(deviceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deviceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDevicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deviceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(deviceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.supplierForeignId").value(DEFAULT_SUPPLIER_FOREIGN_ID.intValue()))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.basePrice").value(DEFAULT_BASE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .supplier(UPDATED_SUPPLIER)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .currency(UPDATED_CURRENCY)
            .active(UPDATED_ACTIVE);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDevice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeviceToMatchAllProperties(updatedDevice);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL_ID, device.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(device)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .supplier(UPDATED_SUPPLIER)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .basePrice(UPDATED_BASE_PRICE)
            .currency(UPDATED_CURRENCY)
            .active(UPDATED_ACTIVE);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDevice, device), getPersistedDevice(device));
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .supplier(UPDATED_SUPPLIER)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .basePrice(UPDATED_BASE_PRICE)
            .currency(UPDATED_CURRENCY)
            .active(UPDATED_ACTIVE);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceUpdatableFieldsEquals(partialUpdatedDevice, getPersistedDevice(partialUpdatedDevice));
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, device.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(device)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deviceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Device getPersistedDevice(Device device) {
        return deviceRepository.findById(device.getId()).orElseThrow();
    }

    protected void assertPersistedDeviceToMatchAllProperties(Device expectedDevice) {
        assertDeviceAllPropertiesEquals(expectedDevice, getPersistedDevice(expectedDevice));
    }

    protected void assertPersistedDeviceToMatchUpdatableProperties(Device expectedDevice) {
        assertDeviceAllUpdatablePropertiesEquals(expectedDevice, getPersistedDevice(expectedDevice));
    }
}
