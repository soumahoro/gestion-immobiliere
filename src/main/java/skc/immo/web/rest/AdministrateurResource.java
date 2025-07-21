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
import skc.immo.domain.Administrateur;
import skc.immo.repository.AdministrateurRepository;
import skc.immo.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link skc.immo.domain.Administrateur}.
 */
@RestController
@RequestMapping("/api/administrateurs")
@Transactional
public class AdministrateurResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdministrateurResource.class);

    private static final String ENTITY_NAME = "administrateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdministrateurRepository administrateurRepository;

    public AdministrateurResource(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    /**
     * {@code POST  /administrateurs} : Create a new administrateur.
     *
     * @param administrateur the administrateur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administrateur, or with status {@code 400 (Bad Request)} if the administrateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Administrateur> createAdministrateur(@Valid @RequestBody Administrateur administrateur)
        throws URISyntaxException {
        LOG.debug("REST request to save Administrateur : {}", administrateur);
        if (administrateur.getId() != null) {
            throw new BadRequestAlertException("A new administrateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        administrateur = administrateurRepository.save(administrateur);
        return ResponseEntity.created(new URI("/api/administrateurs/" + administrateur.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, administrateur.getId().toString()))
            .body(administrateur);
    }

    /**
     * {@code PUT  /administrateurs/:id} : Updates an existing administrateur.
     *
     * @param id the id of the administrateur to save.
     * @param administrateur the administrateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrateur,
     * or with status {@code 400 (Bad Request)} if the administrateur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administrateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Administrateur> updateAdministrateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Administrateur administrateur
    ) throws URISyntaxException {
        LOG.debug("REST request to update Administrateur : {}, {}", id, administrateur);
        if (administrateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        administrateur = administrateurRepository.save(administrateur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrateur.getId().toString()))
            .body(administrateur);
    }

    /**
     * {@code PATCH  /administrateurs/:id} : Partial updates given fields of an existing administrateur, field will ignore if it is null
     *
     * @param id the id of the administrateur to save.
     * @param administrateur the administrateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrateur,
     * or with status {@code 400 (Bad Request)} if the administrateur is not valid,
     * or with status {@code 404 (Not Found)} if the administrateur is not found,
     * or with status {@code 500 (Internal Server Error)} if the administrateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Administrateur> partialUpdateAdministrateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Administrateur administrateur
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Administrateur partially : {}, {}", id, administrateur);
        if (administrateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Administrateur> result = administrateurRepository
            .findById(administrateur.getId())
            .map(existingAdministrateur -> {
                if (administrateur.getCode() != null) {
                    existingAdministrateur.setCode(administrateur.getCode());
                }

                return existingAdministrateur;
            })
            .map(administrateurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrateur.getId().toString())
        );
    }

    /**
     * {@code GET  /administrateurs} : get all the administrateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of administrateurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Administrateur>> getAllAdministrateurs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Administrateurs");
        Page<Administrateur> page = administrateurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /administrateurs/:id} : get the "id" administrateur.
     *
     * @param id the id of the administrateur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administrateur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getAdministrateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Administrateur : {}", id);
        Optional<Administrateur> administrateur = administrateurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(administrateur);
    }

    /**
     * {@code DELETE  /administrateurs/:id} : delete the "id" administrateur.
     *
     * @param id the id of the administrateur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Administrateur : {}", id);
        administrateurRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
