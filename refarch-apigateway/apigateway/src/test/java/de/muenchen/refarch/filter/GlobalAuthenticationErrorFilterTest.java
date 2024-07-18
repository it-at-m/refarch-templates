/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.filter;

import de.muenchen.refarch.ApiGatewayApplication;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import de.muenchen.refarch.TestConstants;
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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = { ApiGatewayApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(TestConstants.SPRING_TEST_PROFILE)
@AutoConfigureWireMock
class GlobalAuthenticationErrorFilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        stubFor(get(urlEqualTo("/remote"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                        .withHeaders(new HttpHeaders(
                                new HttpHeader("Content-Type", "application/json"),
                                new HttpHeader("WWW-Authenticate", "Bearer realm=\"Access to the staging site\", charset=\"UTF-8\""),
                                new HttpHeader("Expires", "Wed, 21 Oct 2099 07:28:06 GMT")))
                        .withBody("{ \"testkey\" : \"testvalue\" }")));
    }

    @Test
    @WithMockUser
    void backendAuthenticationError() {
        //@formatter:off
        webTestClient.get().uri("/api/refarch-backend-service/remote").exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectHeader().valueMatches("Content-Type", "application/json")
                .expectHeader().doesNotExist("WWW-Authenticate")
                .expectHeader().valueMatches("Expires", "0")
                .expectBody()
                    .jsonPath("$.status").isEqualTo("401")
                    .jsonPath("$.error").isEqualTo("Authentication Error");
        //@formatter:on
    }

}
