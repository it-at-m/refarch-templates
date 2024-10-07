package de.muenchen.refarch.theentity;

import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import de.muenchen.refarch.theentity.dto.TheEntityMapper;
import de.muenchen.refarch.theentity.dto.TheEntityResponseDTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllArgsConstructor
class TheEntityMapperTest {

    private final TheEntityMapper theEntityMapper = Mappers.getMapper(TheEntityMapper.class);

    @Test
    void givenEntity_whenToDTO_thenReturnsCorrectDTO() {
        // Given
        UUID uuid = UUID.randomUUID();
        TheEntity theEntity = new TheEntity();
        theEntity.setId(uuid);
        theEntity.setTextAttribute("test");

        // When
        TheEntityResponseDTO result = theEntityMapper.toDTO(theEntity);

        // Then
        assertNotNull(result);
        assertEquals(uuid, result.id());
        assertEquals("test", result.textAttribute());
    }

    @Test
    void givenRequestDTO_whenToEntity_thenReturnsCorrectEntity() {
        // Given
        TheEntityRequestDTO requestDTO = new TheEntityRequestDTO("test");

        // When
        TheEntity result = theEntityMapper.toEntity(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals("test", result.getTextAttribute());
    }

}
