package fran.procesador.orden.service;

import fran.procesador.orden.domain.Operacion;
import fran.procesador.orden.repository.OperacionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Operacion}.
 */
@Service
@Transactional
public class OperacionService {

    private final Logger log = LoggerFactory.getLogger(OperacionService.class);

    private final OperacionRepository operacionRepository;

    public OperacionService(OperacionRepository operacionRepository) {
        this.operacionRepository = operacionRepository;
    }

    /**
     * Save a operacion.
     *
     * @param operacion the entity to save.
     * @return the persisted entity.
     */
    public Operacion save(Operacion operacion) {
        log.debug("Request to save Operacion : {}", operacion);
        return operacionRepository.save(operacion);
    }

    /**
     * Update a operacion.
     *
     * @param operacion the entity to save.
     * @return the persisted entity.
     */
    public Operacion update(Operacion operacion) {
        log.debug("Request to update Operacion : {}", operacion);
        return operacionRepository.save(operacion);
    }

    /**
     * Partially update a operacion.
     *
     * @param operacion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Operacion> partialUpdate(Operacion operacion) {
        log.debug("Request to partially update Operacion : {}", operacion);

        return operacionRepository
            .findById(operacion.getId())
            .map(existingOperacion -> {
                if (operacion.getCliente() != null) {
                    existingOperacion.setCliente(operacion.getCliente());
                }
                if (operacion.getAccionId() != null) {
                    existingOperacion.setAccionId(operacion.getAccionId());
                }
                if (operacion.getAccion() != null) {
                    existingOperacion.setAccion(operacion.getAccion());
                }
                if (operacion.getPrecio() != null) {
                    existingOperacion.setPrecio(operacion.getPrecio());
                }
                if (operacion.getCantidad() != null) {
                    existingOperacion.setCantidad(operacion.getCantidad());
                }
                if (operacion.getFechaOperacion() != null) {
                    existingOperacion.setFechaOperacion(operacion.getFechaOperacion());
                }
                if (operacion.getModo() != null) {
                    existingOperacion.setModo(operacion.getModo());
                }
                if (operacion.getOperacionExitosa() != null) {
                    existingOperacion.setOperacionExitosa(operacion.getOperacionExitosa());
                }
                if (operacion.getOperacionObservaciones() != null) {
                    existingOperacion.setOperacionObservaciones(operacion.getOperacionObservaciones());
                }

                return existingOperacion;
            })
            .map(operacionRepository::save);
    }

    /**
     * Get all the operacions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Operacion> findAll() {
        log.debug("Request to get all Operacions");
        return operacionRepository.findAll();
    }

    /**
     *  Get all the operacions where Orden is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Operacion> findAllWhereOrdenIsNull() {
        log.debug("Request to get all operacions where Orden is null");
        return StreamSupport
            .stream(operacionRepository.findAll().spliterator(), false)
            .filter(operacion -> operacion.getOrden() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one operacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Operacion> findOne(Long id) {
        log.debug("Request to get Operacion : {}", id);
        return operacionRepository.findById(id);
    }

    /**
     * Delete the operacion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Operacion : {}", id);
        operacionRepository.deleteById(id);
    }
}
