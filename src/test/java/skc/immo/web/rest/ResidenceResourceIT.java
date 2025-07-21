package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.ResidenceAsserts.*;
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
import skc.immo.domain.Residence;
import skc.immo.repository.ResidenceRepository;

/**
 * Integration tests for the {@link ResidenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResidenceResourceIT {

    private static final String DEFAULT_IDRES = "AAAAAAAAAA";
    private static final String UPDATED_IDRES = "BBBBBBBBBB";

    private static final String DEFAULT_ILOT = "AAAAAAAAAA";
    private static final String UPDATED_ILOT = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALISATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCALISATION = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String DEFAULT_QUARTIER = "AAAAAAAAAA";
    private static final String UPDATED_QUARTIER = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/residences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResidenceRepository residenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResidenceMockMvc;

    private Residence residence;

    private Residence insertedResidence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Residence createEntity() {
        return new Residence()
            .idres(DEFAULT_IDRES)
            .ilot(DEFAULT_ILOT)
            .localisation(DEFAULT_LOCALISATION)
            .observation(DEFAULT_OBSERVATION)
            .quartier(DEFAULT_QUARTIER)
            .ville(DEFAULT_VILLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Residence createUpdatedEntity() {
        return new Residence()
            .idres(UPDATED_IDRES)
            .ilot(UPDATED_ILOT)
            .localisation(UPDATED_LOCALISATION)
            .observation(UPDATED_OBSERVATION)
            .quartier(UPDATED_QUARTIER)
            .ville(UPDATED_VILLE);
    }

    @BeforeEach
    void initTest() {
        residence = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedResidence != null) {
            residenceRepository.delete(insertedResidence);
            insertedResidence = null;
        }
    }

    @Test
    @Transactional
    void createResidence() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Residence
        var returnedResidence = om.readValue(
            restResidenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(residence)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Residence.class
        );

        // Validate the Residence in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertResidenceUpdatableFieldsEquals(returnedResidence, getPersistedResidence(returnedResidence));

        insertedResidence = returnedResidence;
    }

    @Test
    @Transactional
    void createResidenceWithExistingId() throws Exception {
        // Create the Residence with an existing ID
        residence.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResidenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(residence)))
            .andExpect(status().isBadRequest());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        residence.setIdres(null);

        // Create the Residence, which fails.

        restResidenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(residence)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResidences() throws Exception {
        // Initialize the database
        insertedResidence = residenceRepository.saveAndFlush(residence);

        // Get all the residenceList
        restResidenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(residence.getId().intValue())))
            .andExpect(jsonPath("$.[*].idres").value(hasItem(DEFAULT_IDRES)))
            .andExpect(jsonPath("$.[*].ilot").value(hasItem(DEFAULT_ILOT)))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].quartier").value(hasItem(DEFAULT_QUARTIER)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)));
    }

    @Test
    @Transactional
    void getResidence() throws Exception {
        // Initialize the database
        insertedResidence = residenceRepository.saveAndFlush(residence);

        // Get the residence
        restResidenceMockMvc
            .perform(get(ENTITY_API_URL_ID, residence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(residence.getId().intValue()))
            .andExpect(jsonPath("$.idres").value(DEFAULT_IDRES))
            .andExpect(jsonPath("$.ilot").value(DEFAULT_ILOT))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.quartier").value(DEFAULT_QUARTIER))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE));
    }

    @Test
    @Transactional
    void getNonExistingResidence() throws Exception {
        // Get the residence
        restResidenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResidence() throws Exception {
        // Initialize the database
        insertedResidence = residenceRepository.saveAndFlush(residence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the residence
        Residence updatedResidence = residenceRepository.findById(residence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResidence are not directly saved in db
        em.detach(updatedResidence);
        updatedResidence
            .idres(UPDATED_IDRES)
            .ilot(UPDATED_ILOT)
            .localisation(UPDATED_LOCALISATION)
            .observation(UPDATED_OBSERVATION)
            .quartier(UPDATED_QUARTIER)
            .ville(UPDATED_VILLE);

        restResidenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResidence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedResidence))
            )
            .andExpect(status().isOk());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResidenceToMatchAllProperties(updatedResidence);
    }

    @Test
    @Transactional
    void putNonExistingResidence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        residence.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResidenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, residence.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(residence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResidence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        residence.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResidenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(residence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResidence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        residence.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResidenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(residence)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResidenceWithPatch() throws Exception {
        // Initialize the database
        insertedResidence = residenceRepository.saveAndFlush(residence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the residence using partial update
        Residence partialUpdatedResidence = new Residence();
        partialUpdatedResidence.setId(residence.getId());

        partialUpdatedResidence
            .idres(UPDATED_IDRES)
            .localisation(UPDATED_LOCALISATION)
            .observation(UPDATED_OBSERVATION)
            .quartier(UPDATED_QUARTIER)
            .ville(UPDATED_VILLE);

        restResidenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResidence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResidence))
            )
            .andExpect(status().isOk());

        // Validate the Residence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResidenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResidence, residence),
            getPersistedResidence(residence)
        );
    }

    @Test
    @Transactional
    void fullUpdateResidenceWithPatch() throws Exception {
        // Initialize the database
        insertedResidence = residenceRepository.saveAndFlush(residence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the residence using partial update
        Residence partialUpdatedResidence = new Residence();
        partialUpdatedResidence.setId(residence.getId());

        partialUpdatedResidence
            .idres(UPDATED_IDRES)
            .ilot(UPDATED_ILOT)
            .localisation(UPDATED_LOCALISATION)
            .observation(UPDATED_OBSERVATION)
            .quartier(UPDATED_QUARTIER)
            .ville(UPDATED_VILLE);

        restResidenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResidence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResidence))
            )
            .andExpect(status().isOk());

        // Validate the Residence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResidenceUpdatableFieldsEquals(partialUpdatedResidence, getPersistedResidence(partialUpdatedResidence));
    }

    @Test
    @Transactional
    void patchNonExistingResidence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        residence.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResidenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, residence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(residence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResidence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        residence.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResidenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(residence))
            )
            .andExpect(status().isBadRequest());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResidence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        residence.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResidenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(residence)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Residence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResidence() throws Exception {
        // Initialize the database
        insertedResidence = residenceRepository.saveAndFlush(residence);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the residence
        restResidenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, residence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return residenceRepository.count();
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

    protected Residence getPersistedResidence(Residence residence) {
        return residenceRepository.findById(residence.getId()).orElseThrow();
    }

    protected void assertPersistedResidenceToMatchAllProperties(Residence expectedResidence) {
        assertResidenceAllPropertiesEquals(expectedResidence, getPersistedResidence(expectedResidence));
    }

    protected void assertPersistedResidenceToMatchUpdatableProperties(Residence expectedResidence) {
        assertResidenceAllUpdatablePropertiesEquals(expectedResidence, getPersistedResidence(expectedResidence));
    }
}
