package de.muenchen.refarch.rest;

import de.muenchen.refarch.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.muenchen.refarch.MicroServiceApplication;
import de.muenchen.refarch.domain.TheEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class TheEntityRepositoryTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    @Autowired
    private TheEntityRepository repository;

    @Test
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    void testSave() {
        // initialize
        TheEntity original = new TheEntity();
        original.setTextAttribute("test");

        // persist
        original = repository.save(original);

        // check
        final TheEntity persisted = repository.findById(original.getId()).orElse(null);
        assertNotNull(persisted);
        assertEquals(original, persisted);
    }

}
