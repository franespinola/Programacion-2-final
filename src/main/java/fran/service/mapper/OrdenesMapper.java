package fran.service.mapper;

import fran.domain.Ordenes;
import fran.service.dto.OrdenesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ordenes} and its DTO {@link OrdenesDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdenesMapper extends EntityMapper<OrdenesDTO, Ordenes> {}
