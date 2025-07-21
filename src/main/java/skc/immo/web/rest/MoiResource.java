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
import skc.immo.domain.Moi;
import skc.immo.repository.MoiRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Moi}.
 */
@RestController
@RequestMapping("/api/mois")
@Transactional
public class MoiResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoiResource.class);

    private static final String ENTITY_NAME = "moi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoiRepository moiRepository;

    public MoiResource(MoiRepository moiRepository) {
        this.moiRepository = moiRepository;
    }

    /**
     * {@code POST  /mois} : Create a new moi.
     *
     * @param moi the moi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moi, or with status {@code 400 (Bad Request)} if the moi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Moi> createMoi(@Valid @RequestBody Moi moi) throws URISyntaxException {
        LOG.debug("REST request to save Moi : {}", moi);
        if (moi.getId() != null) {
            throw new BadRequestAlertException("A new moi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moi = moiRepository.save(moi);
        return ResponseEntity.created(new URI("/api/mois/" + moi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moi.getId().toString()))
            .body(moi);
    }

    /**
     * {@code PUT  /mois/:id} : Updates an existing moi.
     *
     * @param id the id of the moi to save.
     * @param moi the moi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moi,
     * or with status {@code 400 (Bad Request)} if the moi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Moi> updateMoi(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Moi moi)
        throws URISyntaxException {
        LOG.debug("REST request to update Moi : {}, {}", id, moi);
        if (moi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moi = moiRepository.save(moi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moi.getId().toString()))
            .body(moi);
    }

    /**
     * {@code PATCH  /mois/:id} : Partial updates given fields of an existing moi, field will ignore if it is null
     *
     * @param id the id of the moi to save.
     * @param moi the moi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moi,
     * or with status {@code 400 (Bad Request)} if the moi is not valid,
     * or with status {@code 404 (Not Found)} if the moi is not found,
     * or with status {@code 500 (Internal Server Error)} if the moi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Moi> partialUpdateMoi(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Moi moi)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Moi partially : {}, {}", id, moi);
        if (moi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Moi> result = moiRepository
            .findById(moi.getId())
            .map(existingMoi -> {
                if (moi.getIdmois() != null) {
                    existingMoi.setIdmois(moi.getIdmois());
                }
                if (moi.getMois() != null) {
                    existingMoi.setMois(moi.getMois());
                }

                return existingMoi;
            })
            .map(moiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moi.getId().toString())
        );
    }

    /**
     * {@code GET  /mois} : get all the mois.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mois in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Moi>> getAllMois(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Mois");
        Page<Moi> page = moiRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mois/:id} : get the "id" moi.
     *
     * @param id the id of the moi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Moi> getMoi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Moi : {}", id);
        Optional<Moi> moi = moiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(moi);
    }

    /**
     * {@code DELETE  /mois/:id} : delete the "id" moi.
     *
     * @param id the id of the moi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Moi : {}", id);
        moiRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
