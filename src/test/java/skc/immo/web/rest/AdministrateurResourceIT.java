package skc.immo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static skc.immo.domain.AdministrateurAsserts.*;
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
import skc.immo.domain.Administrateur;
import skc.immo.repository.AdministrateurRepository;

/**
 * Integration tests for the {@link AdministrateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministrateurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/administrateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministrateurMockMvc;

    private Administrateur administrateur;

    private Administrateur insertedAdministrateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createEntity() {
        return new Administrateur().code(DEFAULT_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createUpdatedEntity() {
        return new Administrateur().code(UPDATED_CODE);
    }

    @BeforeEach
    void initTest() {
        administrateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAdministrateur != null) {
            administrateurRepository.delete(insertedAdministrateur);
            insertedAdministrateur = null;
        }
    }

    @Test
    @Transactional
    void createAdministrateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Administrateur
        var returnedAdministrateur = om.readValue(
            restAdministrateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateur)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Administrateur.class
        );

        // Validate the Administrateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAdministrateurUpdatableFieldsEquals(returnedAdministrateur, getPersistedAdministrateur(returnedAdministrateur));

        insertedAdministrateur = returnedAdministrateur;
    }

    @Test
    @Transactional
    void createAdministrateurWithExistingId() throws Exception {
        // Create the Administrateur with an existing ID
        administrateur.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateur)))
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        administrateur.setCode(null);

        // Create the Administrateur, which fails.

        restAdministrateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateur)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministrateurs() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        // Get all the administrateurList
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getAdministrateur() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        // Get the administrateur
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL_ID, administrateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingAdministrateur() throws Exception {
        // Get the administrateur
        restAdministrateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdministrateur() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrateur
        Administrateur updatedAdministrateur = administrateurRepository.findById(administrateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdministrateur are not directly saved in db
        em.detach(updatedAdministrateur);
        updatedAdministrateur.code(UPDATED_CODE);

        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdministrateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdministrateurToMatchAllProperties(updatedAdministrateur);
    }

    @Test
    @Transactional
    void putNonExistingAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administrateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        partialUpdatedAdministrateur.code(UPDATED_CODE);

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministrateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdministrateur, administrateur),
            getPersistedAdministrateur(administrateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        partialUpdatedAdministrateur.code(UPDATED_CODE);

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministrateurUpdatableFieldsEquals(partialUpdatedAdministrateur, getPersistedAdministrateur(partialUpdatedAdministrateur));
    }

    @Test
    @Transactional
    void patchNonExistingAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(administrateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrateur() throws Exception {
        // Initialize the database
        insertedAdministrateur = administrateurRepository.saveAndFlush(administrateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the administrateur
        restAdministrateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return administrateurRepository.count();
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

    protected Administrateur getPersistedAdministrateur(Administrateur administrateur) {
        return administrateurRepository.findById(administrateur.getId()).orElseThrow();
    }

    protected void assertPersistedAdministrateurToMatchAllProperties(Administrateur expectedAdministrateur) {
        assertAdministrateurAllPropertiesEquals(expectedAdministrateur, getPersistedAdministrateur(expectedAdministrateur));
    }

    protected void assertPersistedAdministrateurToMatchUpdatableProperties(Administrateur expectedAdministrateur) {
        assertAdministrateurAllUpdatablePropertiesEquals(expectedAdministrateur, getPersistedAdministrateur(expectedAdministrateur));
    }
}
