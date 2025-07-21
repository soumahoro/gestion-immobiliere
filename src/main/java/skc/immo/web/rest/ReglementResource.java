package skc.immo.web.rest;

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
import skc.immo.domain.Reglement;
import skc.immo.repository.ReglementRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Reglement}.
 */
@RestController
@RequestMapping("/api/reglements")
@Transactional
public class ReglementResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReglementResource.class);

    private static final String ENTITY_NAME = "reglement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReglementRepository reglementRepository;

    public ReglementResource(ReglementRepository reglementRepository) {
        this.reglementRepository = reglementRepository;
    }

    /**
     * {@code POST  /reglements} : Create a new reglement.
     *
     * @param reglement the reglement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reglement, or with status {@code 400 (Bad Request)} if the reglement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Reglement> createReglement(@RequestBody Reglement reglement) throws URISyntaxException {
        LOG.debug("REST request to save Reglement : {}", reglement);
        if (reglement.getId() != null) {
            throw new BadRequestAlertException("A new reglement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reglement = reglementRepository.save(reglement);
        return ResponseEntity.created(new URI("/api/reglements/" + reglement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reglement.getId().toString()))
            .body(reglement);
    }

    /**
     * {@code PUT  /reglements/:id} : Updates an existing reglement.
     *
     * @param id the id of the reglement to save.
     * @param reglement the reglement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reglement,
     * or with status {@code 400 (Bad Request)} if the reglement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reglement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reglement> updateReglement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reglement reglement
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reglement : {}, {}", id, reglement);
        if (reglement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reglement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reglementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reglement = reglementRepository.save(reglement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reglement.getId().toString()))
            .body(reglement);
    }

    /**
     * {@code PATCH  /reglements/:id} : Partial updates given fields of an existing reglement, field will ignore if it is null
     *
     * @param id the id of the reglement to save.
     * @param reglement the reglement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reglement,
     * or with status {@code 400 (Bad Request)} if the reglement is not valid,
     * or with status {@code 404 (Not Found)} if the reglement is not found,
     * or with status {@code 500 (Internal Server Error)} if the reglement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reglement> partialUpdateReglement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reglement reglement
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reglement partially : {}, {}", id, reglement);
        if (reglement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reglement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reglementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reglement> result = reglementRepository
            .findById(reglement.getId())
            .map(existingReglement -> {
                if (reglement.getIdreg() != null) {
                    existingReglement.setIdreg(reglement.getIdreg());
                }
                if (reglement.getAnnee() != null) {
                    existingReglement.setAnnee(reglement.getAnnee());
                }
                if (reglement.getDate() != null) {
                    existingReglement.setDate(reglement.getDate());
                }
                if (reglement.getMontant() != null) {
                    existingReglement.setMontant(reglement.getMontant());
                }
                if (reglement.getMontantlettres() != null) {
                    existingReglement.setMontantlettres(reglement.getMontantlettres());
                }
                if (reglement.getMotif() != null) {
                    existingReglement.setMotif(reglement.getMotif());
                }
                if (reglement.getObserv1() != null) {
                    existingReglement.setObserv1(reglement.getObserv1());
                }
                if (reglement.getObserv2() != null) {
                    existingReglement.setObserv2(reglement.getObserv2());
                }
                if (reglement.getObserv3() != null) {
                    existingReglement.setObserv3(reglement.getObserv3());
                }
                if (reglement.getReste() != null) {
                    existingReglement.setReste(reglement.getReste());
                }

                return existingReglement;
            })
            .map(reglementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reglement.getId().toString())
        );
    }

    /**
     * {@code GET  /reglements} : get all the reglements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reglements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Reglement>> getAllReglements(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Reglements");
        Page<Reglement> page = reglementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reglements/:id} : get the "id" reglement.
     *
     * @param id the id of the reglement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reglement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reglement> getReglement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reglement : {}", id);
        Optional<Reglement> reglement = reglementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reglement);
    }

    /**
     * {@code DELETE  /reglements/:id} : delete the "id" reglement.
     *
     * @param id the id of the reglement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReglement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reglement : {}", id);
        reglementRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
