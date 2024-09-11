package de.muenchen.refarch.example;

import de.muenchen.refarch.example.dto.TheEntityDTO;
import de.muenchen.refarch.example.dto.TheEntityMapper;
import jakarta.validation.constraints.NotEmpty;
import java.util.stream.Collectors;
import lombok.val;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@RequestMapping("/theEntity")
public class TheEntityController {

    TheEntityService theEntityService;
    TheEntityMapper theEntityMapper;

    @GetMapping("{theEntityID}")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityDTO getTheEntity(@PathVariable("theEntityID") final UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.getTheEntity(theEntityId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TheEntityDTO> getTheEntitiesByPageAndSize(@RequestParam(defaultValue = "0") final int pageNumber,
            @RequestParam(defaultValue = "10") final int pageSize) {
        Page<TheEntity> pageWithEntity = theEntityService.getAllEntities(pageNumber, pageSize);
        List<TheEntityDTO> theEntityDTOList = pageWithEntity.getContent().stream().map(theEntityMapper::toDTO).toList();
        return new PageImpl<>(theEntityDTOList, pageWithEntity.getPageable(), pageWithEntity.getTotalElements());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TheEntityDTO saveTheEntity(@RequestBody TheEntityDTO theEntityDTO) {
        return theEntityMapper.toDTO(theEntityService.createTheEntity(theEntityMapper.toEntity(theEntityDTO)));
    }

    @PutMapping("{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public TheEntityDTO updateTheEntity(@RequestBody TheEntityDTO theEntityDTO, @PathVariable("theEntityId") @NotEmpty UUID theEntityId) {
        return theEntityMapper.toDTO(theEntityService.updateTheEntity(theEntityMapper.toEntity(theEntityDTO), theEntityId));
    }

    @DeleteMapping("{theEntityId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTheEntity(@PathVariable("theEntityId") @NotEmpty UUID theEntityId) {
        theEntityService.deleteTheEntity(theEntityId);
    }

}
