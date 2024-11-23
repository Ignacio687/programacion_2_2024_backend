package ar.edu.um.programacion2.jh.web.rest;

import static ar.edu.um.programacion2.jh.domain.CustomizationAsserts.*;
import static ar.edu.um.programacion2.jh.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
import ar.edu.um.programacion2.jh.service.CustomizationService;
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
 * Integration tests for the {@link CustomizationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomizationResourceIT {

    private static final Long DEFAULT_SUPPLIER_FOREIGN_ID = 1L;
    private static final Long UPDATED_SUPPLIER_FOREIGN_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomizationRepository customizationRepository;

    @Mock
    private CustomizationRepository customizationRepositoryMock;

    @Mock
    private CustomizationService customizationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomizationMockMvc;

    private Customization customization;

    private Customization insertedCustomization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customization createEntity() {
        return new Customization().supplierForeignId(DEFAULT_SUPPLIER_FOREIGN_ID).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customization createUpdatedEntity() {
        return new Customization().supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        customization = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomization != null) {
            customizationRepository.delete(insertedCustomization);
            insertedCustomization = null;
        }
    }

    @Test
    @Transactional
    void createCustomization() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Customization
        var returnedCustomization = om.readValue(
            restCustomizationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customization)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Customization.class
        );

        // Validate the Customization in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomizationUpdatableFieldsEquals(returnedCustomization, getPersistedCustomization(returnedCustomization));

        insertedCustomization = returnedCustomization;
    }

    @Test
    @Transactional
    void createCustomizationWithExistingId() throws Exception {
        // Create the Customization with an existing ID
        customization.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customization)))
            .andExpect(status().isBadRequest());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSupplierForeignIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customization.setSupplierForeignId(null);

        // Create the Customization, which fails.

        restCustomizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customization)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customization.setName(null);

        // Create the Customization, which fails.

        restCustomizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customization)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomizations() throws Exception {
        // Initialize the database
        insertedCustomization = customizationRepository.saveAndFlush(customization);

        // Get all the customizationList
        restCustomizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customization.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierForeignId").value(hasItem(DEFAULT_SUPPLIER_FOREIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomizationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(customizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customizationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomizationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customizationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomizationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(customizationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCustomization() throws Exception {
        // Initialize the database
        insertedCustomization = customizationRepository.saveAndFlush(customization);

        // Get the customization
        restCustomizationMockMvc
            .perform(get(ENTITY_API_URL_ID, customization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customization.getId().intValue()))
            .andExpect(jsonPath("$.supplierForeignId").value(DEFAULT_SUPPLIER_FOREIGN_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCustomization() throws Exception {
        // Get the customization
        restCustomizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomization() throws Exception {
        // Initialize the database
        insertedCustomization = customizationRepository.saveAndFlush(customization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customization
        Customization updatedCustomization = customizationRepository.findById(customization.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomization are not directly saved in db
        em.detach(updatedCustomization);
        updatedCustomization.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustomizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomization))
            )
            .andExpect(status().isOk());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomizationToMatchAllProperties(updatedCustomization);
    }

    @Test
    @Transactional
    void putNonExistingCustomization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customization.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customization.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customization.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomizationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customization)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomizationWithPatch() throws Exception {
        // Initialize the database
        insertedCustomization = customizationRepository.saveAndFlush(customization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customization using partial update
        Customization partialUpdatedCustomization = new Customization();
        partialUpdatedCustomization.setId(customization.getId());

        partialUpdatedCustomization.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).description(UPDATED_DESCRIPTION);

        restCustomizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomization))
            )
            .andExpect(status().isOk());

        // Validate the Customization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomizationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomization, customization),
            getPersistedCustomization(customization)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomizationWithPatch() throws Exception {
        // Initialize the database
        insertedCustomization = customizationRepository.saveAndFlush(customization);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customization using partial update
        Customization partialUpdatedCustomization = new Customization();
        partialUpdatedCustomization.setId(customization.getId());

        partialUpdatedCustomization.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCustomizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomization))
            )
            .andExpect(status().isOk());

        // Validate the Customization in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomizationUpdatableFieldsEquals(partialUpdatedCustomization, getPersistedCustomization(partialUpdatedCustomization));
    }

    @Test
    @Transactional
    void patchNonExistingCustomization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customization.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customization.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customization.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomization() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customization.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomizationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customization)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customization in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomization() throws Exception {
        // Initialize the database
        insertedCustomization = customizationRepository.saveAndFlush(customization);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customization
        restCustomizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, customization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customizationRepository.count();
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

    protected Customization getPersistedCustomization(Customization customization) {
        return customizationRepository.findById(customization.getId()).orElseThrow();
    }

    protected void assertPersistedCustomizationToMatchAllProperties(Customization expectedCustomization) {
        assertCustomizationAllPropertiesEquals(expectedCustomization, getPersistedCustomization(expectedCustomization));
    }

    protected void assertPersistedCustomizationToMatchUpdatableProperties(Customization expectedCustomization) {
        assertCustomizationAllUpdatablePropertiesEquals(expectedCustomization, getPersistedCustomization(expectedCustomization));
    }
}
