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
import skc.immo.domain.Appartement;
import skc.immo.repository.AppartementRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Appartement}.
 */
@RestController
@RequestMapping("/api/appartements")
@Transactional
public class AppartementResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppartementResource.class);

    private static final String ENTITY_NAME = "appartement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppartementRepository appartementRepository;

    public AppartementResource(AppartementRepository appartementRepository) {
        this.appartementRepository = appartementRepository;
    }

    /**
     * {@code POST  /appartements} : Create a new appartement.
     *
     * @param appartement the appartement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appartement, or with status {@code 400 (Bad Request)} if the appartement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Appartement> createAppartement(@Valid @RequestBody Appartement appartement) throws URISyntaxException {
        LOG.debug("REST request to save Appartement : {}", appartement);
        if (appartement.getId() != null) {
            throw new BadRequestAlertException("A new appartement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appartement = appartementRepository.save(appartement);
        return ResponseEntity.created(new URI("/api/appartements/" + appartement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appartement.getId().toString()))
            .body(appartement);
    }

    /**
     * {@code PUT  /appartements/:id} : Updates an existing appartement.
     *
     * @param id the id of the appartement to save.
     * @param appartement the appartement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appartement,
     * or with status {@code 400 (Bad Request)} if the appartement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appartement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Appartement> updateAppartement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Appartement appartement
    ) throws URISyntaxException {
        LOG.debug("REST request to update Appartement : {}, {}", id, appartement);
        if (appartement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appartement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appartementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appartement = appartementRepository.save(appartement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appartement.getId().toString()))
            .body(appartement);
    }

    /**
     * {@code PATCH  /appartements/:id} : Partial updates given fields of an existing appartement, field will ignore if it is null
     *
     * @param id the id of the appartement to save.
     * @param appartement the appartement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appartement,
     * or with status {@code 400 (Bad Request)} if the appartement is not valid,
     * or with status {@code 404 (Not Found)} if the appartement is not found,
     * or with status {@code 500 (Internal Server Error)} if the appartement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Appartement> partialUpdateAppartement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Appartement appartement
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Appartement partially : {}, {}", id, appartement);
        if (appartement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appartement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appartementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Appartement> result = appartementRepository
            .findById(appartement.getId())
            .map(existingAppartement -> {
                if (appartement.getIdapp() != null) {
                    existingAppartement.setIdapp(appartement.getIdapp());
                }
                if (appartement.getLibelle() != null) {
                    existingAppartement.setLibelle(appartement.getLibelle());
                }
                if (appartement.getLoyer() != null) {
                    existingAppartement.setLoyer(appartement.getLoyer());
                }
                if (appartement.getNbrepieces() != null) {
                    existingAppartement.setNbrepieces(appartement.getNbrepieces());
                }
                if (appartement.getTaux() != null) {
                    existingAppartement.setTaux(appartement.getTaux());
                }

                return existingAppartement;
            })
            .map(appartementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appartement.getId().toString())
        );
    }

    /**
     * {@code GET  /appartements} : get all the appartements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appartements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Appartement>> getAllAppartements(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Appartements");
        Page<Appartement> page = appartementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appartements/:id} : get the "id" appartement.
     *
     * @param id the id of the appartement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appartement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Appartement> getAppartement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Appartement : {}", id);
        Optional<Appartement> appartement = appartementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appartement);
    }

    /**
     * {@code DELETE  /appartements/:id} : delete the "id" appartement.
     *
     * @param id the id of the appartement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppartement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Appartement : {}", id);
        appartementRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
