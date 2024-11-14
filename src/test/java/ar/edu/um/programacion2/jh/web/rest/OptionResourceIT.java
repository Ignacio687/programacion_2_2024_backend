package ar.edu.um.programacion2.jh.web.rest;

import static ar.edu.um.programacion2.jh.domain.OptionAsserts.*;
import static ar.edu.um.programacion2.jh.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.programacion2.jh.IntegrationTest;
import ar.edu.um.programacion2.jh.domain.Option;
import ar.edu.um.programacion2.jh.repository.OptionRepository;
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
 * Integration tests for the {@link OptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionResourceIT {

    private static final Long DEFAULT_SUPPLIER_FOREIGN_ID = 1L;
    private static final Long UPDATED_SUPPLIER_FOREIGN_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_ADDITIONAL_PRICE = 1D;
    private static final Double UPDATED_ADDITIONAL_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionMockMvc;

    private Option option;

    private Option insertedOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity() {
        return new Option()
            .supplierForeignId(DEFAULT_SUPPLIER_FOREIGN_ID)
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .additionalPrice(DEFAULT_ADDITIONAL_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity() {
        return new Option()
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .additionalPrice(UPDATED_ADDITIONAL_PRICE);
    }

    @BeforeEach
    public void initTest() {
        option = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOption != null) {
            optionRepository.delete(insertedOption);
            insertedOption = null;
        }
    }

    @Test
    @Transactional
    void createOption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Option
        var returnedOption = om.readValue(
            restOptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Option.class
        );

        // Validate the Option in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOptionUpdatableFieldsEquals(returnedOption, getPersistedOption(returnedOption));

        insertedOption = returnedOption;
    }

    @Test
    @Transactional
    void createOptionWithExistingId() throws Exception {
        // Create the Option with an existing ID
        option.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSupplierForeignIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        option.setSupplierForeignId(null);

        // Create the Option, which fails.

        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        option.setCode(null);

        // Create the Option, which fails.

        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdditionalPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        option.setAdditionalPrice(null);

        // Create the Option, which fails.

        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOptions() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].supplierForeignId").value(hasItem(DEFAULT_SUPPLIER_FOREIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].additionalPrice").value(hasItem(DEFAULT_ADDITIONAL_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getOption() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.supplierForeignId").value(DEFAULT_SUPPLIER_FOREIGN_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.additionalPrice").value(DEFAULT_ADDITIONAL_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOption() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOption are not directly saved in db
        em.detach(updatedOption);
        updatedOption
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .additionalPrice(UPDATED_ADDITIONAL_PRICE);

        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOptionToMatchAllProperties(updatedOption);
    }

    @Test
    @Transactional
    void putNonExistingOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(put(ENTITY_API_URL_ID, option.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(option)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID).name(UPDATED_NAME).additionalPrice(UPDATED_ADDITIONAL_PRICE);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOption, option), getPersistedOption(option));
    }

    @Test
    @Transactional
    void fullUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption
            .supplierForeignId(UPDATED_SUPPLIER_FOREIGN_ID)
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .additionalPrice(UPDATED_ADDITIONAL_PRICE);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionUpdatableFieldsEquals(partialUpdatedOption, getPersistedOption(partialUpdatedOption));
    }

    @Test
    @Transactional
    void patchNonExistingOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, option.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(option))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(option)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOption() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the option
        restOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, option.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return optionRepository.count();
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

    protected Option getPersistedOption(Option option) {
        return optionRepository.findById(option.getId()).orElseThrow();
    }

    protected void assertPersistedOptionToMatchAllProperties(Option expectedOption) {
        assertOptionAllPropertiesEquals(expectedOption, getPersistedOption(expectedOption));
    }

    protected void assertPersistedOptionToMatchUpdatableProperties(Option expectedOption) {
        assertOptionAllUpdatablePropertiesEquals(expectedOption, getPersistedOption(expectedOption));
    }
}
