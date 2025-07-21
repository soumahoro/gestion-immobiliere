package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.ProprietaireAsserts.*;
import static skc.immo.web.rest.TestUtil.createUpdateProxyForBean;
import static skc.immo.web.rest.TestUtil.sameInstant;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import skc.immo.domain.Proprietaire;
import skc.immo.repository.ProprietaireRepository;

/**
 * Integration tests for the {@link ProprietaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProprietaireResourceIT {

    private static final String DEFAULT_IDPRO = "AAAAAAAAAA";
    private static final String UPDATED_IDPRO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENCE = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proprietaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProprietaireMockMvc;

    private Proprietaire proprietaire;

    private Proprietaire insertedProprietaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proprietaire createEntity() {
        return new Proprietaire().idpro(DEFAULT_IDPRO).date(DEFAULT_DATE).nom(DEFAULT_NOM).residence(DEFAULT_RESIDENCE).tel(DEFAULT_TEL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proprietaire createUpdatedEntity() {
        return new Proprietaire().idpro(UPDATED_IDPRO).date(UPDATED_DATE).nom(UPDATED_NOM).residence(UPDATED_RESIDENCE).tel(UPDATED_TEL);
    }

    @BeforeEach
    void initTest() {
        proprietaire = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProprietaire != null) {
            proprietaireRepository.delete(insertedProprietaire);
            insertedProprietaire = null;
        }
    }

    @Test
    @Transactional
    void createProprietaire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Proprietaire
        var returnedProprietaire = om.readValue(
            restProprietaireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proprietaire)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Proprietaire.class
        );

        // Validate the Proprietaire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProprietaireUpdatableFieldsEquals(returnedProprietaire, getPersistedProprietaire(returnedProprietaire));

        insertedProprietaire = returnedProprietaire;
    }

    @Test
    @Transactional
    void createProprietaireWithExistingId() throws Exception {
        // Create the Proprietaire with an existing ID
        proprietaire.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProprietaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proprietaire)))
            .andExpect(status().isBadRequest());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdproIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        proprietaire.setIdpro(null);

        // Create the Proprietaire, which fails.

        restProprietaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proprietaire)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProprietaires() throws Exception {
        // Initialize the database
        insertedProprietaire = proprietaireRepository.saveAndFlush(proprietaire);

        // Get all the proprietaireList
        restProprietaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proprietaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].idpro").value(hasItem(DEFAULT_IDPRO)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].residence").value(hasItem(DEFAULT_RESIDENCE)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)));
    }

    @Test
    @Transactional
    void getProprietaire() throws Exception {
        // Initialize the database
        insertedProprietaire = proprietaireRepository.saveAndFlush(proprietaire);

        // Get the proprietaire
        restProprietaireMockMvc
            .perform(get(ENTITY_API_URL_ID, proprietaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proprietaire.getId().intValue()))
            .andExpect(jsonPath("$.idpro").value(DEFAULT_IDPRO))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.residence").value(DEFAULT_RESIDENCE))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL));
    }

    @Test
    @Transactional
    void getNonExistingProprietaire() throws Exception {
        // Get the proprietaire
        restProprietaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProprietaire() throws Exception {
        // Initialize the database
        insertedProprietaire = proprietaireRepository.saveAndFlush(proprietaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proprietaire
        Proprietaire updatedProprietaire = proprietaireRepository.findById(proprietaire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProprietaire are not directly saved in db
        em.detach(updatedProprietaire);
        updatedProprietaire.idpro(UPDATED_IDPRO).date(UPDATED_DATE).nom(UPDATED_NOM).residence(UPDATED_RESIDENCE).tel(UPDATED_TEL);

        restProprietaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProprietaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProprietaire))
            )
            .andExpect(status().isOk());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProprietaireToMatchAllProperties(updatedProprietaire);
    }

    @Test
    @Transactional
    void putNonExistingProprietaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proprietaire.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProprietaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proprietaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proprietaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProprietaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proprietaire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProprietaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(proprietaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProprietaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proprietaire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProprietaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(proprietaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProprietaireWithPatch() throws Exception {
        // Initialize the database
        insertedProprietaire = proprietaireRepository.saveAndFlush(proprietaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proprietaire using partial update
        Proprietaire partialUpdatedProprietaire = new Proprietaire();
        partialUpdatedProprietaire.setId(proprietaire.getId());

        restProprietaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProprietaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProprietaire))
            )
            .andExpect(status().isOk());

        // Validate the Proprietaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProprietaireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProprietaire, proprietaire),
            getPersistedProprietaire(proprietaire)
        );
    }

    @Test
    @Transactional
    void fullUpdateProprietaireWithPatch() throws Exception {
        // Initialize the database
        insertedProprietaire = proprietaireRepository.saveAndFlush(proprietaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the proprietaire using partial update
        Proprietaire partialUpdatedProprietaire = new Proprietaire();
        partialUpdatedProprietaire.setId(proprietaire.getId());

        partialUpdatedProprietaire.idpro(UPDATED_IDPRO).date(UPDATED_DATE).nom(UPDATED_NOM).residence(UPDATED_RESIDENCE).tel(UPDATED_TEL);

        restProprietaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProprietaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProprietaire))
            )
            .andExpect(status().isOk());

        // Validate the Proprietaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProprietaireUpdatableFieldsEquals(partialUpdatedProprietaire, getPersistedProprietaire(partialUpdatedProprietaire));
    }

    @Test
    @Transactional
    void patchNonExistingProprietaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proprietaire.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProprietaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proprietaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proprietaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProprietaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proprietaire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProprietaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(proprietaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProprietaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        proprietaire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProprietaireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(proprietaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proprietaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProprietaire() throws Exception {
        // Initialize the database
        insertedProprietaire = proprietaireRepository.saveAndFlush(proprietaire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the proprietaire
        restProprietaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, proprietaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return proprietaireRepository.count();
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

    protected Proprietaire getPersistedProprietaire(Proprietaire proprietaire) {
        return proprietaireRepository.findById(proprietaire.getId()).orElseThrow();
    }

    protected void assertPersistedProprietaireToMatchAllProperties(Proprietaire expectedProprietaire) {
        assertProprietaireAllPropertiesEquals(expectedProprietaire, getPersistedProprietaire(expectedProprietaire));
    }

    protected void assertPersistedProprietaireToMatchUpdatableProperties(Proprietaire expectedProprietaire) {
        assertProprietaireAllUpdatablePropertiesEquals(expectedProprietaire, getPersistedProprietaire(expectedProprietaire));
    }
}
