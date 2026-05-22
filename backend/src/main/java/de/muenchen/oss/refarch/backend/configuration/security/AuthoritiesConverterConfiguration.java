package de.muenchen.oss.refarch.backend.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * Configure how authorities are resolved.
 * Either {@link KeycloakRolesAuthoritiesConverter} or with profile "keycloak-permissions"
 * {@link KeycloakPermissionsAuthoritiesConverter}.
 */
@Configuration
@Profile("!no-security")
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
            final SecurityProperties securityProperties, final RestClient.Builder restClientBuilder) {
        return new KeycloakPermissionsAuthoritiesConverter(
                securityProperties,
                restClientBuilder
                        .requestFactory(clientHttpRequestFactory())
                        .build());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(KEYCLOAK_FETCH_TIMEOUT));
        factory.setReadTimeout(Duration.ofSeconds(KEYCLOAK_FETCH_TIMEOUT));
        return factory;
    }
}
