package de.muenchen.oss.refarch.backend.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This class represents an auditable entity using Spring JPA Auditing
 * and allows storage of creation and modification timestamps and usernames.
 *
 * <p>
 * <strong>Note:</strong> A
 * {@link de.muenchen.oss.refarch.backend.configuration.JPAAuditingConfiguration} is required in
 * order to function correctly.
 * </p>
 *
 * @see <a href=
 *      "https://docs.spring.io/spring-data/jpa/reference/auditing.html">https://docs.spring.io/spring-data/jpa/reference/auditing.html</a>
 */
@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
