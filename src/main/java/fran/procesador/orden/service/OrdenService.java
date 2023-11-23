package fran.procesador.orden.service;

import fran.procesador.orden.domain.Operacion;
import fran.procesador.orden.domain.Orden;
import fran.procesador.orden.repository.OrdenRepository;
import fran.procesador.orden.service.dto.ListaDeOrdenesExternas;
import fran.procesador.orden.service.dto.OrdenExternaDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * Service Implementation for managing {@link Orden}.
 */
@Service
@Transactional
public class OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenService.class);

    private final OrdenRepository ordenRepository;

    private final OperacionService operacionService;

    public OrdenService(OrdenRepository ordenRepository, OperacionService operacionService) {
        this.ordenRepository = ordenRepository;
        this.operacionService = operacionService;
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

    public List<Operacion> procesarOrdenesPendiente() {
        // 1. Buscar ordenes en el servicio externo de catedra
        List<OrdenExternaDTO> ordenesPendientes = buscarTodasLasOrdenes();

        List<Operacion> resultadoOperaciones = new ArrayList<>();

        //2. Procesar las ordenes
        for (OrdenExternaDTO o : ordenesPendientes) {
            // 2.1 Busco si existe el cliente
            boolean existeCliente = buscarUsuarioEnElServicio(o.getCliente());

            Operacion operacion = new Operacion();
            operacion.setFechaOperacion(Instant.now());

            if (existeCliente) { // Si es verdadero significa que existe el cliente
                // Aca chequeaste todo y andubo perfecto
                operacion.setOperacionExitosa(true);
            } else { // Significa que el cliente no existe, creamos operacion fallida
                operacion.setOperacionExitosa(false);
            }

            Orden orden = new Orden();
            orden.setCliente(o.getCliente());
            orden.setAccion(o.getAccion());
            orden.setModo(o.getModo().name());
            orden.setPrecio(o.getPrecio());
            orden.setFechaOperacion(o.getFechaOperacion());
            orden.setCantidad(o.getCantidad());
            operacion.setOrden(save(orden));

            // Guardamos la operacion exitosa en la base de datos
            operacionService.save(operacion);

            // Agregamos la operacion fallida a la lista de operaciones que le vamos a devolver al usuario
            resultadoOperaciones.add(operacion);
        }

        return resultadoOperaciones;
    }

    /**
     * Metodo para traer la lista de ordenes del servicio de catedras.
     */
    private List<OrdenExternaDTO> buscarTodasLasOrdenes() {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://192.168.194.254:8000/";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmcmFuZXNwaW5vbGEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNzMwOTcwNDMwfQ.H9oPo335mVdfNwQjQVCzFJZOlPqWijdqFI8eDFalB9bWXuJLINnpoSZwbqktOaHee_ynx4EALbexkOPt7QHVTg"
        );

        HttpEntity<String> request = new HttpEntity<String>("352823435", headers);

        ResponseEntity<ListaDeOrdenesExternas> response = restTemplate.exchange(
            baseUrl + "/api/ordenes/ordenes",
            HttpMethod.GET,
            request,
            ListaDeOrdenesExternas.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.debug("Se trajo la lista de ordenes de la cátedra. Se trajeron {} ordenes", response.getBody().getOrdenes().size());
            return response.getBody().getOrdenes();
        } else {
            log.error("No se pudo traer la lista de ordenes.");
        }

        return new ArrayList<>();
    }

    /**
     * Metodo para buscar si un usuario existe o no en el servicio de catedras
     */
    private boolean buscarUsuarioEnElServicio(int id) {
        if (id == 25) {
            return true;
        }

        return false;
    }
}
