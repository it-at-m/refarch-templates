/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package refarch.route;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import refarch.ApiGatewayApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static refarch.TestConstants.SPRING_TEST_PROFILE;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = { ApiGatewayApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(SPRING_TEST_PROFILE)
@AutoConfigureWireMock
class BackendRouteTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        stubFor(get(urlEqualTo("/remote/endpoint"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeaders(new HttpHeaders(
                                new HttpHeader("Content-Type", "application/json"),
                                new HttpHeader("WWW-Authenticate", "Bearer realm=\"Access to the staging site\", charset=\"UTF-8\""), // removed by route filter
                                new HttpHeader("Expires", "Wed, 21 Oct 2099 07:28:06 GMT") // removed by route filter
                        ))
                        .withBody("{ \"testkey\" : \"testvalue\" }")));
    }

    @Test
    @WithMockUser
    void backendRouteResponse() {
        webTestClient.get().uri("/api/refarch-backend-service/remote/endpoint")
                .header("Cookie", "SESSION=5cfb01a3-b691-4ca9-8735-a05690e6c2ec; XSRF-TOKEN=4d82f9f1-41f6-4a09-994a-df99d30d1be9") // removed by default-filter
                .header("X-XSRF-TOKEN", "5cfb01a3-b691-4ca9-8735-a05690e6c2ec") // angular specific -> removed by default-filter
                .header("Content-Type", "application/hal+json")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().valueMatches("Content-Type", "application/json")
                .expectHeader().doesNotExist("WWW-Authenticate")
                .expectHeader().valueMatches("Expires", "0")
                .expectBody().jsonPath("$.testkey").isEqualTo("testvalue");

        verify(getRequestedFor(urlEqualTo("/remote/endpoint"))
                .withoutHeader("Cookie")
                .withoutHeader("X-SRF-TOKEN")
                .withHeader("Content-Type", new EqualToPattern("application/hal+json")));
    }

}
