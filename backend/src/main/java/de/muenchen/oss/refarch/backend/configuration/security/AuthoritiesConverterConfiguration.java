package de.muenchen.oss.refarch.backend.configuration.security;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

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
        final RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(Timeout.ofSeconds(KEYCLOAK_FETCH_TIMEOUT))
                .setConnectionRequestTimeout(Timeout.ofSeconds(KEYCLOAK_FETCH_TIMEOUT))
                .build();

        final ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(KEYCLOAK_FETCH_TIMEOUT))
                .build();

        @SuppressWarnings("PMD.CloseResource")
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultConnectionConfig(connectionConfig);

        final CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
