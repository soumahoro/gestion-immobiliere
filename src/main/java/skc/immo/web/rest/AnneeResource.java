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
import skc.immo.domain.Annee;
import skc.immo.repository.AnneeRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Annee}.
 */
@RestController
@RequestMapping("/api/annees")
@Transactional
public class AnneeResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnneeResource.class);

    private static final String ENTITY_NAME = "annee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnneeRepository anneeRepository;

    public AnneeResource(AnneeRepository anneeRepository) {
        this.anneeRepository = anneeRepository;
    }

    /**
     * {@code POST  /annees} : Create a new annee.
     *
     * @param annee the annee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annee, or with status {@code 400 (Bad Request)} if the annee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Annee> createAnnee(@Valid @RequestBody Annee annee) throws URISyntaxException {
        LOG.debug("REST request to save Annee : {}", annee);
        if (annee.getId() != null) {
            throw new BadRequestAlertException("A new annee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        annee = anneeRepository.save(annee);
        return ResponseEntity.created(new URI("/api/annees/" + annee.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, annee.getId().toString()))
            .body(annee);
    }

    /**
     * {@code PUT  /annees/:id} : Updates an existing annee.
     *
     * @param id the id of the annee to save.
     * @param annee the annee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annee,
     * or with status {@code 400 (Bad Request)} if the annee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Annee> updateAnnee(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Annee annee)
        throws URISyntaxException {
        LOG.debug("REST request to update Annee : {}, {}", id, annee);
        if (annee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        annee = anneeRepository.save(annee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annee.getId().toString()))
            .body(annee);
    }

    /**
     * {@code PATCH  /annees/:id} : Partial updates given fields of an existing annee, field will ignore if it is null
     *
     * @param id the id of the annee to save.
     * @param annee the annee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annee,
     * or with status {@code 400 (Bad Request)} if the annee is not valid,
     * or with status {@code 404 (Not Found)} if the annee is not found,
     * or with status {@code 500 (Internal Server Error)} if the annee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Annee> partialUpdateAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Annee annee
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Annee partially : {}, {}", id, annee);
        if (annee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Annee> result = anneeRepository
            .findById(annee.getId())
            .map(existingAnnee -> {
                if (annee.getAn() != null) {
                    existingAnnee.setAn(annee.getAn());
                }
                if (annee.getLibelle() != null) {
                    existingAnnee.setLibelle(annee.getLibelle());
                }

                return existingAnnee;
            })
            .map(anneeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annee.getId().toString())
        );
    }

    /**
     * {@code GET  /annees} : get all the annees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Annee>> getAllAnnees(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Annees");
        Page<Annee> page = anneeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annees/:id} : get the "id" annee.
     *
     * @param id the id of the annee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Annee> getAnnee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Annee : {}", id);
        Optional<Annee> annee = anneeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(annee);
    }

    /**
     * {@code DELETE  /annees/:id} : delete the "id" annee.
     *
     * @param id the id of the annee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnee(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Annee : {}", id);
        anneeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
