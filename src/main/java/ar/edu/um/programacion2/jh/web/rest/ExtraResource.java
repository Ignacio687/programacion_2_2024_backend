package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.domain.Extra;
import ar.edu.um.programacion2.jh.repository.ExtraRepository;
import ar.edu.um.programacion2.jh.service.ExtraService;
import ar.edu.um.programacion2.jh.web.rest.errors.BadRequestAlertException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.jh.domain.Extra}.
 */
@RestController
@RequestMapping("/api/extras")
public class ExtraResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExtraResource.class);

    private static final String ENTITY_NAME = "extra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraService extraService;

    private final ExtraRepository extraRepository;

    public ExtraResource(ExtraService extraService, ExtraRepository extraRepository) {
        this.extraService = extraService;
        this.extraRepository = extraRepository;
    }

    /**
     * {@code POST  /extras} : Create a new extra.
     *
     * @param extra the extra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extra, or with status {@code 400 (Bad Request)} if the extra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Extra> createExtra(@Valid @RequestBody Extra extra) throws URISyntaxException {
        LOG.debug("REST request to save Extra : {}", extra);
        if (extra.getId() != null) {
            throw new BadRequestAlertException("A new extra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        extra = extraService.save(extra);
        return ResponseEntity.created(new URI("/api/extras/" + extra.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, extra.getId().toString()))
            .body(extra);
    }

    /**
     * {@code PUT  /extras/:id} : Updates an existing extra.
     *
     * @param id the id of the extra to save.
     * @param extra the extra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extra,
     * or with status {@code 400 (Bad Request)} if the extra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Extra> updateExtra(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Extra extra)
        throws URISyntaxException {
        LOG.debug("REST request to update Extra : {}, {}", id, extra);
        if (extra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        extra = extraService.update(extra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extra.getId().toString()))
            .body(extra);
    }

    /**
     * {@code PATCH  /extras/:id} : Partial updates given fields of an existing extra, field will ignore if it is null
     *
     * @param id the id of the extra to save.
     * @param extra the extra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extra,
     * or with status {@code 400 (Bad Request)} if the extra is not valid,
     * or with status {@code 404 (Not Found)} if the extra is not found,
     * or with status {@code 500 (Internal Server Error)} if the extra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Extra> partialUpdateExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Extra extra
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Extra partially : {}, {}", id, extra);
        if (extra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Extra> result = extraService.partialUpdate(extra);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extra.getId().toString())
        );
    }

    /**
     * {@code GET  /extras} : get all the extras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Extra>> getAllExtras(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Extras");
        Page<Extra> page = extraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /extras/:id} : get the "id" extra.
     *
     * @param id the id of the extra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Extra> getExtra(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Extra : {}", id);
        Optional<Extra> extra = extraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extra);
    }

    /**
     * {@code DELETE  /extras/:id} : delete the "id" extra.
     *
     * @param id the id of the extra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteExtra(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Extra : {}", id);
        extraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
