package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.LocataireAsserts.*;
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
import skc.immo.domain.Locataire;
import skc.immo.repository.LocataireRepository;

/**
 * Integration tests for the {@link LocataireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocataireResourceIT {

    private static final String DEFAULT_IDLOC = "AAAAAAAAAA";
    private static final String UPDATED_IDLOC = "BBBBBBBBBB";

    private static final Integer DEFAULT_ARRIERE = 1;
    private static final Integer UPDATED_ARRIERE = 2;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATEDEPART = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATEDEPART = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_FONCTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOYER = 1;
    private static final Integer UPDATED_LOYER = 2;

    private static final String DEFAULT_MOTIFDEPART = "AAAAAAAAAA";
    private static final String UPDATED_MOTIFDEPART = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_NUMPIECE = "AAAAAAAAAA";
    private static final String UPDATED_NUMPIECE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPEPIECE = "AAAAAAAAAA";
    private static final String UPDATED_TYPEPIECE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locataires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocataireRepository locataireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocataireMockMvc;

    private Locataire locataire;

    private Locataire insertedLocataire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locataire createEntity() {
        return new Locataire()
            .idloc(DEFAULT_IDLOC)
            .arriere(DEFAULT_ARRIERE)
            .date(DEFAULT_DATE)
            .datedepart(DEFAULT_DATEDEPART)
            .fonction(DEFAULT_FONCTION)
            .loyer(DEFAULT_LOYER)
            .motifdepart(DEFAULT_MOTIFDEPART)
            .nationalite(DEFAULT_NATIONALITE)
            .nom(DEFAULT_NOM)
            .numpiece(DEFAULT_NUMPIECE)
            .observation(DEFAULT_OBSERVATION)
            .statut(DEFAULT_STATUT)
            .telephone(DEFAULT_TELEPHONE)
            .typepiece(DEFAULT_TYPEPIECE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Locataire createUpdatedEntity() {
        return new Locataire()
            .idloc(UPDATED_IDLOC)
            .arriere(UPDATED_ARRIERE)
            .date(UPDATED_DATE)
            .datedepart(UPDATED_DATEDEPART)
            .fonction(UPDATED_FONCTION)
            .loyer(UPDATED_LOYER)
            .motifdepart(UPDATED_MOTIFDEPART)
            .nationalite(UPDATED_NATIONALITE)
            .nom(UPDATED_NOM)
            .numpiece(UPDATED_NUMPIECE)
            .observation(UPDATED_OBSERVATION)
            .statut(UPDATED_STATUT)
            .telephone(UPDATED_TELEPHONE)
            .typepiece(UPDATED_TYPEPIECE);
    }

    @BeforeEach
    void initTest() {
        locataire = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLocataire != null) {
            locataireRepository.delete(insertedLocataire);
            insertedLocataire = null;
        }
    }

    @Test
    @Transactional
    void createLocataire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Locataire
        var returnedLocataire = om.readValue(
            restLocataireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locataire)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Locataire.class
        );

        // Validate the Locataire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocataireUpdatableFieldsEquals(returnedLocataire, getPersistedLocataire(returnedLocataire));

        insertedLocataire = returnedLocataire;
    }

    @Test
    @Transactional
    void createLocataireWithExistingId() throws Exception {
        // Create the Locataire with an existing ID
        locataire.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocataireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locataire)))
            .andExpect(status().isBadRequest());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdlocIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        locataire.setIdloc(null);

        // Create the Locataire, which fails.

        restLocataireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locataire)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocataires() throws Exception {
        // Initialize the database
        insertedLocataire = locataireRepository.saveAndFlush(locataire);

        // Get all the locataireList
        restLocataireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locataire.getId().intValue())))
            .andExpect(jsonPath("$.[*].idloc").value(hasItem(DEFAULT_IDLOC)))
            .andExpect(jsonPath("$.[*].arriere").value(hasItem(DEFAULT_ARRIERE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].datedepart").value(hasItem(sameInstant(DEFAULT_DATEDEPART))))
            .andExpect(jsonPath("$.[*].fonction").value(hasItem(DEFAULT_FONCTION)))
            .andExpect(jsonPath("$.[*].loyer").value(hasItem(DEFAULT_LOYER)))
            .andExpect(jsonPath("$.[*].motifdepart").value(hasItem(DEFAULT_MOTIFDEPART)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].numpiece").value(hasItem(DEFAULT_NUMPIECE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].typepiece").value(hasItem(DEFAULT_TYPEPIECE)));
    }

    @Test
    @Transactional
    void getLocataire() throws Exception {
        // Initialize the database
        insertedLocataire = locataireRepository.saveAndFlush(locataire);

        // Get the locataire
        restLocataireMockMvc
            .perform(get(ENTITY_API_URL_ID, locataire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locataire.getId().intValue()))
            .andExpect(jsonPath("$.idloc").value(DEFAULT_IDLOC))
            .andExpect(jsonPath("$.arriere").value(DEFAULT_ARRIERE))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.datedepart").value(sameInstant(DEFAULT_DATEDEPART)))
            .andExpect(jsonPath("$.fonction").value(DEFAULT_FONCTION))
            .andExpect(jsonPath("$.loyer").value(DEFAULT_LOYER))
            .andExpect(jsonPath("$.motifdepart").value(DEFAULT_MOTIFDEPART))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.numpiece").value(DEFAULT_NUMPIECE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.typepiece").value(DEFAULT_TYPEPIECE));
    }

    @Test
    @Transactional
    void getNonExistingLocataire() throws Exception {
        // Get the locataire
        restLocataireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocataire() throws Exception {
        // Initialize the database
        insertedLocataire = locataireRepository.saveAndFlush(locataire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locataire
        Locataire updatedLocataire = locataireRepository.findById(locataire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocataire are not directly saved in db
        em.detach(updatedLocataire);
        updatedLocataire
            .idloc(UPDATED_IDLOC)
            .arriere(UPDATED_ARRIERE)
            .date(UPDATED_DATE)
            .datedepart(UPDATED_DATEDEPART)
            .fonction(UPDATED_FONCTION)
            .loyer(UPDATED_LOYER)
            .motifdepart(UPDATED_MOTIFDEPART)
            .nationalite(UPDATED_NATIONALITE)
            .nom(UPDATED_NOM)
            .numpiece(UPDATED_NUMPIECE)
            .observation(UPDATED_OBSERVATION)
            .statut(UPDATED_STATUT)
            .telephone(UPDATED_TELEPHONE)
            .typepiece(UPDATED_TYPEPIECE);

        restLocataireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocataire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocataire))
            )
            .andExpect(status().isOk());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocataireToMatchAllProperties(updatedLocataire);
    }

    @Test
    @Transactional
    void putNonExistingLocataire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locataire.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocataireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locataire.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocataire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locataire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocataireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocataire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locataire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocataireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locataire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocataireWithPatch() throws Exception {
        // Initialize the database
        insertedLocataire = locataireRepository.saveAndFlush(locataire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locataire using partial update
        Locataire partialUpdatedLocataire = new Locataire();
        partialUpdatedLocataire.setId(locataire.getId());

        partialUpdatedLocataire.fonction(UPDATED_FONCTION).numpiece(UPDATED_NUMPIECE).observation(UPDATED_OBSERVATION);

        restLocataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocataire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocataire))
            )
            .andExpect(status().isOk());

        // Validate the Locataire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocataireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocataire, locataire),
            getPersistedLocataire(locataire)
        );
    }

    @Test
    @Transactional
    void fullUpdateLocataireWithPatch() throws Exception {
        // Initialize the database
        insertedLocataire = locataireRepository.saveAndFlush(locataire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the locataire using partial update
        Locataire partialUpdatedLocataire = new Locataire();
        partialUpdatedLocataire.setId(locataire.getId());

        partialUpdatedLocataire
            .idloc(UPDATED_IDLOC)
            .arriere(UPDATED_ARRIERE)
            .date(UPDATED_DATE)
            .datedepart(UPDATED_DATEDEPART)
            .fonction(UPDATED_FONCTION)
            .loyer(UPDATED_LOYER)
            .motifdepart(UPDATED_MOTIFDEPART)
            .nationalite(UPDATED_NATIONALITE)
            .nom(UPDATED_NOM)
            .numpiece(UPDATED_NUMPIECE)
            .observation(UPDATED_OBSERVATION)
            .statut(UPDATED_STATUT)
            .telephone(UPDATED_TELEPHONE)
            .typepiece(UPDATED_TYPEPIECE);

        restLocataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocataire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocataire))
            )
            .andExpect(status().isOk());

        // Validate the Locataire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocataireUpdatableFieldsEquals(partialUpdatedLocataire, getPersistedLocataire(partialUpdatedLocataire));
    }

    @Test
    @Transactional
    void patchNonExistingLocataire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locataire.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locataire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocataire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locataire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocataireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locataire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocataire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        locataire.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocataireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(locataire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Locataire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocataire() throws Exception {
        // Initialize the database
        insertedLocataire = locataireRepository.saveAndFlush(locataire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the locataire
        restLocataireMockMvc
            .perform(delete(ENTITY_API_URL_ID, locataire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locataireRepository.count();
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

    protected Locataire getPersistedLocataire(Locataire locataire) {
        return locataireRepository.findById(locataire.getId()).orElseThrow();
    }

    protected void assertPersistedLocataireToMatchAllProperties(Locataire expectedLocataire) {
        assertLocataireAllPropertiesEquals(expectedLocataire, getPersistedLocataire(expectedLocataire));
    }

    protected void assertPersistedLocataireToMatchUpdatableProperties(Locataire expectedLocataire) {
        assertLocataireAllUpdatablePropertiesEquals(expectedLocataire, getPersistedLocataire(expectedLocataire));
    }
}
