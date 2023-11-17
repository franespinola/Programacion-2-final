package fran.um.edu.ar.web.rest;

import fran.um.edu.ar.repository.OrdenesRepository;
import fran.um.edu.ar.service.OrdenesQueryService;
import fran.um.edu.ar.service.OrdenesService;
import fran.um.edu.ar.service.criteria.OrdenesCriteria;
import fran.um.edu.ar.service.dto.OrdenesDTO;
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
import org.springframework.http.ResponseEntity;
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
public class OrdenesResource {

    private final Logger log = LoggerFactory.getLogger(OrdenesResource.class);

    private static final String ENTITY_NAME = "ordenes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenesService ordenesService;

    private final OrdenesRepository ordenesRepository;

    private final OrdenesQueryService ordenesQueryService;

    public OrdenesResource(OrdenesService ordenesService, OrdenesRepository ordenesRepository, OrdenesQueryService ordenesQueryService) {
        this.ordenesService = ordenesService;
        this.ordenesRepository = ordenesRepository;
        this.ordenesQueryService = ordenesQueryService;
    }

    /**
     * {@code POST  /ordenes} : Create a new ordenes.
     *
     * @param ordenesDTO the ordenesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordenesDTO, or with status {@code 400 (Bad Request)} if the ordenes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordenes")
    public ResponseEntity<OrdenesDTO> createOrdenes(@RequestBody OrdenesDTO ordenesDTO) throws URISyntaxException {
        log.debug("REST request to save Ordenes : {}", ordenesDTO);
        if (ordenesDTO.getId() != null) {
            throw new BadRequestAlertException("A new ordenes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdenesDTO result = ordenesService.save(ordenesDTO);
        return ResponseEntity
            .created(new URI("/api/ordenes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordenes/:id} : Updates an existing ordenes.
     *
     * @param id the id of the ordenesDTO to save.
     * @param ordenesDTO the ordenesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenesDTO,
     * or with status {@code 400 (Bad Request)} if the ordenesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordenesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordenes/{id}")
    public ResponseEntity<OrdenesDTO> updateOrdenes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdenesDTO ordenesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ordenes : {}, {}", id, ordenesDTO);
        if (ordenesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdenesDTO result = ordenesService.update(ordenesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordenes/:id} : Partial updates given fields of an existing ordenes, field will ignore if it is null
     *
     * @param id the id of the ordenesDTO to save.
     * @param ordenesDTO the ordenesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenesDTO,
     * or with status {@code 400 (Bad Request)} if the ordenesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ordenesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordenesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordenes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdenesDTO> partialUpdateOrdenes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdenesDTO ordenesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ordenes partially : {}, {}", id, ordenesDTO);
        if (ordenesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdenesDTO> result = ordenesService.partialUpdate(ordenesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ordenes} : get all the ordenes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordenes in body.
     */
    @GetMapping("/ordenes")
    public ResponseEntity<List<OrdenesDTO>> getAllOrdenes(
        OrdenesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Ordenes by criteria: {}", criteria);
        Page<OrdenesDTO> page = ordenesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordenes/count} : count all the ordenes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ordenes/count")
    public ResponseEntity<Long> countOrdenes(OrdenesCriteria criteria) {
        log.debug("REST request to count Ordenes by criteria: {}", criteria);
        return ResponseEntity.ok().body(ordenesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ordenes/:id} : get the "id" ordenes.
     *
     * @param id the id of the ordenesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordenesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordenes/{id}")
    public ResponseEntity<OrdenesDTO> getOrdenes(@PathVariable Long id) {
        log.debug("REST request to get Ordenes : {}", id);
        Optional<OrdenesDTO> ordenesDTO = ordenesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordenesDTO);
    }

    /**
     * {@code DELETE  /ordenes/:id} : delete the "id" ordenes.
     *
     * @param id the id of the ordenesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordenes/{id}")
    public ResponseEntity<Void> deleteOrdenes(@PathVariable Long id) {
        log.debug("REST request to delete Ordenes : {}", id);
        ordenesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
