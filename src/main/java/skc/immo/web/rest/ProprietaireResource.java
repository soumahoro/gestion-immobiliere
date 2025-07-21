package skc.immo.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import skc.immo.domain.Proprietaire;
import skc.immo.repository.ProprietaireRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Proprietaire}.
 */
@RestController
@RequestMapping("/api/proprietaires")
@Transactional
public class ProprietaireResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProprietaireResource.class);

    private static final String ENTITY_NAME = "proprietaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProprietaireRepository proprietaireRepository;

    public ProprietaireResource(ProprietaireRepository proprietaireRepository) {
        this.proprietaireRepository = proprietaireRepository;
    }

    /**
     * {@code POST  /proprietaires} : Create a new proprietaire.
     *
     * @param proprietaire the proprietaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proprietaire, or with status {@code 400 (Bad Request)} if the proprietaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Proprietaire> createProprietaire(@Valid @RequestBody Proprietaire proprietaire) throws URISyntaxException {
        LOG.debug("REST request to save Proprietaire : {}", proprietaire);
        if (proprietaire.getId() != null) {
            throw new BadRequestAlertException("A new proprietaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        proprietaire = proprietaireRepository.save(proprietaire);
        return ResponseEntity.created(new URI("/api/proprietaires/" + proprietaire.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, proprietaire.getId().toString()))
            .body(proprietaire);
    }

    /**
     * {@code PUT  /proprietaires/:id} : Updates an existing proprietaire.
     *
     * @param id the id of the proprietaire to save.
     * @param proprietaire the proprietaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proprietaire,
     * or with status {@code 400 (Bad Request)} if the proprietaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proprietaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Proprietaire> updateProprietaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Proprietaire proprietaire
    ) throws URISyntaxException {
        LOG.debug("REST request to update Proprietaire : {}, {}", id, proprietaire);
        if (proprietaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proprietaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proprietaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        proprietaire = proprietaireRepository.save(proprietaire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proprietaire.getId().toString()))
            .body(proprietaire);
    }

    /**
     * {@code PATCH  /proprietaires/:id} : Partial updates given fields of an existing proprietaire, field will ignore if it is null
     *
     * @param id the id of the proprietaire to save.
     * @param proprietaire the proprietaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proprietaire,
     * or with status {@code 400 (Bad Request)} if the proprietaire is not valid,
     * or with status {@code 404 (Not Found)} if the proprietaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the proprietaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Proprietaire> partialUpdateProprietaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Proprietaire proprietaire
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Proprietaire partially : {}, {}", id, proprietaire);
        if (proprietaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proprietaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proprietaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proprietaire> result = proprietaireRepository
            .findById(proprietaire.getId())
            .map(existingProprietaire -> {
                if (proprietaire.getIdpro() != null) {
                    existingProprietaire.setIdpro(proprietaire.getIdpro());
                }
                if (proprietaire.getDate() != null) {
                    existingProprietaire.setDate(proprietaire.getDate());
                }
                if (proprietaire.getNom() != null) {
                    existingProprietaire.setNom(proprietaire.getNom());
                }
                if (proprietaire.getResidence() != null) {
                    existingProprietaire.setResidence(proprietaire.getResidence());
                }
                if (proprietaire.getTel() != null) {
                    existingProprietaire.setTel(proprietaire.getTel());
                }

                return existingProprietaire;
            })
            .map(proprietaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proprietaire.getId().toString())
        );
    }

    /**
     * {@code GET  /proprietaires} : get all the proprietaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proprietaires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Proprietaire>> getAllProprietaires(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Proprietaires");
        Page<Proprietaire> page = proprietaireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proprietaires/:id} : get the "id" proprietaire.
     *
     * @param id the id of the proprietaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proprietaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Proprietaire> getProprietaire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Proprietaire : {}", id);
        Optional<Proprietaire> proprietaire = proprietaireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(proprietaire);
    }

    /**
     * {@code DELETE  /proprietaires/:id} : delete the "id" proprietaire.
     *
     * @param id the id of the proprietaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProprietaire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Proprietaire : {}", id);
        proprietaireRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
