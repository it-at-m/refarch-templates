package de.muenchen.refarch.configuration.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class KeycloakRolesAuthoritiesConverterTest {
    private static final String TEST_CLIENT = "test-client";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";

    @Mock
    private SecurityProperties securityProperties;
    @InjectMocks
    private KeycloakRolesAuthoritiesConverter converter;

    @BeforeEach
    void setUp() {
        when(securityProperties.getClientId()).thenReturn(TEST_CLIENT);
    }

    @Test
    void testConvert_WithRoles() {
        // Setup
        final Map<String, Object> resourceAccessClaim = new HashMap<>();
        resourceAccessClaim.put(TEST_CLIENT, Map.of("roles", List.of("admin", "user")));
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM)).thenReturn(resourceAccessClaim);

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_admin")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_user")));
    }

    @Test
    void testConvert_WithoutRoles() {
        // Setup
        final Map<String, Object> claims = new HashMap<>();
        claims.put(RESOURCE_ACCESS_CLAIM, Map.of(
                TEST_CLIENT, Collections.emptyMap()));
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM)).thenReturn(claims);

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testConvert_ClientNotInResourceAccess() {
        // Setup
        final Map<String, Object> resourceAccessClaim = new HashMap<>();
        resourceAccessClaim.put("other-client", Map.of("roles", List.of("admin")));
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM)).thenReturn(resourceAccessClaim);

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testConvert_NullClaims() {
        // Setup
        final Jwt jwt = mock(Jwt.class);

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertTrue(authorities.isEmpty());
    }
}
