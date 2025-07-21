package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.ReglementAsserts.*;
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
import skc.immo.domain.Reglement;
import skc.immo.repository.ReglementRepository;

/**
 * Integration tests for the {@link ReglementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReglementResourceIT {

    private static final Integer DEFAULT_IDREG = 1;
    private static final Integer UPDATED_IDREG = 2;

    private static final Integer DEFAULT_ANNEE = 1;
    private static final Integer UPDATED_ANNEE = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_MONTANT = 1;
    private static final Integer UPDATED_MONTANT = 2;

    private static final String DEFAULT_MONTANTLETTRES = "AAAAAAAAAA";
    private static final String UPDATED_MONTANTLETTRES = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERV_1 = "AAAAAAAAAA";
    private static final String UPDATED_OBSERV_1 = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERV_2 = "AAAAAAAAAA";
    private static final String UPDATED_OBSERV_2 = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERV_3 = "AAAAAAAAAA";
    private static final String UPDATED_OBSERV_3 = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESTE = 1;
    private static final Integer UPDATED_RESTE = 2;

    private static final String ENTITY_API_URL = "/api/reglements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReglementRepository reglementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReglementMockMvc;

    private Reglement reglement;

    private Reglement insertedReglement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reglement createEntity() {
        return new Reglement()
            .idreg(DEFAULT_IDREG)
            .annee(DEFAULT_ANNEE)
            .date(DEFAULT_DATE)
            .montant(DEFAULT_MONTANT)
            .montantlettres(DEFAULT_MONTANTLETTRES)
            .motif(DEFAULT_MOTIF)
            .observ1(DEFAULT_OBSERV_1)
            .observ2(DEFAULT_OBSERV_2)
            .observ3(DEFAULT_OBSERV_3)
            .reste(DEFAULT_RESTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reglement createUpdatedEntity() {
        return new Reglement()
            .idreg(UPDATED_IDREG)
            .annee(UPDATED_ANNEE)
            .date(UPDATED_DATE)
            .montant(UPDATED_MONTANT)
            .montantlettres(UPDATED_MONTANTLETTRES)
            .motif(UPDATED_MOTIF)
            .observ1(UPDATED_OBSERV_1)
            .observ2(UPDATED_OBSERV_2)
            .observ3(UPDATED_OBSERV_3)
            .reste(UPDATED_RESTE);
    }

    @BeforeEach
    void initTest() {
        reglement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReglement != null) {
            reglementRepository.delete(insertedReglement);
            insertedReglement = null;
        }
    }

    @Test
    @Transactional
    void createReglement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reglement
        var returnedReglement = om.readValue(
            restReglementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reglement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Reglement.class
        );

        // Validate the Reglement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReglementUpdatableFieldsEquals(returnedReglement, getPersistedReglement(returnedReglement));

        insertedReglement = returnedReglement;
    }

    @Test
    @Transactional
    void createReglementWithExistingId() throws Exception {
        // Create the Reglement with an existing ID
        reglement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReglementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reglement)))
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReglements() throws Exception {
        // Initialize the database
        insertedReglement = reglementRepository.saveAndFlush(reglement);

        // Get all the reglementList
        restReglementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reglement.getId().intValue())))
            .andExpect(jsonPath("$.[*].idreg").value(hasItem(DEFAULT_IDREG)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.[*].montantlettres").value(hasItem(DEFAULT_MONTANTLETTRES)))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].observ1").value(hasItem(DEFAULT_OBSERV_1)))
            .andExpect(jsonPath("$.[*].observ2").value(hasItem(DEFAULT_OBSERV_2)))
            .andExpect(jsonPath("$.[*].observ3").value(hasItem(DEFAULT_OBSERV_3)))
            .andExpect(jsonPath("$.[*].reste").value(hasItem(DEFAULT_RESTE)));
    }

    @Test
    @Transactional
    void getReglement() throws Exception {
        // Initialize the database
        insertedReglement = reglementRepository.saveAndFlush(reglement);

        // Get the reglement
        restReglementMockMvc
            .perform(get(ENTITY_API_URL_ID, reglement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reglement.getId().intValue()))
            .andExpect(jsonPath("$.idreg").value(DEFAULT_IDREG))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT))
            .andExpect(jsonPath("$.montantlettres").value(DEFAULT_MONTANTLETTRES))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.observ1").value(DEFAULT_OBSERV_1))
            .andExpect(jsonPath("$.observ2").value(DEFAULT_OBSERV_2))
            .andExpect(jsonPath("$.observ3").value(DEFAULT_OBSERV_3))
            .andExpect(jsonPath("$.reste").value(DEFAULT_RESTE));
    }

    @Test
    @Transactional
    void getNonExistingReglement() throws Exception {
        // Get the reglement
        restReglementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReglement() throws Exception {
        // Initialize the database
        insertedReglement = reglementRepository.saveAndFlush(reglement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reglement
        Reglement updatedReglement = reglementRepository.findById(reglement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReglement are not directly saved in db
        em.detach(updatedReglement);
        updatedReglement
            .idreg(UPDATED_IDREG)
            .annee(UPDATED_ANNEE)
            .date(UPDATED_DATE)
            .montant(UPDATED_MONTANT)
            .montantlettres(UPDATED_MONTANTLETTRES)
            .motif(UPDATED_MOTIF)
            .observ1(UPDATED_OBSERV_1)
            .observ2(UPDATED_OBSERV_2)
            .observ3(UPDATED_OBSERV_3)
            .reste(UPDATED_RESTE);

        restReglementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReglement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReglement))
            )
            .andExpect(status().isOk());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReglementToMatchAllProperties(updatedReglement);
    }

    @Test
    @Transactional
    void putNonExistingReglement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reglement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reglement.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReglement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reglement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReglement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reglement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reglement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReglementWithPatch() throws Exception {
        // Initialize the database
        insertedReglement = reglementRepository.saveAndFlush(reglement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reglement using partial update
        Reglement partialUpdatedReglement = new Reglement();
        partialUpdatedReglement.setId(reglement.getId());

        partialUpdatedReglement
            .date(UPDATED_DATE)
            .montant(UPDATED_MONTANT)
            .montantlettres(UPDATED_MONTANTLETTRES)
            .observ1(UPDATED_OBSERV_1)
            .observ2(UPDATED_OBSERV_2);

        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReglement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReglement))
            )
            .andExpect(status().isOk());

        // Validate the Reglement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReglementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReglement, reglement),
            getPersistedReglement(reglement)
        );
    }

    @Test
    @Transactional
    void fullUpdateReglementWithPatch() throws Exception {
        // Initialize the database
        insertedReglement = reglementRepository.saveAndFlush(reglement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reglement using partial update
        Reglement partialUpdatedReglement = new Reglement();
        partialUpdatedReglement.setId(reglement.getId());

        partialUpdatedReglement
            .idreg(UPDATED_IDREG)
            .annee(UPDATED_ANNEE)
            .date(UPDATED_DATE)
            .montant(UPDATED_MONTANT)
            .montantlettres(UPDATED_MONTANTLETTRES)
            .motif(UPDATED_MOTIF)
            .observ1(UPDATED_OBSERV_1)
            .observ2(UPDATED_OBSERV_2)
            .observ3(UPDATED_OBSERV_3)
            .reste(UPDATED_RESTE);

        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReglement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReglement))
            )
            .andExpect(status().isOk());

        // Validate the Reglement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReglementUpdatableFieldsEquals(partialUpdatedReglement, getPersistedReglement(partialUpdatedReglement));
    }

    @Test
    @Transactional
    void patchNonExistingReglement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reglement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reglement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReglement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reglement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reglement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReglement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reglement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReglementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reglement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reglement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReglement() throws Exception {
        // Initialize the database
        insertedReglement = reglementRepository.saveAndFlush(reglement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reglement
        restReglementMockMvc
            .perform(delete(ENTITY_API_URL_ID, reglement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reglementRepository.count();
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

    protected Reglement getPersistedReglement(Reglement reglement) {
        return reglementRepository.findById(reglement.getId()).orElseThrow();
    }

    protected void assertPersistedReglementToMatchAllProperties(Reglement expectedReglement) {
        assertReglementAllPropertiesEquals(expectedReglement, getPersistedReglement(expectedReglement));
    }

    protected void assertPersistedReglementToMatchUpdatableProperties(Reglement expectedReglement) {
        assertReglementAllUpdatablePropertiesEquals(expectedReglement, getPersistedReglement(expectedReglement));
    }
}
