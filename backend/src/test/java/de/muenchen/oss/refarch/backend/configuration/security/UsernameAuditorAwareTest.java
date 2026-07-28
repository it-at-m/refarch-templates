package de.muenchen.oss.refarch.backend.configuration.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
class UsernameAuditorAwareTest {

    private static final String USERNAME_CLAIM = "preferred_username";

    @Mock
    private SecurityProperties securityProperties;

    @InjectMocks
    private UsernameAuditorAware auditorAware;

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
        final Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        // then
        assertThat(currentAuditor).contains("john.doe");
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
        final Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        // then
        assertThat(currentAuditor).contains(UsernameAuditorAware.NAME_UNAUTHENTICATED_USER);
    }

    @Test
    void givenJwtWithBlankUsernameClaim_thenReturnUnauthenticatedUser() {
        // given
        when(securityProperties.getUsernameClaim()).thenReturn(USERNAME_CLAIM);
        final Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim(USERNAME_CLAIM, "   ")
                .build();
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        // then
        assertThat(currentAuditor).contains(UsernameAuditorAware.NAME_UNAUTHENTICATED_USER);
    }

    @Test
    void givenUsernamePasswordAuthentication_thenReturnUsername() {
        // given
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("jane.doe", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        // then
        assertThat(currentAuditor).contains("jane.doe");
    }

    @Test
    void givenUsernamePasswordAuthenticationWithBlankName_thenReturnUnauthenticatedUser() {
        // given
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        // then
        assertThat(currentAuditor).contains(UsernameAuditorAware.NAME_UNAUTHENTICATED_USER);
    }

    @Test
    void givenNoAuthentication_thenReturnUnauthenticatedUser() {
        // given
        SecurityContextHolder.clearContext();

        // when
        final Optional<String> currentAuditor = auditorAware.getCurrentAuditor();

        // then
        assertThat(currentAuditor).contains(UsernameAuditorAware.NAME_UNAUTHENTICATED_USER);
    }

}
