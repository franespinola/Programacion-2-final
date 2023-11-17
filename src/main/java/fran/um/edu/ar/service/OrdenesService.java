package fran.um.edu.ar.service;

import fran.um.edu.ar.service.dto.OrdenesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fran.um.edu.ar.domain.Ordenes}.
 */
public interface OrdenesService {
    /**
     * Save a ordenes.
     *
     * @param ordenesDTO the entity to save.
     * @return the persisted entity.
     */
    OrdenesDTO save(OrdenesDTO ordenesDTO);

    /**
     * Updates a ordenes.
     *
     * @param ordenesDTO the entity to update.
     * @return the persisted entity.
     */
    OrdenesDTO update(OrdenesDTO ordenesDTO);

    /**
     * Partially updates a ordenes.
     *
     * @param ordenesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdenesDTO> partialUpdate(OrdenesDTO ordenesDTO);

    /**
     * Get all the ordenes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrdenesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ordenes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdenesDTO> findOne(Long id);

    /**
     * Delete the "id" ordenes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
