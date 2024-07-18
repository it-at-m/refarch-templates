/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.controller;

import de.muenchen.refarch.ApiGatewayApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = { ApiGatewayApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(SPRING_TEST_PROFILE)
public class ActuatorInfoEndpointTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void actuatorInfoProvidesAppswitcherUrl() {
        //@formatter:off
        webTestClient.get().uri("/actuator/info").exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.appswitcher.url").isEqualTo("https://test-url-appswitcher.muenchen.de");
        //@formatter:on
    }

}
