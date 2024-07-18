/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.configuration;

import de.muenchen.refarch.MicroServiceApplication;
import de.muenchen.refarch.domain.TheEntity;
import de.muenchen.refarch.rest.TheEntityRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.UUID;

import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.refarch.TestConstants.TheEntityDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testexample;DB_CLOSE_ON_EXIT=FALSE"
        }
)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class UnicodeConfigurationTest {

    private static final String ENTITY_ENDPOINT_URL = "/theEntities";

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
        // Persist entity with decomposed string.
        final TheEntityDto theEntityDto = new TheEntityDto();
        theEntityDto.setTextAttribute(TEXT_ATTRIBUTE_DECOMPOSED);
        assertEquals(TEXT_ATTRIBUTE_DECOMPOSED.length(), theEntityDto.getTextAttribute().length());
        final TheEntityDto response = testRestTemplate.postForEntity(URI.create(ENTITY_ENDPOINT_URL), theEntityDto, TheEntityDto.class).getBody();

        // Check whether response contains a composed string.
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, response.getTextAttribute());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED.length(), response.getTextAttribute().length());

        // Extract uuid from self link.
        final UUID uuid = UUID.fromString(StringUtils.substringAfterLast(response.getRequiredLink("self").getHref(), "/"));

        // Check persisted entity contains a composed string via JPA repository.
        final TheEntity theEntity = theEntityRepository.findById(uuid).orElse(null);
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, theEntity.getTextAttribute());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED.length(), theEntity.getTextAttribute().length());
    }

}
