package fran.um.edu.ar.service;

import fran.um.edu.ar.domain.*; // for static metamodels
import fran.um.edu.ar.domain.Ordenes;
import fran.um.edu.ar.repository.OrdenesRepository;
import fran.um.edu.ar.service.criteria.OrdenesCriteria;
import fran.um.edu.ar.service.dto.OrdenesDTO;
import fran.um.edu.ar.service.mapper.OrdenesMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ordenes} entities in the database.
 * The main input is a {@link OrdenesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrdenesDTO} or a {@link Page} of {@link OrdenesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrdenesQueryService extends QueryService<Ordenes> {

    private final Logger log = LoggerFactory.getLogger(OrdenesQueryService.class);

    private final OrdenesRepository ordenesRepository;

    private final OrdenesMapper ordenesMapper;

    public OrdenesQueryService(OrdenesRepository ordenesRepository, OrdenesMapper ordenesMapper) {
        this.ordenesRepository = ordenesRepository;
        this.ordenesMapper = ordenesMapper;
    }

    /**
     * Return a {@link List} of {@link OrdenesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrdenesDTO> findByCriteria(OrdenesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ordenes> specification = createSpecification(criteria);
        return ordenesMapper.toDto(ordenesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrdenesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrdenesDTO> findByCriteria(OrdenesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ordenes> specification = createSpecification(criteria);
        return ordenesRepository.findAll(specification, page).map(ordenesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrdenesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ordenes> specification = createSpecification(criteria);
        return ordenesRepository.count(specification);
    }

    /**
     * Function to convert {@link OrdenesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ordenes> createSpecification(OrdenesCriteria criteria) {
        Specification<Ordenes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ordenes_.id));
            }
            if (criteria.getCliente() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCliente(), Ordenes_.cliente));
            }
            if (criteria.getAccionId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccionId(), Ordenes_.accionId));
            }
            if (criteria.getAccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccion(), Ordenes_.accion));
            }
            if (criteria.getOperacion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOperacion(), Ordenes_.operacion));
            }
            if (criteria.getPrecio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecio(), Ordenes_.precio));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), Ordenes_.cantidad));
            }
            if (criteria.getFechaOperacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaOperacion(), Ordenes_.fechaOperacion));
            }
            if (criteria.getModo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModo(), Ordenes_.modo));
            }
        }
        return specification;
    }
}
