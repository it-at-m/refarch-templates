package de.muenchen.refarch.configuration;

import de.muenchen.refarch.MicroServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static de.muenchen.refarch.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testexample;DB_CLOSE_ON_EXIT=FALSE"
        }
)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
class CacheControlConfigurationTest {

    private static final String ENTITY_ENDPOINT_URL = "/theEntities";

    private static final String EXPECTED_CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testForCacheControlHeadersForEntityEndpoint() {
        final ResponseEntity<String> response = testRestTemplate.exchange(ENTITY_ENDPOINT_URL, HttpMethod.GET, null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.CACHE_CONTROL));
        assertEquals(EXPECTED_CACHE_CONTROL_HEADER_VALUES, response.getHeaders().getCacheControl());
    }

}
