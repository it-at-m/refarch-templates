package de.muenchen.refarch.theentity;

import static de.muenchen.refarch.common.ExceptionMessageConstants.MSG_NOT_FOUND;

import de.muenchen.refarch.common.NotFoundException;
import de.muenchen.refarch.security.Authorities;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TheEntityService {

    private final TheEntityRepository theEntityRepository;

    @PreAuthorize(Authorities.THEENTITY_GET)
    public TheEntity getTheEntity(final UUID theEntityId) {
        log.info("Get TheEntity with ID {}", theEntityId);
        return getEntityOrThrowException(theEntityId);
    }

    @PreAuthorize(Authorities.THEENTITY_GET_ALL)
    public Page<TheEntity> getAllEntities(final int pageNumber, final int pageSize) {
        log.info("Get all TheEntity with at Page {} with a PageSize of {}", pageNumber, pageSize);
        final Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return theEntityRepository.findAll(pageRequest);
    }

    @PreAuthorize(Authorities.THEENTITY_CREATE)
    public TheEntity createTheEntity(final TheEntity entity) {
        log.debug("Create TheEntity {}", entity);
        return theEntityRepository.save(entity);
    }

    @PreAuthorize(Authorities.THEENTITY_UPDATE)
    public TheEntity updateTheEntity(final TheEntity entity, final UUID theEntityId) {
        final TheEntity foundEntity = getEntityOrThrowException(theEntityId);
        foundEntity.setTextAttribute(entity.getTextAttribute());
        log.debug("Update TheEntity {}", foundEntity);
        return theEntityRepository.save(foundEntity);
    }

    @PreAuthorize(Authorities.THEENTITY_DELETE)
    public void deleteTheEntity(final UUID theEntityId) {
        log.debug("Delete TheEntity with ID {}", theEntityId);
        theEntityRepository.deleteById(theEntityId);
    }

    private TheEntity getEntityOrThrowException(final UUID theEntityId) {
        return theEntityRepository
                .findById(theEntityId)
                .orElseThrow(() -> new NotFoundException(String.format(MSG_NOT_FOUND, theEntityId)));
    }
}
