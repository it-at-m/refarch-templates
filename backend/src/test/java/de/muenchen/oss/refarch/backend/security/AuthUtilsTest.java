package de.muenchen.oss.refarch.backend.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class AuthUtilsTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenJwtWithPreferredUsernameClaim_thenGetUsernameReturnsClaimValue() {
        final Jwt jwt = new Jwt("token", null, null, Map.of("alg", "none"), Map.of("preferred_username", "user1"));
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String username = AuthUtils.getUsername("preferred_username");

        assertThat(username).isEqualTo("user1");
    }

    @Test
    void givenJwtWithConfiguredUsernameClaim_thenGetUsernameReturnsClaimValue() {
        final Jwt jwt = new Jwt("token", null, null, Map.of("alg", "none"), Map.of("custom_claim", "user2"));
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String username = AuthUtils.getUsername("custom_claim");

        assertThat(username).isEqualTo("user2");
    }

    @Test
    void givenNoAuthentication_thenGetUsernameReturnsUnauthenticatedConstant() {
        SecurityContextHolder.clearContext();

        final String username = AuthUtils.getUsername("preferred_username");

        assertThat(username).isEqualTo(AuthUtils.NAME_UNAUTHENTICATED_USER);
    }

    @Test
    void givenUsernamePasswordAuthentication_thenGetUsernameReturnsAuthenticationName() {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("user3", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String username = AuthUtils.getUsername(null);

        assertThat(username).isEqualTo("user3");
    }
}
