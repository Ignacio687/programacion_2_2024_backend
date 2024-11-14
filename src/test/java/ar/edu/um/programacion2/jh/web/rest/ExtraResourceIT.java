package ar.edu.um.programacion2.jh.web.rest;

import static ar.edu.um.programacion2.jh.domain.ExtraAsserts.*;
import static ar.edu.um.programacion2.jh.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
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
 * Integration tests for the {@link ExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtraResourceIT {

    private static final Long DEFAULT_SUPPLIER_FOREIGN_ID = 1L;
    private static final Long UPDATED_SUPPLIER_FOREIGN_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_FREE_PRICE = 1D;
    private static final Double UPDATED_FREE_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExtraRepository extraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraMockMvc;

    private Extra extra;

    private Extra insertedExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extra createEntity() {
        return new Extra()
            .supplierForeignId(DEFAULT_SUPPLIER_FOREIGN_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .freePrice(DEFAULT_FREE_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extra createUpdatedEntity() {
        return new Extra()
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .freePrice(UPDATED_FREE_PRICE);
    }

    @BeforeEach
    public void initTest() {
        extra = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedExtra != null) {
            extraRepository.delete(insertedExtra);
            insertedExtra = null;
        }
    }

    @Test
    @Transactional
    void createExtra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Extra
        var returnedExtra = om.readValue(
            restExtraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Extra.class
        );

        // Validate the Extra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExtraUpdatableFieldsEquals(returnedExtra, getPersistedExtra(returnedExtra));

        insertedExtra = returnedExtra;
    }

    @Test
    @Transactional
    void createExtraWithExistingId() throws Exception {
        // Create the Extra with an existing ID
        extra.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSupplierForeignIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        extra.setSupplierForeignId(null);

        // Create the Extra, which fails.

        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        extra.setName(null);

        // Create the Extra, which fails.

        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        extra.setPrice(null);

        // Create the Extra, which fails.

        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFreePriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        extra.setFreePrice(null);

        // Create the Extra, which fails.

        restExtraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExtras() throws Exception {
        // Initialize the database
        insertedExtra = extraRepository.saveAndFlush(extra);

        // Get all the extraList
        restExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extra.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierForeignId").value(hasItem(DEFAULT_SUPPLIER_FOREIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].freePrice").value(hasItem(DEFAULT_FREE_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getExtra() throws Exception {
        // Initialize the database
        insertedExtra = extraRepository.saveAndFlush(extra);

        // Get the extra
        restExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, extra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extra.getId().intValue()))
            .andExpect(jsonPath("$.supplierForeignId").value(DEFAULT_SUPPLIER_FOREIGN_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.freePrice").value(DEFAULT_FREE_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingExtra() throws Exception {
        // Get the extra
        restExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtra() throws Exception {
        // Initialize the database
        insertedExtra = extraRepository.saveAndFlush(extra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extra
        Extra updatedExtra = extraRepository.findById(extra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExtra are not directly saved in db
        em.detach(updatedExtra);
        updatedExtra
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .freePrice(UPDATED_FREE_PRICE);

        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtra.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExtraToMatchAllProperties(updatedExtra);
    }

    @Test
    @Transactional
    void putNonExistingExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extra.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(put(ENTITY_API_URL_ID, extra.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtraWithPatch() throws Exception {
        // Initialize the database
        insertedExtra = extraRepository.saveAndFlush(extra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extra using partial update
        Extra partialUpdatedExtra = new Extra();
        partialUpdatedExtra.setId(extra.getId());

        partialUpdatedExtra.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).description(UPDATED_DESCRIPTION).freePrice(UPDATED_FREE_PRICE);

        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtraUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedExtra, extra), getPersistedExtra(extra));
    }

    @Test
    @Transactional
    void fullUpdateExtraWithPatch() throws Exception {
        // Initialize the database
        insertedExtra = extraRepository.saveAndFlush(extra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extra using partial update
        Extra partialUpdatedExtra = new Extra();
        partialUpdatedExtra.setId(extra.getId());

        partialUpdatedExtra
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .freePrice(UPDATED_FREE_PRICE);

        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtra))
            )
            .andExpect(status().isOk());

        // Validate the Extra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtraUpdatableFieldsEquals(partialUpdatedExtra, getPersistedExtra(partialUpdatedExtra));
    }

    @Test
    @Transactional
    void patchNonExistingExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extra.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extra.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extra))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extra.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extra)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtra() throws Exception {
        // Initialize the database
        insertedExtra = extraRepository.saveAndFlush(extra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the extra
        restExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, extra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return extraRepository.count();
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

    protected Extra getPersistedExtra(Extra extra) {
        return extraRepository.findById(extra.getId()).orElseThrow();
    }

    protected void assertPersistedExtraToMatchAllProperties(Extra expectedExtra) {
        assertExtraAllPropertiesEquals(expectedExtra, getPersistedExtra(expectedExtra));
    }

    protected void assertPersistedExtraToMatchUpdatableProperties(Extra expectedExtra) {
        assertExtraAllUpdatablePropertiesEquals(expectedExtra, getPersistedExtra(expectedExtra));
    }
}
