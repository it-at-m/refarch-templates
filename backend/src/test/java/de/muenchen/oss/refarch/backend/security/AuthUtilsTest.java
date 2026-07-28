package de.muenchen.oss.refarch.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import de.muenchen.oss.refarch.backend.configuration.security.SecurityProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AuthUtilsTest {

    private static final String USERNAME_CLAIM = "preferred_username";

    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private AuthUtils authUtils;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenJwtWithUsernameClaim_thenReturnUsername() {
        // given
        when(securityProperties.getUsernameClaim()).thenReturn(USERNAME_CLAIM);
        final Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim(USERNAME_CLAIM, "john.doe")
                .build();
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final String username = authUtils.getUsername();

        // then
        assertThat(username).isEqualTo("john.doe");
    }

    @Test
    void givenJwtWithoutUsernameClaim_thenReturnUnauthenticatedUser() {
        // given
        when(securityProperties.getUsernameClaim()).thenReturn(USERNAME_CLAIM);
        final Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claims(claims -> claims.put("other", "value"))
                .build();
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final String username = authUtils.getUsername();

        // then
        assertThat(username).isNull();
    }

    @Test
    void givenUsernamePasswordAuthentication_thenReturnUsername() {
        // given
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("jane.doe", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final String username = authUtils.getUsername();

        // then
        assertThat(username).isEqualTo("jane.doe");
    }

    @Test
    void givenNoAuthentication_thenReturnUnauthenticatedUser() {
        // given
        SecurityContextHolder.clearContext();

        // when
        final String username = authUtils.getUsername();

        // then
        assertThat(username).isEqualTo(AuthUtils.NAME_UNAUTHENTICATED_USER);
    }

}
