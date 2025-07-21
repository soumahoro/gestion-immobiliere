package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.AppartementAsserts.*;
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
import skc.immo.domain.Appartement;
import skc.immo.repository.AppartementRepository;

/**
 * Integration tests for the {@link AppartementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppartementResourceIT {

    private static final String DEFAULT_IDAPP = "AAAAAAAAAA";
    private static final String UPDATED_IDAPP = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOYER = 1;
    private static final Integer UPDATED_LOYER = 2;

    private static final Integer DEFAULT_NBREPIECES = 1;
    private static final Integer UPDATED_NBREPIECES = 2;

    private static final Integer DEFAULT_TAUX = 1;
    private static final Integer UPDATED_TAUX = 2;

    private static final String ENTITY_API_URL = "/api/appartements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppartementRepository appartementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppartementMockMvc;

    private Appartement appartement;

    private Appartement insertedAppartement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appartement createEntity() {
        return new Appartement()
            .idapp(DEFAULT_IDAPP)
            .libelle(DEFAULT_LIBELLE)
            .loyer(DEFAULT_LOYER)
            .nbrepieces(DEFAULT_NBREPIECES)
            .taux(DEFAULT_TAUX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appartement createUpdatedEntity() {
        return new Appartement()
            .idapp(UPDATED_IDAPP)
            .libelle(UPDATED_LIBELLE)
            .loyer(UPDATED_LOYER)
            .nbrepieces(UPDATED_NBREPIECES)
            .taux(UPDATED_TAUX);
    }

    @BeforeEach
    void initTest() {
        appartement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAppartement != null) {
            appartementRepository.delete(insertedAppartement);
            insertedAppartement = null;
        }
    }

    @Test
    @Transactional
    void createAppartement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Appartement
        var returnedAppartement = om.readValue(
            restAppartementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appartement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Appartement.class
        );

        // Validate the Appartement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppartementUpdatableFieldsEquals(returnedAppartement, getPersistedAppartement(returnedAppartement));

        insertedAppartement = returnedAppartement;
    }

    @Test
    @Transactional
    void createAppartementWithExistingId() throws Exception {
        // Create the Appartement with an existing ID
        appartement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppartementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appartement)))
            .andExpect(status().isBadRequest());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdappIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appartement.setIdapp(null);

        // Create the Appartement, which fails.

        restAppartementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appartement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppartements() throws Exception {
        // Initialize the database
        insertedAppartement = appartementRepository.saveAndFlush(appartement);

        // Get all the appartementList
        restAppartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appartement.getId().intValue())))
            .andExpect(jsonPath("$.[*].idapp").value(hasItem(DEFAULT_IDAPP)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].loyer").value(hasItem(DEFAULT_LOYER)))
            .andExpect(jsonPath("$.[*].nbrepieces").value(hasItem(DEFAULT_NBREPIECES)))
            .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX)));
    }

    @Test
    @Transactional
    void getAppartement() throws Exception {
        // Initialize the database
        insertedAppartement = appartementRepository.saveAndFlush(appartement);

        // Get the appartement
        restAppartementMockMvc
            .perform(get(ENTITY_API_URL_ID, appartement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appartement.getId().intValue()))
            .andExpect(jsonPath("$.idapp").value(DEFAULT_IDAPP))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.loyer").value(DEFAULT_LOYER))
            .andExpect(jsonPath("$.nbrepieces").value(DEFAULT_NBREPIECES))
            .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX));
    }

    @Test
    @Transactional
    void getNonExistingAppartement() throws Exception {
        // Get the appartement
        restAppartementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppartement() throws Exception {
        // Initialize the database
        insertedAppartement = appartementRepository.saveAndFlush(appartement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appartement
        Appartement updatedAppartement = appartementRepository.findById(appartement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppartement are not directly saved in db
        em.detach(updatedAppartement);
        updatedAppartement
            .idapp(UPDATED_IDAPP)
            .libelle(UPDATED_LIBELLE)
            .loyer(UPDATED_LOYER)
            .nbrepieces(UPDATED_NBREPIECES)
            .taux(UPDATED_TAUX);

        restAppartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppartement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppartement))
            )
            .andExpect(status().isOk());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppartementToMatchAllProperties(updatedAppartement);
    }

    @Test
    @Transactional
    void putNonExistingAppartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appartement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appartement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appartement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appartement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appartement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appartement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppartementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appartement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppartementWithPatch() throws Exception {
        // Initialize the database
        insertedAppartement = appartementRepository.saveAndFlush(appartement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appartement using partial update
        Appartement partialUpdatedAppartement = new Appartement();
        partialUpdatedAppartement.setId(appartement.getId());

        partialUpdatedAppartement.nbrepieces(UPDATED_NBREPIECES).taux(UPDATED_TAUX);

        restAppartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppartement))
            )
            .andExpect(status().isOk());

        // Validate the Appartement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppartementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppartement, appartement),
            getPersistedAppartement(appartement)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppartementWithPatch() throws Exception {
        // Initialize the database
        insertedAppartement = appartementRepository.saveAndFlush(appartement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appartement using partial update
        Appartement partialUpdatedAppartement = new Appartement();
        partialUpdatedAppartement.setId(appartement.getId());

        partialUpdatedAppartement
            .idapp(UPDATED_IDAPP)
            .libelle(UPDATED_LIBELLE)
            .loyer(UPDATED_LOYER)
            .nbrepieces(UPDATED_NBREPIECES)
            .taux(UPDATED_TAUX);

        restAppartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppartement))
            )
            .andExpect(status().isOk());

        // Validate the Appartement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppartementUpdatableFieldsEquals(partialUpdatedAppartement, getPersistedAppartement(partialUpdatedAppartement));
    }

    @Test
    @Transactional
    void patchNonExistingAppartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appartement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appartement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appartement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appartement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appartement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppartementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appartement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appartement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppartement() throws Exception {
        // Initialize the database
        insertedAppartement = appartementRepository.saveAndFlush(appartement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appartement
        restAppartementMockMvc
            .perform(delete(ENTITY_API_URL_ID, appartement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appartementRepository.count();
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

    protected Appartement getPersistedAppartement(Appartement appartement) {
        return appartementRepository.findById(appartement.getId()).orElseThrow();
    }

    protected void assertPersistedAppartementToMatchAllProperties(Appartement expectedAppartement) {
        assertAppartementAllPropertiesEquals(expectedAppartement, getPersistedAppartement(expectedAppartement));
    }

    protected void assertPersistedAppartementToMatchUpdatableProperties(Appartement expectedAppartement) {
        assertAppartementAllUpdatablePropertiesEquals(expectedAppartement, getPersistedAppartement(expectedAppartement));
    }
}
