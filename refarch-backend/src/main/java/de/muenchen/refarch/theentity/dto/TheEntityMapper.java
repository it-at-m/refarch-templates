package de.muenchen.refarch.theentity.dto;

import de.muenchen.refarch.theentity.TheEntity;
import org.mapstruct.Mapper;

@Mapper
public interface TheEntityMapper {

    TheEntityResponseDTO toDTO(TheEntity theEntity);

    TheEntity toEntity(TheEntityRequestDTO theEntityRequestDTO);
}
