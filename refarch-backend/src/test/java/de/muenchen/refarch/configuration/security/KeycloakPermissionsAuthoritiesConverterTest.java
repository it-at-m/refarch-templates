package de.muenchen.refarch.configuration.security;

import static de.muenchen.refarch.TestConstants.SPRING_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = { SecurityProperties.class })
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties(SecurityProperties.class)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE })
class KeycloakPermissionsAuthoritiesConverterTest {
    private static final String TEST_SUBJECT = "test-subject";
    private static final String TEST_TOKEN_VALUE = "test-token";
    private static final String PERMISSIONS_URI = "http://localhost/permissions";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Autowired
    private SecurityProperties securityProperties;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Cache cache;
    private KeycloakPermissionsAuthoritiesConverter converter;

    @BeforeEach
    void setUp() {
        converter = new KeycloakPermissionsAuthoritiesConverter(securityProperties, restTemplate, cache);
    }

    @Test
    void testConvert_WithAuthorities() {
        // Setup
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
        when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);

        final List<Map<String, String>> response = List.of(
                Map.of(
                        "rsid", UUID.randomUUID().toString(),
                        "rsname", ROLE_USER),
                Map.of(
                        "rsid", UUID.randomUUID().toString(),
                        "rsname", ROLE_ADMIN));
        when(restTemplate.exchange(eq(PERMISSIONS_URI), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_USER)));
        assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));
    }

    @Test
    void testConvert_NoAuthorities() {
        // Setup
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
        when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);

        final List<Map<String, String>> response = List.of();
        when(restTemplate.exchange(eq(PERMISSIONS_URI), eq(HttpMethod.POST), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testConvert_CacheHit() {
        // Setup
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(TEST_SUBJECT);

        final Collection<GrantedAuthority> cachedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER));
        when(cache.get(eq(TEST_SUBJECT))).thenReturn(() -> cachedAuthorities);

        // Call
        final Collection<GrantedAuthority> authorities = converter.convert(jwt);

        // Assert
        assert authorities != null;
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(ROLE_USER)));
        verifyNoInteractions(restTemplate);
    }
}
