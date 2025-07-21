package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.MoiAsserts.*;
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
import skc.immo.domain.Moi;
import skc.immo.repository.MoiRepository;

/**
 * Integration tests for the {@link MoiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoiResourceIT {

    private static final Integer DEFAULT_IDMOIS = 1;
    private static final Integer UPDATED_IDMOIS = 2;

    private static final String DEFAULT_MOIS = "AAAAAAAAAA";
    private static final String UPDATED_MOIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mois";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoiRepository moiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoiMockMvc;

    private Moi moi;

    private Moi insertedMoi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moi createEntity() {
        return new Moi().idmois(DEFAULT_IDMOIS).mois(DEFAULT_MOIS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moi createUpdatedEntity() {
        return new Moi().idmois(UPDATED_IDMOIS).mois(UPDATED_MOIS);
    }

    @BeforeEach
    void initTest() {
        moi = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoi != null) {
            moiRepository.delete(insertedMoi);
            insertedMoi = null;
        }
    }

    @Test
    @Transactional
    void createMoi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Moi
        var returnedMoi = om.readValue(
            restMoiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Moi.class
        );

        // Validate the Moi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMoiUpdatableFieldsEquals(returnedMoi, getPersistedMoi(returnedMoi));

        insertedMoi = returnedMoi;
    }

    @Test
    @Transactional
    void createMoiWithExistingId() throws Exception {
        // Create the Moi with an existing ID
        moi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moi)))
            .andExpect(status().isBadRequest());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdmoisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moi.setIdmois(null);

        // Create the Moi, which fails.

        restMoiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMois() throws Exception {
        // Initialize the database
        insertedMoi = moiRepository.saveAndFlush(moi);

        // Get all the moiList
        restMoiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moi.getId().intValue())))
            .andExpect(jsonPath("$.[*].idmois").value(hasItem(DEFAULT_IDMOIS)))
            .andExpect(jsonPath("$.[*].mois").value(hasItem(DEFAULT_MOIS)));
    }

    @Test
    @Transactional
    void getMoi() throws Exception {
        // Initialize the database
        insertedMoi = moiRepository.saveAndFlush(moi);

        // Get the moi
        restMoiMockMvc
            .perform(get(ENTITY_API_URL_ID, moi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moi.getId().intValue()))
            .andExpect(jsonPath("$.idmois").value(DEFAULT_IDMOIS))
            .andExpect(jsonPath("$.mois").value(DEFAULT_MOIS));
    }

    @Test
    @Transactional
    void getNonExistingMoi() throws Exception {
        // Get the moi
        restMoiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoi() throws Exception {
        // Initialize the database
        insertedMoi = moiRepository.saveAndFlush(moi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moi
        Moi updatedMoi = moiRepository.findById(moi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoi are not directly saved in db
        em.detach(updatedMoi);
        updatedMoi.idmois(UPDATED_IDMOIS).mois(UPDATED_MOIS);

        restMoiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMoi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedMoi))
            )
            .andExpect(status().isOk());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoiToMatchAllProperties(updatedMoi);
    }

    @Test
    @Transactional
    void putNonExistingMoi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoiMockMvc
            .perform(put(ENTITY_API_URL_ID, moi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moi)))
            .andExpect(status().isBadRequest());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoiWithPatch() throws Exception {
        // Initialize the database
        insertedMoi = moiRepository.saveAndFlush(moi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moi using partial update
        Moi partialUpdatedMoi = new Moi();
        partialUpdatedMoi.setId(moi.getId());

        partialUpdatedMoi.idmois(UPDATED_IDMOIS);

        restMoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoi))
            )
            .andExpect(status().isOk());

        // Validate the Moi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoiUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMoi, moi), getPersistedMoi(moi));
    }

    @Test
    @Transactional
    void fullUpdateMoiWithPatch() throws Exception {
        // Initialize the database
        insertedMoi = moiRepository.saveAndFlush(moi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moi using partial update
        Moi partialUpdatedMoi = new Moi();
        partialUpdatedMoi.setId(moi.getId());

        partialUpdatedMoi.idmois(UPDATED_IDMOIS).mois(UPDATED_MOIS);

        restMoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoi))
            )
            .andExpect(status().isOk());

        // Validate the Moi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoiUpdatableFieldsEquals(partialUpdatedMoi, getPersistedMoi(partialUpdatedMoi));
    }

    @Test
    @Transactional
    void patchNonExistingMoi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoiMockMvc
            .perform(patch(ENTITY_API_URL_ID, moi.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moi)))
            .andExpect(status().isBadRequest());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moi))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoi() throws Exception {
        // Initialize the database
        insertedMoi = moiRepository.saveAndFlush(moi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moi
        restMoiMockMvc.perform(delete(ENTITY_API_URL_ID, moi.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moiRepository.count();
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

    protected Moi getPersistedMoi(Moi moi) {
        return moiRepository.findById(moi.getId()).orElseThrow();
    }

    protected void assertPersistedMoiToMatchAllProperties(Moi expectedMoi) {
        assertMoiAllPropertiesEquals(expectedMoi, getPersistedMoi(expectedMoi));
    }

    protected void assertPersistedMoiToMatchUpdatableProperties(Moi expectedMoi) {
        assertMoiAllUpdatablePropertiesEquals(expectedMoi, getPersistedMoi(expectedMoi));
    }
}
