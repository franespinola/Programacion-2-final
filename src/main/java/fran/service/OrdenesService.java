package fran.service;

import fran.domain.Ordenes;
import fran.repository.OrdenesRepository;
import fran.service.dto.OrdenesDTO;
import fran.service.mapper.OrdenesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ordenes}.
 */
@Service
@Transactional
public class OrdenesService {

    private final Logger log = LoggerFactory.getLogger(OrdenesService.class);

    private final OrdenesRepository ordenesRepository;

    private final OrdenesMapper ordenesMapper;

    public OrdenesService(OrdenesRepository ordenesRepository, OrdenesMapper ordenesMapper) {
        this.ordenesRepository = ordenesRepository;
        this.ordenesMapper = ordenesMapper;
    }

    /**
     * Save a ordenes.
     *
     * @param ordenesDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdenesDTO save(OrdenesDTO ordenesDTO) {
        log.debug("Request to save Ordenes : {}", ordenesDTO);
        Ordenes ordenes = ordenesMapper.toEntity(ordenesDTO);
        ordenes = ordenesRepository.save(ordenes);
        return ordenesMapper.toDto(ordenes);
    }

    /**
     * Update a ordenes.
     *
     * @param ordenesDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdenesDTO update(OrdenesDTO ordenesDTO) {
        log.debug("Request to update Ordenes : {}", ordenesDTO);
        Ordenes ordenes = ordenesMapper.toEntity(ordenesDTO);
        ordenes = ordenesRepository.save(ordenes);
        return ordenesMapper.toDto(ordenes);
    }

    /**
     * Partially update a ordenes.
     *
     * @param ordenesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrdenesDTO> partialUpdate(OrdenesDTO ordenesDTO) {
        log.debug("Request to partially update Ordenes : {}", ordenesDTO);

        return ordenesRepository
            .findById(ordenesDTO.getId())
            .map(existingOrdenes -> {
                ordenesMapper.partialUpdate(existingOrdenes, ordenesDTO);

                return existingOrdenes;
            })
            .map(ordenesRepository::save)
            .map(ordenesMapper::toDto);
    }

    /**
     * Get all the ordenes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdenesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ordenes");
        return ordenesRepository.findAll(pageable).map(ordenesMapper::toDto);
    }

    /**
     * Get one ordenes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrdenesDTO> findOne(Long id) {
        log.debug("Request to get Ordenes : {}", id);
        return ordenesRepository.findById(id).map(ordenesMapper::toDto);
    }

    /**
     * Delete the ordenes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ordenes : {}", id);
        ordenesRepository.deleteById(id);
    }
}
