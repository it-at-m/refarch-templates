package de.muenchen.refarch.theentity;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheEntityRepository extends PagingAndSortingRepository<TheEntity, UUID>, CrudRepository<TheEntity, UUID> {

}
