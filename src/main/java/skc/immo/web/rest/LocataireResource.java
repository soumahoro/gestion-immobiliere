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
import skc.immo.domain.Locataire;
import skc.immo.repository.LocataireRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Locataire}.
 */
@RestController
@RequestMapping("/api/locataires")
@Transactional
public class LocataireResource {

    private static final Logger LOG = LoggerFactory.getLogger(LocataireResource.class);

    private static final String ENTITY_NAME = "locataire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocataireRepository locataireRepository;

    public LocataireResource(LocataireRepository locataireRepository) {
        this.locataireRepository = locataireRepository;
    }

    /**
     * {@code POST  /locataires} : Create a new locataire.
     *
     * @param locataire the locataire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locataire, or with status {@code 400 (Bad Request)} if the locataire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Locataire> createLocataire(@Valid @RequestBody Locataire locataire) throws URISyntaxException {
        LOG.debug("REST request to save Locataire : {}", locataire);
        if (locataire.getId() != null) {
            throw new BadRequestAlertException("A new locataire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        locataire = locataireRepository.save(locataire);
        return ResponseEntity.created(new URI("/api/locataires/" + locataire.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, locataire.getId().toString()))
            .body(locataire);
    }

    /**
     * {@code PUT  /locataires/:id} : Updates an existing locataire.
     *
     * @param id the id of the locataire to save.
     * @param locataire the locataire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locataire,
     * or with status {@code 400 (Bad Request)} if the locataire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locataire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Locataire> updateLocataire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Locataire locataire
    ) throws URISyntaxException {
        LOG.debug("REST request to update Locataire : {}, {}", id, locataire);
        if (locataire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locataire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locataireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        locataire = locataireRepository.save(locataire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locataire.getId().toString()))
            .body(locataire);
    }

    /**
     * {@code PATCH  /locataires/:id} : Partial updates given fields of an existing locataire, field will ignore if it is null
     *
     * @param id the id of the locataire to save.
     * @param locataire the locataire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locataire,
     * or with status {@code 400 (Bad Request)} if the locataire is not valid,
     * or with status {@code 404 (Not Found)} if the locataire is not found,
     * or with status {@code 500 (Internal Server Error)} if the locataire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Locataire> partialUpdateLocataire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Locataire locataire
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Locataire partially : {}, {}", id, locataire);
        if (locataire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locataire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locataireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Locataire> result = locataireRepository
            .findById(locataire.getId())
            .map(existingLocataire -> {
                if (locataire.getIdloc() != null) {
                    existingLocataire.setIdloc(locataire.getIdloc());
                }
                if (locataire.getArriere() != null) {
                    existingLocataire.setArriere(locataire.getArriere());
                }
                if (locataire.getDate() != null) {
                    existingLocataire.setDate(locataire.getDate());
                }
                if (locataire.getDatedepart() != null) {
                    existingLocataire.setDatedepart(locataire.getDatedepart());
                }
                if (locataire.getFonction() != null) {
                    existingLocataire.setFonction(locataire.getFonction());
                }
                if (locataire.getLoyer() != null) {
                    existingLocataire.setLoyer(locataire.getLoyer());
                }
                if (locataire.getMotifdepart() != null) {
                    existingLocataire.setMotifdepart(locataire.getMotifdepart());
                }
                if (locataire.getNationalite() != null) {
                    existingLocataire.setNationalite(locataire.getNationalite());
                }
                if (locataire.getNom() != null) {
                    existingLocataire.setNom(locataire.getNom());
                }
                if (locataire.getNumpiece() != null) {
                    existingLocataire.setNumpiece(locataire.getNumpiece());
                }
                if (locataire.getObservation() != null) {
                    existingLocataire.setObservation(locataire.getObservation());
                }
                if (locataire.getStatut() != null) {
                    existingLocataire.setStatut(locataire.getStatut());
                }
                if (locataire.getTelephone() != null) {
                    existingLocataire.setTelephone(locataire.getTelephone());
                }
                if (locataire.getTypepiece() != null) {
                    existingLocataire.setTypepiece(locataire.getTypepiece());
                }

                return existingLocataire;
            })
            .map(locataireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locataire.getId().toString())
        );
    }

    /**
     * {@code GET  /locataires} : get all the locataires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locataires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Locataire>> getAllLocataires(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Locataires");
        Page<Locataire> page = locataireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locataires/:id} : get the "id" locataire.
     *
     * @param id the id of the locataire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locataire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Locataire> getLocataire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Locataire : {}", id);
        Optional<Locataire> locataire = locataireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(locataire);
    }

    /**
     * {@code DELETE  /locataires/:id} : delete the "id" locataire.
     *
     * @param id the id of the locataire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocataire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Locataire : {}", id);
        locataireRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
