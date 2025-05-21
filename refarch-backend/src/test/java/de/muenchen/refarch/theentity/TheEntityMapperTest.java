package de.muenchen.refarch.theentity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muenchen.refarch.theentity.dto.TheEntityMapper;
import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import de.muenchen.refarch.theentity.dto.TheEntityResponseDTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@AllArgsConstructor
class TheEntityMapperTest {

    private final TheEntityMapper theEntityMapper = Mappers.getMapper(TheEntityMapper.class);

    @Nested
    class ToDTO {
        @Test
        void givenEntity_thenReturnsCorrectDTO() {
            // Given
            final UUID uuid = UUID.randomUUID();
            final TheEntity theEntity = new TheEntity();
            theEntity.setId(uuid);
            theEntity.setTextAttribute("test");

            // When
            final TheEntityResponseDTO result = theEntityMapper.toDTO(theEntity);

            // Then
            assertNotNull(result);
            assertThat(result).usingRecursiveComparison().isEqualTo(theEntity);
        }
    }

    @Nested
    class ToEntity {
        @Test
        void givenRequestDTO_thenReturnsCorrectEntity() {
            // Given
            final TheEntityRequestDTO requestDTO = new TheEntityRequestDTO("test");

            // When
            final TheEntity result = theEntityMapper.toEntity(requestDTO);

            // Then
            assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(requestDTO);
        }
    }

}
