package de.muenchen.refarch.example;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

/**
 * Provides a Repository for {@link TheEntity}. This Repository is exported as a REST resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring
 * Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@Repository
public interface TheEntityRepository extends PagingAndSortingRepository<TheEntity, UUID>, CrudRepository<TheEntity, UUID> { //NOSONAR

}
