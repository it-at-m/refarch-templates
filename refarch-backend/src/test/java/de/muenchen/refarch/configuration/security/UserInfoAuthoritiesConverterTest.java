package de.muenchen.refarch.configuration.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

@Deprecated
@ExtendWith(MockitoExtension.class)
class UserInfoAuthoritiesConverterTest {
    private static final String TEST_SUBJECT = "test-subject";
    private static final String TEST_TOKEN_VALUE = "test-token";
    private static final String USER_INFO_URI = "http://localhost/userinfo";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Cache cache;
    private UserInfoAuthoritiesConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserInfoAuthoritiesConverter(USER_INFO_URI, restTemplate, cache);
    }

    @Test
    void testConvert_WithAuthorities() {
        // Setup
        final Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(TEST_SUBJECT);
        when(jwt.getTokenValue()).thenReturn(TEST_TOKEN_VALUE);

        final Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("authorities", new String[] { ROLE_USER, ROLE_ADMIN });
        when(restTemplate.exchange(eq(USER_INFO_URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(responseMap, HttpStatus.OK));

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

        final Map<String, Object> responseMap = new HashMap<>();
        when(restTemplate.exchange(eq(USER_INFO_URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(responseMap, HttpStatus.OK));

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
