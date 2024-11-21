package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.domain.SaleItem;
import ar.edu.um.programacion2.jh.repository.SaleItemRepository;
import ar.edu.um.programacion2.jh.service.SaleItemService;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.jh.domain.SaleItem}.
 */
@RestController
@RequestMapping("/api/sale-items")
public class SaleItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaleItemResource.class);

    private static final String ENTITY_NAME = "saleItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaleItemService saleItemService;

    private final SaleItemRepository saleItemRepository;

    public SaleItemResource(SaleItemService saleItemService, SaleItemRepository saleItemRepository) {
        this.saleItemService = saleItemService;
        this.saleItemRepository = saleItemRepository;
    }

    /**
     * {@code POST  /sale-items} : Create a new saleItem.
     *
     * @param saleItem the saleItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saleItem, or with status {@code 400 (Bad Request)} if the saleItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaleItem> createSaleItem(@Valid @RequestBody SaleItem saleItem) throws URISyntaxException {
        LOG.debug("REST request to save SaleItem : {}", saleItem);
        if (saleItem.getId() != null) {
            throw new BadRequestAlertException("A new saleItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saleItem = saleItemService.save(saleItem);
        return ResponseEntity.created(new URI("/api/sale-items/" + saleItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saleItem.getId().toString()))
            .body(saleItem);
    }

    /**
     * {@code PUT  /sale-items/:id} : Updates an existing saleItem.
     *
     * @param id the id of the saleItem to save.
     * @param saleItem the saleItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saleItem,
     * or with status {@code 400 (Bad Request)} if the saleItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saleItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaleItem> updateSaleItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaleItem saleItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update SaleItem : {}, {}", id, saleItem);
        if (saleItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saleItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saleItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saleItem = saleItemService.update(saleItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saleItem.getId().toString()))
            .body(saleItem);
    }

    /**
     * {@code PATCH  /sale-items/:id} : Partial updates given fields of an existing saleItem, field will ignore if it is null
     *
     * @param id the id of the saleItem to save.
     * @param saleItem the saleItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saleItem,
     * or with status {@code 400 (Bad Request)} if the saleItem is not valid,
     * or with status {@code 404 (Not Found)} if the saleItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the saleItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaleItem> partialUpdateSaleItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaleItem saleItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SaleItem partially : {}, {}", id, saleItem);
        if (saleItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saleItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saleItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaleItem> result = saleItemService.partialUpdate(saleItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saleItem.getId().toString())
        );
    }

    /**
     * {@code GET  /sale-items} : get all the saleItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saleItems in body.
     */
    @GetMapping("")
    public List<SaleItem> getAllSaleItems() {
        LOG.debug("REST request to get all SaleItems");
        return saleItemService.findAll();
    }

    /**
     * {@code GET  /sale-items/:id} : get the "id" saleItem.
     *
     * @param id the id of the saleItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saleItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaleItem> getSaleItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SaleItem : {}", id);
        Optional<SaleItem> saleItem = saleItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saleItem);
    }

    /**
     * {@code DELETE  /sale-items/:id} : delete the "id" saleItem.
     *
     * @param id the id of the saleItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SaleItem : {}", id);
        saleItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
