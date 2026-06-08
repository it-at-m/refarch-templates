package de.muenchen.oss.refarch.backend;

import de.muenchen.oss.refarch.backend.configuration.security.SecurityProperties;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * Configures a mocked JwtDecoder as Spring bean to test authorization via roles.
 * When an Authorization header is provided in the request, the Bearer value is mapped to an
 * equivalent role if registered in {@link TestSecurityConfiguration#MOCKED_ROLES}.
 * e.g. Authorization: "Bearer reader" -> Role reader
 */
@TestConfiguration
@RequiredArgsConstructor
public class TestSecurityConfiguration {

    private final SecurityProperties securityProperties;

    private static final List<String> MOCKED_ROLES = List.of("reader", "writer");

    @Bean
    public JwtDecoder mockedJwtDecoder() {
        JwtDecoder mockedJwtDecoder = Mockito.mock(JwtDecoder.class);

        MOCKED_ROLES.forEach(role -> {
            Mockito.when(mockedJwtDecoder.decode(role))
                    .thenReturn(jwtWithRole(role));
        });

        return mockedJwtDecoder;
    }

    private Jwt jwtWithRole(String role) {
        return Jwt.withTokenValue(role)
                .header("alg", "none")
                .claim(
                        "resource_access",
                        Map.of(
                                securityProperties.getClientId(),
                                Map.of("roles", List.of(role))))
                .build();
    }

}
