package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.AnneeAsserts.*;
import static skc.immo.web.rest.TestUtil.createUpdateProxyForBean;

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
import skc.immo.IntegrationTest;
import skc.immo.domain.Annee;
import skc.immo.repository.AnneeRepository;

/**
 * Integration tests for the {@link AnneeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnneeResourceIT {

    private static final Integer DEFAULT_AN = 1;
    private static final Integer UPDATED_AN = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/annees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnneeRepository anneeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnneeMockMvc;

    private Annee annee;

    private Annee insertedAnnee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annee createEntity() {
        return new Annee().an(DEFAULT_AN).libelle(DEFAULT_LIBELLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annee createUpdatedEntity() {
        return new Annee().an(UPDATED_AN).libelle(UPDATED_LIBELLE);
    }

    @BeforeEach
    void initTest() {
        annee = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAnnee != null) {
            anneeRepository.delete(insertedAnnee);
            insertedAnnee = null;
        }
    }

    @Test
    @Transactional
    void createAnnee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Annee
        var returnedAnnee = om.readValue(
            restAnneeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annee)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Annee.class
        );

        // Validate the Annee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAnneeUpdatableFieldsEquals(returnedAnnee, getPersistedAnnee(returnedAnnee));

        insertedAnnee = returnedAnnee;
    }

    @Test
    @Transactional
    void createAnneeWithExistingId() throws Exception {
        // Create the Annee with an existing ID
        annee.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnneeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annee)))
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        annee.setAn(null);

        // Create the Annee, which fails.

        restAnneeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnnees() throws Exception {
        // Initialize the database
        insertedAnnee = anneeRepository.saveAndFlush(annee);

        // Get all the anneeList
        restAnneeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annee.getId().intValue())))
            .andExpect(jsonPath("$.[*].an").value(hasItem(DEFAULT_AN)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getAnnee() throws Exception {
        // Initialize the database
        insertedAnnee = anneeRepository.saveAndFlush(annee);

        // Get the annee
        restAnneeMockMvc
            .perform(get(ENTITY_API_URL_ID, annee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annee.getId().intValue()))
            .andExpect(jsonPath("$.an").value(DEFAULT_AN))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingAnnee() throws Exception {
        // Get the annee
        restAnneeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnnee() throws Exception {
        // Initialize the database
        insertedAnnee = anneeRepository.saveAndFlush(annee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annee
        Annee updatedAnnee = anneeRepository.findById(annee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnnee are not directly saved in db
        em.detach(updatedAnnee);
        updatedAnnee.an(UPDATED_AN).libelle(UPDATED_LIBELLE);

        restAnneeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnnee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAnnee))
            )
            .andExpect(status().isOk());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnneeToMatchAllProperties(updatedAnnee);
    }

    @Test
    @Transactional
    void putNonExistingAnnee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeMockMvc
            .perform(put(ENTITY_API_URL_ID, annee.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annee)))
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(annee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnneeWithPatch() throws Exception {
        // Initialize the database
        insertedAnnee = anneeRepository.saveAndFlush(annee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annee using partial update
        Annee partialUpdatedAnnee = new Annee();
        partialUpdatedAnnee.setId(annee.getId());

        partialUpdatedAnnee.an(UPDATED_AN);

        restAnneeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnee))
            )
            .andExpect(status().isOk());

        // Validate the Annee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnneeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAnnee, annee), getPersistedAnnee(annee));
    }

    @Test
    @Transactional
    void fullUpdateAnneeWithPatch() throws Exception {
        // Initialize the database
        insertedAnnee = anneeRepository.saveAndFlush(annee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annee using partial update
        Annee partialUpdatedAnnee = new Annee();
        partialUpdatedAnnee.setId(annee.getId());

        partialUpdatedAnnee.an(UPDATED_AN).libelle(UPDATED_LIBELLE);

        restAnneeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnee))
            )
            .andExpect(status().isOk());

        // Validate the Annee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnneeUpdatableFieldsEquals(partialUpdatedAnnee, getPersistedAnnee(partialUpdatedAnnee));
    }

    @Test
    @Transactional
    void patchNonExistingAnnee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annee.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(annee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(annee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnneeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(annee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Annee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnee() throws Exception {
        // Initialize the database
        insertedAnnee = anneeRepository.saveAndFlush(annee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the annee
        restAnneeMockMvc
            .perform(delete(ENTITY_API_URL_ID, annee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return anneeRepository.count();
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

    protected Annee getPersistedAnnee(Annee annee) {
        return anneeRepository.findById(annee.getId()).orElseThrow();
    }

    protected void assertPersistedAnneeToMatchAllProperties(Annee expectedAnnee) {
        assertAnneeAllPropertiesEquals(expectedAnnee, getPersistedAnnee(expectedAnnee));
    }

    protected void assertPersistedAnneeToMatchUpdatableProperties(Annee expectedAnnee) {
        assertAnneeAllUpdatablePropertiesEquals(expectedAnnee, getPersistedAnnee(expectedAnnee));
    }
}
