package fran.um.edu.ar.service.impl;

import fran.um.edu.ar.domain.Ordenes;
import fran.um.edu.ar.repository.OrdenesRepository;
import fran.um.edu.ar.service.OrdenesService;
import fran.um.edu.ar.service.dto.OrdenesDTO;
import fran.um.edu.ar.service.mapper.OrdenesMapper;
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
public class OrdenesServiceImpl implements OrdenesService {

    private final Logger log = LoggerFactory.getLogger(OrdenesServiceImpl.class);

    private final OrdenesRepository ordenesRepository;

    private final OrdenesMapper ordenesMapper;

    public OrdenesServiceImpl(OrdenesRepository ordenesRepository, OrdenesMapper ordenesMapper) {
        this.ordenesRepository = ordenesRepository;
        this.ordenesMapper = ordenesMapper;
    }

    @Override
    public OrdenesDTO save(OrdenesDTO ordenesDTO) {
        log.debug("Request to save Ordenes : {}", ordenesDTO);
        Ordenes ordenes = ordenesMapper.toEntity(ordenesDTO);
        ordenes = ordenesRepository.save(ordenes);
        return ordenesMapper.toDto(ordenes);
    }

    @Override
    public OrdenesDTO update(OrdenesDTO ordenesDTO) {
        log.debug("Request to update Ordenes : {}", ordenesDTO);
        Ordenes ordenes = ordenesMapper.toEntity(ordenesDTO);
        ordenes = ordenesRepository.save(ordenes);
        return ordenesMapper.toDto(ordenes);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ordenes");
        return ordenesRepository.findAll(pageable).map(ordenesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenesDTO> findOne(Long id) {
        log.debug("Request to get Ordenes : {}", id);
        return ordenesRepository.findById(id).map(ordenesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ordenes : {}", id);
        ordenesRepository.deleteById(id);
    }
}
