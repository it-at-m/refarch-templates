package de.muenchen.refarch.example.dto;

import de.muenchen.refarch.example.TheEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TheEntityMapper {

    TheEntityDTO toDTO(TheEntity theEntity);

    @Mapping(target = "id", ignore = true)
    TheEntity toEntity(TheEntityDTO theEntityDTO);
}
