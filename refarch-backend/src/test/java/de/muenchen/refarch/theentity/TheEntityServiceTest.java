package de.muenchen.refarch.theentity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.refarch.common.NotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TheEntityServiceTest {
    private static final String DEFAULT_TEXTATTRIBUT = "rand";

    @Mock
    private TheEntityRepository theEntityRepository;

    @InjectMocks
    private TheEntityService unitUnderTest;

    @Nested
    class GetTheEntity {
        @Test
        void givenUUID_thenReturnEntity() {
            // Given
            final UUID id = UUID.randomUUID();
            final TheEntity theEntity = new TheEntity();
            theEntity.setId(id);
            theEntity.setTextAttribute(DEFAULT_TEXTATTRIBUT);
            when(theEntityRepository.findById(id)).thenReturn(Optional.of(theEntity));

            // When
            final TheEntity result = unitUnderTest.getTheEntity(id);

            // Then
            verify(theEntityRepository).findById(id);
            assertThat(result).usingRecursiveComparison().isEqualTo(theEntity);
        }

        @Test
        void givenNonExistentUUID_thenThrowNotFoundException() {
            // Given
            final UUID id = UUID.randomUUID();
            when(theEntityRepository.findById(id)).thenReturn(Optional.empty());

            // When
            final Exception exception = Assertions.assertThrows(NotFoundException.class, () -> unitUnderTest.getTheEntity(id));

            // Then
            verify(theEntityRepository).findById(id);
            Assertions.assertEquals(exception.getClass(), NotFoundException.class);
            Assertions.assertEquals(exception.getMessage(), String.format("404 NOT_FOUND \"Could not find entity with id %s\"", id));
        }
    }

    @Nested
    class GetEntitiesPage {
        @Test
        void givenPageNumberAndPageSize_thenReturnPageOfEntities() {
            // Given
            final int pageNumber = 0;
            final int pageSize = 10;
            final Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

            final TheEntity entity1 = new TheEntity();
            final TheEntity entity2 = new TheEntity();
            final List<TheEntity> entities = Arrays.asList(entity1, entity2);
            final Page<TheEntity> expectedPage = new PageImpl<>(entities, pageRequest, entities.size());

            when(theEntityRepository.findAll(pageRequest)).thenReturn(expectedPage);

            // When
            final Page<TheEntity> result = unitUnderTest.getAllEntities(pageNumber, pageSize);

            // Then
            Assertions.assertEquals(expectedPage, result);
            verify(theEntityRepository, times(1)).findAll(pageRequest);
        }
    }

    @Nested
    class SaveTheEntity {
        @Test
        void givenTheEntity_thenReturnEntity() {
            // Given
            final TheEntity entityToSave = new TheEntity();
            entityToSave.setTextAttribute(DEFAULT_TEXTATTRIBUT);

            final TheEntity expectedEntity = new TheEntity();
            expectedEntity.setId(UUID.randomUUID());
            expectedEntity.setTextAttribute(DEFAULT_TEXTATTRIBUT);

            when(theEntityRepository.save(entityToSave)).thenReturn(expectedEntity);

            // When
            final TheEntity result = unitUnderTest.createTheEntity(entityToSave);

            // Then
            assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedEntity);
            verify(theEntityRepository).save(entityToSave);
        }
    }

    @Nested
    class UpdateTheEntity {
        @Test
        void givenTheEntity_thenReturnEntity() {
            // Given
            final TheEntity entityToUpdate = new TheEntity();
            final UUID entityToUpdateId = UUID.randomUUID();
            entityToUpdate.setId(entityToUpdateId);
            entityToUpdate.setTextAttribute(DEFAULT_TEXTATTRIBUT);
            final TheEntity expectedEntity = new TheEntity();
            expectedEntity.setId(entityToUpdateId);
            expectedEntity.setTextAttribute(DEFAULT_TEXTATTRIBUT);
            when(theEntityRepository.save(entityToUpdate)).thenReturn(expectedEntity);
            when(theEntityRepository.findById(entityToUpdateId)).thenReturn(Optional.of(entityToUpdate));

            // When
            final TheEntity result = unitUnderTest.updateTheEntity(entityToUpdate, entityToUpdateId);

            // Then
            assertThat(result).usingRecursiveComparison().isEqualTo(expectedEntity);
            verify(theEntityRepository).save(entityToUpdate);
        }

        @Test
        void givenTheEntity_thenThrowNotFoundException() {
            // Given
            final UUID entityToUpdateId = UUID.randomUUID();
            final TheEntity entityToUpdate = new TheEntity();
            entityToUpdate.setId(entityToUpdateId);
            entityToUpdate.setTextAttribute(DEFAULT_TEXTATTRIBUT);

            when(theEntityRepository.findById(entityToUpdate.getId())).thenReturn(Optional.empty());

            // When
            final Exception exception = Assertions.assertThrows(NotFoundException.class, () -> unitUnderTest.updateTheEntity(entityToUpdate, entityToUpdateId));

            // Then
            verify(theEntityRepository, times(1)).findById(entityToUpdate.getId());
            Assertions.assertEquals(exception.getClass(), NotFoundException.class);
            Assertions.assertEquals(exception.getMessage(), String.format("404 NOT_FOUND \"Could not find entity with id %s\"", entityToUpdateId));

        }
    }

    @Nested
    class DeleteTheEntity {
        @Test
        void givenTheEntityId_thenReturnVoid() {
            // Given
            final UUID entityToDeleteId = UUID.randomUUID();
            Mockito.doNothing().when(theEntityRepository).deleteById(entityToDeleteId);

            // When
            unitUnderTest.deleteTheEntity(entityToDeleteId);

            // Then
            verify(theEntityRepository).deleteById(entityToDeleteId);
        }
    }
}
