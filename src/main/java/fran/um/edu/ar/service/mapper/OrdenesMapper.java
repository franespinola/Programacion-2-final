package fran.um.edu.ar.service.mapper;

import fran.um.edu.ar.domain.Ordenes;
import fran.um.edu.ar.service.dto.OrdenesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ordenes} and its DTO {@link OrdenesDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdenesMapper extends EntityMapper<OrdenesDTO, Ordenes> {}
