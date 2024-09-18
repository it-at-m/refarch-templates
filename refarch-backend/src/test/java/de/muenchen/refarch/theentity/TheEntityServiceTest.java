package de.muenchen.refarch.theentity;

import de.muenchen.refarch.common.exception.NotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TheEntityServiceTest {

    @Mock
    private TheEntityRepository theEntityRepository;

    @InjectMocks
    private TheEntityService unitUnderTest;

    @Test
    void givenUUID_whenFindByID_thenReturnEntity() {
        // Given
        UUID id = UUID.randomUUID();
        TheEntity theEntity = new TheEntity();
        theEntity.setId(id);
        theEntity.setTextAttribute("rand");
        when(theEntityRepository.findById(id)).thenReturn(Optional.of(theEntity));

        // When
        TheEntity result = unitUnderTest.getTheEntity(id);

        // Then
        verify(theEntityRepository).findById(id);
        assertThat(result).usingRecursiveComparison().isEqualTo(theEntity);
    }

    @Test
    void givenNonExistentUUID_whenFindById_thenThrowNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        when(theEntityRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> unitUnderTest.getTheEntity(id));

        // Then
        verify(theEntityRepository).findById(id);
        Assertions.assertEquals(exception.getClass(), NotFoundException.class);
        Assertions.assertEquals(exception.getMessage(), String.format("404 NOT_FOUND \"Could not find entity with id %s\"", id));
    }

    @Test
    void givenPageNumberAndPageSize_whenGetAllEntities_thenReturnPageOfEntities() {
        // Given
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);

        TheEntity entity1 = new TheEntity();
        TheEntity entity2 = new TheEntity();
        List<TheEntity> entities = Arrays.asList(entity1, entity2);
        Page<TheEntity> expectedPage = new PageImpl<>(entities, pageRequest, entities.size());

        when(theEntityRepository.findAll(pageRequest)).thenReturn(expectedPage);

        // When
        Page<TheEntity> result = unitUnderTest.getAllEntities(pageNumber, pageSize);

        // Then
        Assertions.assertEquals(expectedPage, result);
        verify(theEntityRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void givenTheEntity_whenSaveNewEntity_thenReturnEntity() {
        // Given
        TheEntity entityToSave = new TheEntity();
        entityToSave.setTextAttribute("rand");

        TheEntity expectedEntity = new TheEntity();
        expectedEntity.setId(UUID.randomUUID());
        expectedEntity.setTextAttribute("rand");

        when(theEntityRepository.save(entityToSave)).thenReturn(expectedEntity);

        // When
        TheEntity result = unitUnderTest.createTheEntity(entityToSave);

        // Then
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedEntity);
        verify(theEntityRepository).save(entityToSave);
    }

    @Test
    void givenTheEntity_whenUpdateEntity_thenReturnEntity() {
        // Given
        TheEntity entityToUpdate = new TheEntity();
        UUID entityToUpdateId = UUID.randomUUID();
        entityToUpdate.setId(entityToUpdateId);
        entityToUpdate.setTextAttribute("rand");
        TheEntity expectedEntity = new TheEntity();
        expectedEntity.setId(entityToUpdateId);
        expectedEntity.setTextAttribute("rand");
        when(theEntityRepository.save(entityToUpdate)).thenReturn(expectedEntity);
        when(this.theEntityRepository.findById(entityToUpdateId)).thenReturn(Optional.of(entityToUpdate));

        // When
        TheEntity result = unitUnderTest.updateTheEntity(entityToUpdate, entityToUpdateId);

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedEntity);
        verify(theEntityRepository).save(entityToUpdate);
    }

    @Test
    void givenTheEntity_whenUpdateEntity_thenThrowNotFoundException() {
        // Given
        UUID entityToUpdateId = UUID.randomUUID();
        TheEntity entityToUpdate = new TheEntity();
        entityToUpdate.setId(entityToUpdateId);
        entityToUpdate.setTextAttribute("rand");

        when(theEntityRepository.findById(entityToUpdate.getId())).thenReturn(Optional.empty());

        // When
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> unitUnderTest.updateTheEntity(entityToUpdate, entityToUpdateId));

        // Then
        verify(theEntityRepository, times(1)).findById(entityToUpdate.getId());
        Assertions.assertEquals(exception.getClass(), NotFoundException.class);
        Assertions.assertEquals(exception.getMessage(), String.format("404 NOT_FOUND \"Could not find entity with id %s\"", entityToUpdateId));

    }

    @Test
    void givenTheEntityId_whenDeleteEntity_thenReturnVoid() {
        // Given
        UUID entityToDeleteId = UUID.randomUUID();
        Mockito.doNothing().when(theEntityRepository).deleteById(entityToDeleteId);

        // When
        unitUnderTest.deleteTheEntity(entityToDeleteId);

        // Then
        verify(theEntityRepository).deleteById(entityToDeleteId);
    }


}