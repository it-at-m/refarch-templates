package de.muenchen.refarch.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.muenchen.refarch.MicroServiceApplication;
import de.muenchen.refarch.domain.TheEntity;

import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:refarch;DB_CLOSE_ON_EXIT=FALSE"
        }
)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class TheEntityRepositoryTest {

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
