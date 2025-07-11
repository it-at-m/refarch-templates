package de.muenchen.refarch.theentity;

import de.muenchen.refarch.theentity.dto.TheEntityMapper;
import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import de.muenchen.refarch.theentity.dto.TheEntityResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/theEntity")
public class TheEntityController {

    private final TheEntityService theEntityService;
    private final TheEntityMapper theEntityMapper;

    /**
     * Retrieve an entity by its UUID.
     * Fetches the entity details using the provided UUID.
     *
     * @param theEntityId the UUID of the requested entity
     * @return the entity with the given UID as a DTO
     */
    @GetMapping("{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityResponseDTO getTheEntity(@PathVariable("theEntityId") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.getTheEntity(theEntityId));
    }

    /**
     * Retrieve entities with pagination.
     * Fetches a paginated list of entities based on the provided page number and size.
     *
     * @param pageNumber the number of the requested page (default: 0)
     * @param pageSize the size of the page to retrieve (default: 10)
     * @return a page of entities represented as DTOs
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TheEntityResponseDTO> getTheEntitiesByPageAndSize(@RequestParam(defaultValue = "0") final int pageNumber,
            @RequestParam(defaultValue = "10") final int pageSize) {
        final Page<TheEntity> pageWithEntity = theEntityService.getAllEntities(pageNumber, pageSize);
        final List<TheEntityResponseDTO> theEntityRequestDTOList = pageWithEntity.getContent().stream().map(theEntityMapper::toDTO).toList();
        return new PageImpl<>(theEntityRequestDTOList, pageWithEntity.getPageable(), pageWithEntity.getTotalElements());
    }

    /**
     * Create a new entity.
     * Creates a new entity using the provided entity details.
     *
     * @param theEntityRequestDTO the details of the entity to create
     * @return the created entity as a DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TheEntityResponseDTO saveTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO) {
        return theEntityMapper.toDTO(theEntityService.createTheEntity(theEntityMapper.toEntity(theEntityRequestDTO)));
    }

    /**
     * Update an existing entity.
     * Updates the details of an existing entity using the provided UUID and entity details.
     *
     * @param theEntityRequestDTO the new details of the entity
     * @param theEntityId the UUID of the entity to update
     * @return the updated entity as a DTO
     */
    @PutMapping("/{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityResponseDTO updateTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO,
            @PathVariable("theEntityId") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.updateTheEntity(theEntityMapper.toEntity(theEntityRequestDTO), theEntityId));
    }

    /**
     * Delete an entity.
     * Deletes the entity using the provided UUID.
     *
     * @param theEntityId the UUID of the entity to delete
     */
    @DeleteMapping("/{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTheEntity(@PathVariable("theEntityId") final UUID theEntityId) {
        theEntityService.deleteTheEntity(theEntityId);
    }

}
