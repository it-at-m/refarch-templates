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

    @GetMapping("{theEntityID}")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityResponseDTO getTheEntity(@PathVariable("theEntityID") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.getTheEntity(theEntityId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TheEntityResponseDTO> getTheEntitiesByPageAndSize(@RequestParam(defaultValue = "0") final int pageNumber,
            @RequestParam(defaultValue = "10") final int pageSize) {
        final Page<TheEntity> pageWithEntity = theEntityService.getAllEntities(pageNumber, pageSize);
        final List<TheEntityResponseDTO> theEntityRequestDTOList = pageWithEntity.getContent().stream().map(theEntityMapper::toDTO).toList();
        return new PageImpl<>(theEntityRequestDTOList, pageWithEntity.getPageable(), pageWithEntity.getTotalElements());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TheEntityResponseDTO saveTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO) {
        return theEntityMapper.toDTO(theEntityService.createTheEntity(theEntityMapper.toEntity(theEntityRequestDTO)));
    }

    @PutMapping("/{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityResponseDTO updateTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO,
            @PathVariable("theEntityId") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.updateTheEntity(theEntityMapper.toEntity(theEntityRequestDTO), theEntityId));
    }

    @DeleteMapping("/{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTheEntity(@PathVariable("theEntityId") final UUID theEntityId) {
        theEntityService.deleteTheEntity(theEntityId);
    }

}
