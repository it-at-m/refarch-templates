package de.muenchen.oss.refarch.backend.configuration.security;

import java.time.Duration;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Configure how authorities are resolved.
 * Either {@link KeycloakRolesAuthoritiesConverter} or with profile "keycloak-permissions"
 * {@link KeycloakPermissionsAuthoritiesConverter}.
 */
@Configuration
public class AuthoritiesConverterConfiguration {
    public static final int KEYCLOAK_FETCH_TIMEOUT = 30;

    @Bean
    @Profile("!keycloak-permissions")
    public KeycloakRolesAuthoritiesConverter keycloakRolesAuthoritiesConverter(final SecurityProperties securityProperties) {
        return new KeycloakRolesAuthoritiesConverter(securityProperties);
    }

    @Bean
    @Profile("keycloak-permissions")
    public KeycloakPermissionsAuthoritiesConverter keycloakPermissionsAuthoritiesConverter(
            final SecurityProperties securityProperties, final RestClient.Builder restClientBuilder,
            final ClientHttpRequestFactoryBuilder<?> requestFactoryBuilder) {
        return new KeycloakPermissionsAuthoritiesConverter(
                securityProperties,
                restClientBuilder
                        .requestFactory(clientHttpRequestFactory(requestFactoryBuilder))
                        .build());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(final ClientHttpRequestFactoryBuilder<?> requestFactoryBuilder) {
        return requestFactoryBuilder
                .build(HttpClientSettings.defaults().withConnectTimeout(Duration.ofSeconds(KEYCLOAK_FETCH_TIMEOUT))
                        .withReadTimeout(Duration.ofSeconds(KEYCLOAK_FETCH_TIMEOUT)));
    }
}
