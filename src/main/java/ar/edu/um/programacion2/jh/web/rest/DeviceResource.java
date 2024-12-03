package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.domain.Device;
import ar.edu.um.programacion2.jh.repository.DeviceRepository;
import ar.edu.um.programacion2.jh.service.DeviceService;
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
 * REST controller for managing {@link ar.edu.um.programacion2.jh.domain.Device}.
 */
@RestController
@RequestMapping("/api/devices")
public class DeviceResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceService deviceService;

    private final DeviceRepository deviceRepository;

    public DeviceResource(DeviceService deviceService, DeviceRepository deviceRepository) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param device the device to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new device, or with status {@code 400 (Bad Request)} if the device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody Device device) throws URISyntaxException {
        LOG.debug("REST request to save Device : {}", device);
        if (device.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        device = deviceService.save(device);
        return ResponseEntity.created(new URI("/api/devices/" + device.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, device.getId().toString()))
            .body(device);
    }

    /**
     * {@code PUT  /devices/:id} : Updates an existing device.
     *
     * @param id the id of the device to save.
     * @param device the device to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated device,
     * or with status {@code 400 (Bad Request)} if the device is not valid,
     * or with status {@code 500 (Internal Server Error)} if the device couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Device> updateDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Device device
    ) throws URISyntaxException {
        LOG.debug("REST request to update Device : {}, {}", id, device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, device.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        device = deviceService.update(device);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, device.getId().toString()))
            .body(device);
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates given fields of an existing device, field will ignore if it is null
     *
     * @param id the id of the device to save.
     * @param device the device to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated device,
     * or with status {@code 400 (Bad Request)} if the device is not valid,
     * or with status {@code 404 (Not Found)} if the device is not found,
     * or with status {@code 500 (Internal Server Error)} if the device couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Device> partialUpdateDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Device device
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Device partially : {}, {}", id, device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, device.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Device> result = deviceService.partialUpdate(device);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, device.getId().toString())
        );
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Device>> getAllDevices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload,
        @RequestParam(name = "active", required = false) Boolean active
    ) {
        LOG.debug("REST request to get a page of Devices");
        Page<Device> page;
        if (active != null) {
            page = deviceService.findAllByActive(active, pageable);
        } else {
            if (eagerload) {
                page = deviceService.findAllWithEagerRelationships(pageable);
            } else {
                page = deviceService.findAll(pageable);
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the device to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the device, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Device : {}", id);
        Optional<Device> device = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(device);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the device to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Device : {}", id);
        deviceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
