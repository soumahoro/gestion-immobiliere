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
import skc.immo.domain.Residence;
import skc.immo.repository.ResidenceRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Residence}.
 */
@RestController
@RequestMapping("/api/residences")
@Transactional
public class ResidenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResidenceResource.class);

    private static final String ENTITY_NAME = "residence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResidenceRepository residenceRepository;

    public ResidenceResource(ResidenceRepository residenceRepository) {
        this.residenceRepository = residenceRepository;
    }

    /**
     * {@code POST  /residences} : Create a new residence.
     *
     * @param residence the residence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new residence, or with status {@code 400 (Bad Request)} if the residence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Residence> createResidence(@Valid @RequestBody Residence residence) throws URISyntaxException {
        LOG.debug("REST request to save Residence : {}", residence);
        if (residence.getId() != null) {
            throw new BadRequestAlertException("A new residence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        residence = residenceRepository.save(residence);
        return ResponseEntity.created(new URI("/api/residences/" + residence.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, residence.getId().toString()))
            .body(residence);
    }

    /**
     * {@code PUT  /residences/:id} : Updates an existing residence.
     *
     * @param id the id of the residence to save.
     * @param residence the residence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated residence,
     * or with status {@code 400 (Bad Request)} if the residence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the residence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Residence> updateResidence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Residence residence
    ) throws URISyntaxException {
        LOG.debug("REST request to update Residence : {}, {}", id, residence);
        if (residence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, residence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!residenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        residence = residenceRepository.save(residence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, residence.getId().toString()))
            .body(residence);
    }

    /**
     * {@code PATCH  /residences/:id} : Partial updates given fields of an existing residence, field will ignore if it is null
     *
     * @param id the id of the residence to save.
     * @param residence the residence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated residence,
     * or with status {@code 400 (Bad Request)} if the residence is not valid,
     * or with status {@code 404 (Not Found)} if the residence is not found,
     * or with status {@code 500 (Internal Server Error)} if the residence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Residence> partialUpdateResidence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Residence residence
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Residence partially : {}, {}", id, residence);
        if (residence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, residence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!residenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Residence> result = residenceRepository
            .findById(residence.getId())
            .map(existingResidence -> {
                if (residence.getIdres() != null) {
                    existingResidence.setIdres(residence.getIdres());
                }
                if (residence.getIlot() != null) {
                    existingResidence.setIlot(residence.getIlot());
                }
                if (residence.getLocalisation() != null) {
                    existingResidence.setLocalisation(residence.getLocalisation());
                }
                if (residence.getObservation() != null) {
                    existingResidence.setObservation(residence.getObservation());
                }
                if (residence.getQuartier() != null) {
                    existingResidence.setQuartier(residence.getQuartier());
                }
                if (residence.getVille() != null) {
                    existingResidence.setVille(residence.getVille());
                }

                return existingResidence;
            })
            .map(residenceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, residence.getId().toString())
        );
    }

    /**
     * {@code GET  /residences} : get all the residences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of residences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Residence>> getAllResidences(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Residences");
        Page<Residence> page = residenceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /residences/:id} : get the "id" residence.
     *
     * @param id the id of the residence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the residence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Residence> getResidence(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Residence : {}", id);
        Optional<Residence> residence = residenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(residence);
    }

    /**
     * {@code DELETE  /residences/:id} : delete the "id" residence.
     *
     * @param id the id of the residence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidence(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Residence : {}", id);
        residenceRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
