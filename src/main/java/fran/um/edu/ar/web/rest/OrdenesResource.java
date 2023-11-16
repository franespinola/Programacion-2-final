package fran.um.edu.ar.web.rest;

import fran.um.edu.ar.domain.Ordenes;
import fran.um.edu.ar.repository.OrdenesRepository;
import fran.um.edu.ar.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fran.um.edu.ar.domain.Ordenes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrdenesResource {

    private final Logger log = LoggerFactory.getLogger(OrdenesResource.class);

    private static final String ENTITY_NAME = "ordenes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenesRepository ordenesRepository;

    public OrdenesResource(OrdenesRepository ordenesRepository) {
        this.ordenesRepository = ordenesRepository;
    }

    /**
     * {@code POST  /ordenes} : Create a new ordenes.
     *
     * @param ordenes the ordenes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordenes, or with status {@code 400 (Bad Request)} if the ordenes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordenes")
    public ResponseEntity<Ordenes> createOrdenes(@RequestBody Ordenes ordenes) throws URISyntaxException {
        log.debug("REST request to save Ordenes : {}", ordenes);
        if (ordenes.getId() != null) {
            throw new BadRequestAlertException("A new ordenes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ordenes result = ordenesRepository.save(ordenes);
        return ResponseEntity
            .created(new URI("/api/ordenes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordenes/:id} : Updates an existing ordenes.
     *
     * @param id the id of the ordenes to save.
     * @param ordenes the ordenes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenes,
     * or with status {@code 400 (Bad Request)} if the ordenes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordenes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordenes/{id}")
    public ResponseEntity<Ordenes> updateOrdenes(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ordenes ordenes)
        throws URISyntaxException {
        log.debug("REST request to update Ordenes : {}, {}", id, ordenes);
        if (ordenes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ordenes result = ordenesRepository.save(ordenes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordenes/:id} : Partial updates given fields of an existing ordenes, field will ignore if it is null
     *
     * @param id the id of the ordenes to save.
     * @param ordenes the ordenes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenes,
     * or with status {@code 400 (Bad Request)} if the ordenes is not valid,
     * or with status {@code 404 (Not Found)} if the ordenes is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordenes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordenes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ordenes> partialUpdateOrdenes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ordenes ordenes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ordenes partially : {}, {}", id, ordenes);
        if (ordenes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ordenes> result = ordenesRepository
            .findById(ordenes.getId())
            .map(existingOrdenes -> {
                if (ordenes.getCliente() != null) {
                    existingOrdenes.setCliente(ordenes.getCliente());
                }
                if (ordenes.getAccionId() != null) {
                    existingOrdenes.setAccionId(ordenes.getAccionId());
                }
                if (ordenes.getAccion() != null) {
                    existingOrdenes.setAccion(ordenes.getAccion());
                }
                if (ordenes.getOperacion() != null) {
                    existingOrdenes.setOperacion(ordenes.getOperacion());
                }
                if (ordenes.getPrecio() != null) {
                    existingOrdenes.setPrecio(ordenes.getPrecio());
                }
                if (ordenes.getCantidad() != null) {
                    existingOrdenes.setCantidad(ordenes.getCantidad());
                }
                if (ordenes.getFechaOperacion() != null) {
                    existingOrdenes.setFechaOperacion(ordenes.getFechaOperacion());
                }
                if (ordenes.getModo() != null) {
                    existingOrdenes.setModo(ordenes.getModo());
                }

                return existingOrdenes;
            })
            .map(ordenesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenes.getId().toString())
        );
    }

    /**
     * {@code GET  /ordenes} : get all the ordenes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordenes in body.
     */
    @GetMapping("/ordenes")
    public ResponseEntity<List<Ordenes>> getAllOrdenes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ordenes");
        Page<Ordenes> page = ordenesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordenes/:id} : get the "id" ordenes.
     *
     * @param id the id of the ordenes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordenes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordenes/{id}")
    public ResponseEntity<Ordenes> getOrdenes(@PathVariable Long id) {
        log.debug("REST request to get Ordenes : {}", id);
        Optional<Ordenes> ordenes = ordenesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ordenes);
    }

    /**
     * {@code DELETE  /ordenes/:id} : delete the "id" ordenes.
     *
     * @param id the id of the ordenes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordenes/{id}")
    public ResponseEntity<Void> deleteOrdenes(@PathVariable Long id) {
        log.debug("REST request to delete Ordenes : {}", id);
        ordenesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
