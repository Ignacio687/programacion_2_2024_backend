package ar.edu.um.programacion2.jh.web.rest;

import static ar.edu.um.programacion2.jh.domain.SaleItemAsserts.*;
import static ar.edu.um.programacion2.jh.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.repository.SaleItemRepository;
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
 * Integration tests for the {@link SaleItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaleItemResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/sale-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaleItemMockMvc;

    private SaleItem saleItem;

    private SaleItem insertedSaleItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaleItem createEntity() {
        return new SaleItem().price(DEFAULT_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaleItem createUpdatedEntity() {
        return new SaleItem().price(UPDATED_PRICE);
    }

    @BeforeEach
    public void initTest() {
        saleItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSaleItem != null) {
            saleItemRepository.delete(insertedSaleItem);
            insertedSaleItem = null;
        }
    }

    @Test
    @Transactional
    void createSaleItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SaleItem
        var returnedSaleItem = om.readValue(
            restSaleItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaleItem.class
        );

        // Validate the SaleItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSaleItemUpdatableFieldsEquals(returnedSaleItem, getPersistedSaleItem(returnedSaleItem));

        insertedSaleItem = returnedSaleItem;
    }

    @Test
    @Transactional
    void createSaleItemWithExistingId() throws Exception {
        // Create the SaleItem with an existing ID
        saleItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleItem)))
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saleItem.setPrice(null);

        // Create the SaleItem, which fails.

        restSaleItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaleItems() throws Exception {
        // Initialize the database
        insertedSaleItem = saleItemRepository.saveAndFlush(saleItem);

        // Get all the saleItemList
        restSaleItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getSaleItem() throws Exception {
        // Initialize the database
        insertedSaleItem = saleItemRepository.saveAndFlush(saleItem);

        // Get the saleItem
        restSaleItemMockMvc
            .perform(get(ENTITY_API_URL_ID, saleItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saleItem.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSaleItem() throws Exception {
        // Get the saleItem
        restSaleItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaleItem() throws Exception {
        // Initialize the database
        insertedSaleItem = saleItemRepository.saveAndFlush(saleItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saleItem
        SaleItem updatedSaleItem = saleItemRepository.findById(saleItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaleItem are not directly saved in db
        em.detach(updatedSaleItem);
        updatedSaleItem.price(UPDATED_PRICE);

        restSaleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSaleItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSaleItem))
            )
            .andExpect(status().isOk());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaleItemToMatchAllProperties(updatedSaleItem);
    }

    @Test
    @Transactional
    void putNonExistingSaleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saleItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saleItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saleItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saleItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saleItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saleItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaleItemWithPatch() throws Exception {
        // Initialize the database
        insertedSaleItem = saleItemRepository.saveAndFlush(saleItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saleItem using partial update
        SaleItem partialUpdatedSaleItem = new SaleItem();
        partialUpdatedSaleItem.setId(saleItem.getId());

        restSaleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaleItem))
            )
            .andExpect(status().isOk());

        // Validate the SaleItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaleItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSaleItem, saleItem), getPersistedSaleItem(saleItem));
    }

    @Test
    @Transactional
    void fullUpdateSaleItemWithPatch() throws Exception {
        // Initialize the database
        insertedSaleItem = saleItemRepository.saveAndFlush(saleItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saleItem using partial update
        SaleItem partialUpdatedSaleItem = new SaleItem();
        partialUpdatedSaleItem.setId(saleItem.getId());

        partialUpdatedSaleItem.price(UPDATED_PRICE);

        restSaleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaleItem))
            )
            .andExpect(status().isOk());

        // Validate the SaleItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaleItemUpdatableFieldsEquals(partialUpdatedSaleItem, getPersistedSaleItem(partialUpdatedSaleItem));
    }

    @Test
    @Transactional
    void patchNonExistingSaleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saleItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saleItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saleItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saleItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saleItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaleItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saleItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaleItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saleItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaleItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaleItem() throws Exception {
        // Initialize the database
        insertedSaleItem = saleItemRepository.saveAndFlush(saleItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saleItem
        restSaleItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, saleItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saleItemRepository.count();
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

    protected SaleItem getPersistedSaleItem(SaleItem saleItem) {
        return saleItemRepository.findById(saleItem.getId()).orElseThrow();
    }

    protected void assertPersistedSaleItemToMatchAllProperties(SaleItem expectedSaleItem) {
        assertSaleItemAllPropertiesEquals(expectedSaleItem, getPersistedSaleItem(expectedSaleItem));
    }

    protected void assertPersistedSaleItemToMatchUpdatableProperties(SaleItem expectedSaleItem) {
        assertSaleItemAllUpdatablePropertiesEquals(expectedSaleItem, getPersistedSaleItem(expectedSaleItem));
    }
}
