package de.muenchen.refarch.configuration;

import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muenchen.refarch.MicroServiceApplication;
import de.muenchen.refarch.TestConstants;
import de.muenchen.refarch.theentity.TheEntity;
import de.muenchen.refarch.theentity.TheEntityRepository;
import de.muenchen.refarch.theentity.dto.TheEntityRequestDTO;
import de.muenchen.refarch.theentity.dto.TheEntityResponseDTO;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class UnicodeConfigurationTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    private static final String ENTITY_ENDPOINT_URL = "/theEntity";

    /**
     * Decomposed string:
     * String "Ä-é" represented with unicode letters "A◌̈-e◌́"
     */
    private static final String TEXT_ATTRIBUTE_DECOMPOSED = "\u0041\u0308-\u0065\u0301";

    /**
     * Composed string:
     * String "Ä-é" represented with unicode letters "Ä-é".
     */
    private static final String TEXT_ATTRIBUTE_COMPOSED = "\u00c4-\u00e9";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TheEntityRepository theEntityRepository;

    @Test
    void testForNfcNormalization() {
        // Given
        // Persist entity with decomposed string.
        final TheEntityRequestDTO theEntityRequestDto = new TheEntityRequestDTO(TEXT_ATTRIBUTE_DECOMPOSED);

        // When
        final TheEntityResponseDTO response = testRestTemplate.postForEntity(URI.create(ENTITY_ENDPOINT_URL), theEntityRequestDto, TheEntityResponseDTO.class)
                .getBody();
        final TheEntity theEntity = theEntityRepository.findById(response.id()).orElse(null);

        // Then
        // Check whether response contains a composed string.
        assertNotNull(response.textAttribute());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, response.textAttribute());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED.length(), response.textAttribute().length());

        // Check persisted entity contains a composed string via JPA repository.
        assertNotNull(theEntity.getTextAttribute());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, theEntity.getTextAttribute());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED.length(), theEntity.getTextAttribute().length());
    }

}
