package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.domain.Customization;
import ar.edu.um.programacion2.jh.repository.CustomizationRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.jh.domain.Customization}.
 */
@RestController
@RequestMapping("/api/customizations")
@Transactional
public class CustomizationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomizationResource.class);

    private static final String ENTITY_NAME = "customization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomizationRepository customizationRepository;

    public CustomizationResource(CustomizationRepository customizationRepository) {
        this.customizationRepository = customizationRepository;
    }

    /**
     * {@code POST  /customizations} : Create a new customization.
     *
     * @param customization the customization to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customization, or with status {@code 400 (Bad Request)} if the customization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Customization> createCustomization(@Valid @RequestBody Customization customization) throws URISyntaxException {
        LOG.debug("REST request to save Customization : {}", customization);
        if (customization.getId() != null) {
            throw new BadRequestAlertException("A new customization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customization = customizationRepository.save(customization);
        return ResponseEntity.created(new URI("/api/customizations/" + customization.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customization.getId().toString()))
            .body(customization);
    }

    /**
     * {@code PUT  /customizations/:id} : Updates an existing customization.
     *
     * @param id the id of the customization to save.
     * @param customization the customization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customization,
     * or with status {@code 400 (Bad Request)} if the customization is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customization> updateCustomization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Customization customization
    ) throws URISyntaxException {
        LOG.debug("REST request to update Customization : {}, {}", id, customization);
        if (customization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customization.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customization = customizationRepository.save(customization);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customization.getId().toString()))
            .body(customization);
    }

    /**
     * {@code PATCH  /customizations/:id} : Partial updates given fields of an existing customization, field will ignore if it is null
     *
     * @param id the id of the customization to save.
     * @param customization the customization to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customization,
     * or with status {@code 400 (Bad Request)} if the customization is not valid,
     * or with status {@code 404 (Not Found)} if the customization is not found,
     * or with status {@code 500 (Internal Server Error)} if the customization couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Customization> partialUpdateCustomization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Customization customization
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Customization partially : {}, {}", id, customization);
        if (customization.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customization.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Customization> result = customizationRepository
            .findById(customization.getId())
            .map(existingCustomization -> {
                if (customization.getName() != null) {
                    existingCustomization.setName(customization.getName());
                }
                if (customization.getDescription() != null) {
                    existingCustomization.setDescription(customization.getDescription());
                }

                return existingCustomization;
            })
            .map(customizationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customization.getId().toString())
        );
    }

    /**
     * {@code GET  /customizations} : get all the customizations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customizations in body.
     */
    @GetMapping("")
    public List<Customization> getAllCustomizations() {
        LOG.debug("REST request to get all Customizations");
        return customizationRepository.findAll();
    }

    /**
     * {@code GET  /customizations/:id} : get the "id" customization.
     *
     * @param id the id of the customization to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customization, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customization> getCustomization(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Customization : {}", id);
        Optional<Customization> customization = customizationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customization);
    }

    /**
     * {@code DELETE  /customizations/:id} : delete the "id" customization.
     *
     * @param id the id of the customization to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomization(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Customization : {}", id);
        customizationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
