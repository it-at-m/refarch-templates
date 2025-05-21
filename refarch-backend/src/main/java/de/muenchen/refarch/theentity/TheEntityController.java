package de.muenchen.refarch.theentity;

import de.muenchen.refarch.theentity.dto.TheEntityMapper;
import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import de.muenchen.refarch.theentity.dto.TheEntityResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "the_entity_example_tag", description = "An example tag for the entity controller")
@RequestMapping("/theEntity")
public class TheEntityController {

    private final TheEntityService theEntityService;
    private final TheEntityMapper theEntityMapper;

    @GetMapping("{theEntityID}")
    @Operation(summary = "Retrieve an entity by its UUID", description = "Fetches the entity details using the provided UUID.")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityResponseDTO getTheEntity(@PathVariable("theEntityID") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.getTheEntity(theEntityId));
    }

    @GetMapping
    @Operation(summary = "Retrieve entities with pagination", description = "Fetches a paginated list of entities based on the provided page number and size.")
    @ResponseStatus(HttpStatus.OK)
    public Page<TheEntityResponseDTO> getTheEntitiesByPageAndSize(@RequestParam(defaultValue = "0") final int pageNumber,
            @RequestParam(defaultValue = "10") final int pageSize) {
        final Page<TheEntity> pageWithEntity = theEntityService.getAllEntities(pageNumber, pageSize);
        final List<TheEntityResponseDTO> theEntityRequestDTOList = pageWithEntity.getContent().stream().map(theEntityMapper::toDTO).toList();
        return new PageImpl<>(theEntityRequestDTOList, pageWithEntity.getPageable(), pageWithEntity.getTotalElements());
    }

    @PostMapping
    @Operation(summary = "Create a new entity", description = "Creates a new entity using the provided entity details.")
    @ResponseStatus(HttpStatus.CREATED)
    public TheEntityResponseDTO saveTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO) {
        return theEntityMapper.toDTO(theEntityService.createTheEntity(theEntityMapper.toEntity(theEntityRequestDTO)));
    }

    @PutMapping("/{theEntityId}")
    @Operation(summary = "Update an existing entity", description = "Updates the details of an existing entity using the provided UUID and entity details.")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityResponseDTO updateTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO,
            @PathVariable("theEntityId") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.updateTheEntity(theEntityMapper.toEntity(theEntityRequestDTO), theEntityId));
    }

    @DeleteMapping("/{theEntityId}")
    @Operation(summary = "Delete an entity", description = "Deletes the entity using the provided UUID.")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTheEntity(@PathVariable("theEntityId") final UUID theEntityId) {
        theEntityService.deleteTheEntity(theEntityId);
    }

}
