package ar.edu.um.programacion2.jh.web.rest;

import ar.edu.um.programacion2.jh.domain.Characteristic;
import ar.edu.um.programacion2.jh.repository.CharacteristicRepository;
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
 * REST controller for managing {@link ar.edu.um.programacion2.jh.domain.Characteristic}.
 */
@RestController
@RequestMapping("/api/characteristics")
@Transactional
public class CharacteristicResource {

    private static final Logger LOG = LoggerFactory.getLogger(CharacteristicResource.class);

    private static final String ENTITY_NAME = "characteristic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CharacteristicRepository characteristicRepository;

    public CharacteristicResource(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    /**
     * {@code POST  /characteristics} : Create a new characteristic.
     *
     * @param characteristic the characteristic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new characteristic, or with status {@code 400 (Bad Request)} if the characteristic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Characteristic> createCharacteristic(@Valid @RequestBody Characteristic characteristic)
        throws URISyntaxException {
        LOG.debug("REST request to save Characteristic : {}", characteristic);
        if (characteristic.getId() != null) {
            throw new BadRequestAlertException("A new characteristic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        characteristic = characteristicRepository.save(characteristic);
        return ResponseEntity.created(new URI("/api/characteristics/" + characteristic.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, characteristic.getId().toString()))
            .body(characteristic);
    }

    /**
     * {@code PUT  /characteristics/:id} : Updates an existing characteristic.
     *
     * @param id the id of the characteristic to save.
     * @param characteristic the characteristic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characteristic,
     * or with status {@code 400 (Bad Request)} if the characteristic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the characteristic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Characteristic> updateCharacteristic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Characteristic characteristic
    ) throws URISyntaxException {
        LOG.debug("REST request to update Characteristic : {}, {}", id, characteristic);
        if (characteristic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characteristic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!characteristicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        characteristic = characteristicRepository.save(characteristic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characteristic.getId().toString()))
            .body(characteristic);
    }

    /**
     * {@code PATCH  /characteristics/:id} : Partial updates given fields of an existing characteristic, field will ignore if it is null
     *
     * @param id the id of the characteristic to save.
     * @param characteristic the characteristic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated characteristic,
     * or with status {@code 400 (Bad Request)} if the characteristic is not valid,
     * or with status {@code 404 (Not Found)} if the characteristic is not found,
     * or with status {@code 500 (Internal Server Error)} if the characteristic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Characteristic> partialUpdateCharacteristic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Characteristic characteristic
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Characteristic partially : {}, {}", id, characteristic);
        if (characteristic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, characteristic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!characteristicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Characteristic> result = characteristicRepository
            .findById(characteristic.getId())
            .map(existingCharacteristic -> {
                if (characteristic.getName() != null) {
                    existingCharacteristic.setName(characteristic.getName());
                }
                if (characteristic.getDescription() != null) {
                    existingCharacteristic.setDescription(characteristic.getDescription());
                }

                return existingCharacteristic;
            })
            .map(characteristicRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, characteristic.getId().toString())
        );
    }

    /**
     * {@code GET  /characteristics} : get all the characteristics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of characteristics in body.
     */
    @GetMapping("")
    public List<Characteristic> getAllCharacteristics() {
        LOG.debug("REST request to get all Characteristics");
        return characteristicRepository.findAll();
    }

    /**
     * {@code GET  /characteristics/:id} : get the "id" characteristic.
     *
     * @param id the id of the characteristic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the characteristic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Characteristic> getCharacteristic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Characteristic : {}", id);
        Optional<Characteristic> characteristic = characteristicRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(characteristic);
    }

    /**
     * {@code DELETE  /characteristics/:id} : delete the "id" characteristic.
     *
     * @param id the id of the characteristic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacteristic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Characteristic : {}", id);
        characteristicRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
