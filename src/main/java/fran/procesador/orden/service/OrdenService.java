package fran.procesador.orden.service;

import fran.procesador.orden.domain.Orden;
import fran.procesador.orden.repository.OrdenRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Orden}.
 */
@Service
@Transactional
public class OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenService.class);

    private final OrdenRepository ordenRepository;

    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    /**
     * Save a orden.
     *
     * @param orden the entity to save.
     * @return the persisted entity.
     */
    public Orden save(Orden orden) {
        log.debug("Request to save Orden : {}", orden);
        return ordenRepository.save(orden);
    }

    /**
     * Update a orden.
     *
     * @param orden the entity to save.
     * @return the persisted entity.
     */
    public Orden update(Orden orden) {
        log.debug("Request to update Orden : {}", orden);
        return ordenRepository.save(orden);
    }

    /**
     * Partially update a orden.
     *
     * @param orden the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Orden> partialUpdate(Orden orden) {
        log.debug("Request to partially update Orden : {}", orden);

        return ordenRepository
            .findById(orden.getId())
            .map(existingOrden -> {
                if (orden.getCliente() != null) {
                    existingOrden.setCliente(orden.getCliente());
                }
                if (orden.getAccionId() != null) {
                    existingOrden.setAccionId(orden.getAccionId());
                }
                if (orden.getAccion() != null) {
                    existingOrden.setAccion(orden.getAccion());
                }
                if (orden.getPrecio() != null) {
                    existingOrden.setPrecio(orden.getPrecio());
                }
                if (orden.getCantidad() != null) {
                    existingOrden.setCantidad(orden.getCantidad());
                }
                if (orden.getFechaOperacion() != null) {
                    existingOrden.setFechaOperacion(orden.getFechaOperacion());
                }
                if (orden.getModo() != null) {
                    existingOrden.setModo(orden.getModo());
                }

                return existingOrden;
            })
            .map(ordenRepository::save);
    }

    /**
     * Get all the ordens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Orden> findAll() {
        log.debug("Request to get all Ordens");
        return ordenRepository.findAll();
    }

    /**
     * Get one orden by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Orden> findOne(Long id) {
        log.debug("Request to get Orden : {}", id);
        return ordenRepository.findById(id);
    }

    /**
     * Delete the orden by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
    }
}
