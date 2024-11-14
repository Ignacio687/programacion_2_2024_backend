package ar.edu.um.programacion2.jh.web.rest;

import static ar.edu.um.programacion2.jh.domain.CharacteristicAsserts.*;
import static ar.edu.um.programacion2.jh.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.Characteristic;
import ar.edu.um.programacion2.jh.repository.CharacteristicRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CharacteristicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharacteristicResourceIT {

    private static final Long DEFAULT_SUPPLIER_FOREIGN_ID = 1L;
    private static final Long UPDATED_SUPPLIER_FOREIGN_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/characteristics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CharacteristicRepository characteristicRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharacteristicMockMvc;

    private Characteristic characteristic;

    private Characteristic insertedCharacteristic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Characteristic createEntity() {
        return new Characteristic().supplierForeignId(DEFAULT_SUPPLIER_FOREIGN_ID).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Characteristic createUpdatedEntity() {
        return new Characteristic().supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        characteristic = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCharacteristic != null) {
            characteristicRepository.delete(insertedCharacteristic);
            insertedCharacteristic = null;
        }
    }

    @Test
    @Transactional
    void createCharacteristic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Characteristic
        var returnedCharacteristic = om.readValue(
            restCharacteristicMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristic)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Characteristic.class
        );

        // Validate the Characteristic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCharacteristicUpdatableFieldsEquals(returnedCharacteristic, getPersistedCharacteristic(returnedCharacteristic));

        insertedCharacteristic = returnedCharacteristic;
    }

    @Test
    @Transactional
    void createCharacteristicWithExistingId() throws Exception {
        // Create the Characteristic with an existing ID
        characteristic.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacteristicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristic)))
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSupplierForeignIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        characteristic.setSupplierForeignId(null);

        // Create the Characteristic, which fails.

        restCharacteristicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristic)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        characteristic.setName(null);

        // Create the Characteristic, which fails.

        restCharacteristicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristic)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCharacteristics() throws Exception {
        // Initialize the database
        insertedCharacteristic = characteristicRepository.saveAndFlush(characteristic);

        // Get all the characteristicList
        restCharacteristicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(characteristic.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierForeignId").value(hasItem(DEFAULT_SUPPLIER_FOREIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCharacteristic() throws Exception {
        // Initialize the database
        insertedCharacteristic = characteristicRepository.saveAndFlush(characteristic);

        // Get the characteristic
        restCharacteristicMockMvc
            .perform(get(ENTITY_API_URL_ID, characteristic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(characteristic.getId().intValue()))
            .andExpect(jsonPath("$.supplierForeignId").value(DEFAULT_SUPPLIER_FOREIGN_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCharacteristic() throws Exception {
        // Get the characteristic
        restCharacteristicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCharacteristic() throws Exception {
        // Initialize the database
        insertedCharacteristic = characteristicRepository.saveAndFlush(characteristic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the characteristic
        Characteristic updatedCharacteristic = characteristicRepository.findById(characteristic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCharacteristic are not directly saved in db
        em.detach(updatedCharacteristic);
        updatedCharacteristic.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCharacteristicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCharacteristic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCharacteristic))
            )
            .andExpect(status().isOk());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCharacteristicToMatchAllProperties(updatedCharacteristic);
    }

    @Test
    @Transactional
    void putNonExistingCharacteristic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristic.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characteristic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(characteristic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacteristic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(characteristic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacteristic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(characteristic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacteristicWithPatch() throws Exception {
        // Initialize the database
        insertedCharacteristic = characteristicRepository.saveAndFlush(characteristic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the characteristic using partial update
        Characteristic partialUpdatedCharacteristic = new Characteristic();
        partialUpdatedCharacteristic.setId(characteristic.getId());

        partialUpdatedCharacteristic.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCharacteristicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacteristic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCharacteristic))
            )
            .andExpect(status().isOk());

        // Validate the Characteristic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCharacteristicUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCharacteristic, characteristic),
            getPersistedCharacteristic(characteristic)
        );
    }

    @Test
    @Transactional
    void fullUpdateCharacteristicWithPatch() throws Exception {
        // Initialize the database
        insertedCharacteristic = characteristicRepository.saveAndFlush(characteristic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the characteristic using partial update
        Characteristic partialUpdatedCharacteristic = new Characteristic();
        partialUpdatedCharacteristic.setId(characteristic.getId());

        partialUpdatedCharacteristic.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCharacteristicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacteristic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCharacteristic))
            )
            .andExpect(status().isOk());

        // Validate the Characteristic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCharacteristicUpdatableFieldsEquals(partialUpdatedCharacteristic, getPersistedCharacteristic(partialUpdatedCharacteristic));
    }

    @Test
    @Transactional
    void patchNonExistingCharacteristic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristic.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characteristic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(characteristic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacteristic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(characteristic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacteristic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        characteristic.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacteristicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(characteristic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Characteristic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacteristic() throws Exception {
        // Initialize the database
        insertedCharacteristic = characteristicRepository.saveAndFlush(characteristic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the characteristic
        restCharacteristicMockMvc
            .perform(delete(ENTITY_API_URL_ID, characteristic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return characteristicRepository.count();
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

    protected Characteristic getPersistedCharacteristic(Characteristic characteristic) {
        return characteristicRepository.findById(characteristic.getId()).orElseThrow();
    }

    protected void assertPersistedCharacteristicToMatchAllProperties(Characteristic expectedCharacteristic) {
        assertCharacteristicAllPropertiesEquals(expectedCharacteristic, getPersistedCharacteristic(expectedCharacteristic));
    }

    protected void assertPersistedCharacteristicToMatchUpdatableProperties(Characteristic expectedCharacteristic) {
        assertCharacteristicAllUpdatablePropertiesEquals(expectedCharacteristic, getPersistedCharacteristic(expectedCharacteristic));
    }
}
