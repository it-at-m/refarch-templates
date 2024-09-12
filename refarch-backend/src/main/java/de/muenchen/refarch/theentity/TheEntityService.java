package de.muenchen.refarch.theentity;

import de.muenchen.refarch.common.exception.NotFoundException;
import de.muenchen.refarch.security.Authorities;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TheEntityService {
    private static final String MSG_NOT_FOUND = "Could not find entity with id %s";

    private final TheEntityRepository theEntityRepository;

    @PreAuthorize(Authorities.HAS_AUTHORITY_READ_THEENTITY)
    public TheEntity getTheEntity(final UUID theEntityId) {
        log.info("Get TheEntity with ID {}", theEntityId);
        return getEntityOrThrowException(theEntityId);
    }

    @PreAuthorize(Authorities.HAS_AUTHORITY_READ_THEENTITY)
    public Page<TheEntity> getAllEntities(final int pageNumber, final int pageSize) {
        log.info("Get AllEntities with at Page {} with a PageSize of {}", pageNumber, pageSize);
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        return theEntityRepository.findAll(pageRequest);
    }

    @PreAuthorize(Authorities.HAS_AUTHORITY_WRITE_THEENTITY)
    public TheEntity createTheEntity(final TheEntity entity) {
        log.debug("Create TheEntity {}", entity);
        return theEntityRepository.save(entity);
    }

    @PreAuthorize(Authorities.HAS_AUTHORITY_WRITE_THEENTITY)
    public TheEntity updateTheEntity(final TheEntity entity, final UUID theEntityId) {
        TheEntity foundEntity = getEntityOrThrowException(theEntityId);
        foundEntity.setId(entity.getId());
        foundEntity.setTextAttribute(entity.getTextAttribute());
        log.debug("Update TheEntity {}", foundEntity);
        return theEntityRepository.save(foundEntity);
    }

    @PreAuthorize(Authorities.HAS_AUTHORITY_DELETE_THEENTIT)
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
