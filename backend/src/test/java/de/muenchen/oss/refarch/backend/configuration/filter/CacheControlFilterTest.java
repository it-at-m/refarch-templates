package de.muenchen.oss.refarch.backend.configuration.filter;

import static de.muenchen.oss.refarch.backend.TestConstants.SPRING_TEST_PROFILE;

import de.muenchen.oss.refarch.backend.MicroServiceApplication;
import de.muenchen.oss.refarch.backend.TestConstants;
import de.muenchen.oss.refarch.backend.TestSecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureRestTestClient
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE })
@Import(TestSecurityConfiguration.class)
class CacheControlFilterTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER = new PostgreSQLContainer(
            DockerImageName.parse(TestConstants.TESTCONTAINERS_POSTGRES_IMAGE));

    private static final String ENTITY_ENDPOINT_URL = "/theEntity";

    private static final String EXPECTED_CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void givenEntityEndpoint_thenCacheControlHeadersPresent() {
        restTestClient.get()
                .uri(ENTITY_ENDPOINT_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer reader")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists(HttpHeaders.CACHE_CONTROL)
                .expectHeader().valueEquals(HttpHeaders.CACHE_CONTROL, EXPECTED_CACHE_CONTROL_HEADER_VALUES);
    }

}
