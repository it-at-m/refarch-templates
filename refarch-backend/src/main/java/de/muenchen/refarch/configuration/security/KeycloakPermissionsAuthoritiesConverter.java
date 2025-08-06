package de.muenchen.refarch.configuration.security;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service that calls an OIDC /userinfo endpoint (with JWT Bearer Auth) and extracts the
 * "Authorities" contained there.
 * <p>
 * The usage of simpler {@link KeycloakRolesAuthoritiesConverter} should be preferred.
 */
@Slf4j
@RequiredArgsConstructor
public class KeycloakPermissionsAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String AUTHENTICATION_CACHE_NAME = "authentication_cache";
    private static final int AUTHENTICATION_CACHE_ENTRY_SECONDS_TO_EXPIRE = 60;
    private static final String PERMISSION_NAME_KEY = "rsname";
    private static final String BODY_GRANT_TYPE = "grant_type";
    private static final String GRANT_TYPE = "urn:ietf:params:oauth:grant-type:uma-ticket";
    private static final String BODY_AUDIENCE = "audience";
    private static final String BODY_RESPONSE_MODE = "response_mode";
    private static final String RESPONSE_MODE_PERMISSIONS = "permissions";

    private final SecurityProperties securityProperties;
    private final RestTemplate restTemplate;
    private final Cache cache;

    public KeycloakPermissionsAuthoritiesConverter(final SecurityProperties securityProperties, final RestTemplateBuilder restTemplateBuilder) {
        this(
                securityProperties,
                restTemplateBuilder.build(),
                new CaffeineCache(
                        AUTHENTICATION_CACHE_NAME,
                        Caffeine.newBuilder()
                                .expireAfterWrite(AUTHENTICATION_CACHE_ENTRY_SECONDS_TO_EXPIRE, TimeUnit.SECONDS)
                                .ticker(Ticker.systemTicker())
                                .build()));
    }

    /**
     * Calls the /userinfo endpoint and extracts {@link GrantedAuthority}s from the "authorities" claim.
     *
     * @param jwt the JWT
     * @return the {@link GrantedAuthority}s according to claim "authorities" of /userinfo endpoint
     */
    @Override
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        final ValueWrapper valueWrapper = this.cache.get(jwt.getSubject());
        if (valueWrapper != null) {
            // value present in cache
            @SuppressWarnings("unchecked")
            final Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) valueWrapper.get();
            log.debug("Resolved authorities (from cache): {}", authorities);
            return authorities;
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        try {
            final List<?> permissions = this.fetchPermissions(this.securityProperties.getPermissionsUri(), jwt, this.securityProperties.getClientId());

            log.debug("Response from permissions endpoint: {}", permissions);
            if (permissions != null) {
                authorities = asAuthorities(permissions);
            }
            log.debug("Resolved authorities (from permissions endpoint): {}", authorities);
            // store
            this.cache.put(jwt.getSubject(), authorities);
        } catch (final Exception e) {
            log.error("Error while resolving user permissions - user is granted NO authorities", e);
        }

        return authorities;
    }

    private static List<GrantedAuthority> asAuthorities(final List<?> permissions) {
        return permissions.stream()
                .filter(i -> i instanceof Map)
                .map(i -> ((Map<?, ?>) i).get(PERMISSION_NAME_KEY))
                .filter(i -> i instanceof String)
                .map(i -> (GrantedAuthority) new SimpleGrantedAuthority((String) i))
                .toList();
    }

    private List<?> fetchPermissions(final String endpointUrl, final Jwt jwt, final String clientId) {
        try {
            log.debug("Fetching permissions for token subject: {}", jwt.getSubject());
            // build headers
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(jwt.getTokenValue());
            // build body
            final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add(BODY_GRANT_TYPE, GRANT_TYPE);
            body.add(BODY_AUDIENCE, clientId);
            body.add(BODY_RESPONSE_MODE, RESPONSE_MODE_PERMISSIONS);
            final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
            // fetch permissions
            return restTemplate.exchange(endpointUrl, HttpMethod.POST, entity,
                    List.class).getBody();
        } catch (final RestClientException e) {
            log.error("Error while fetching permissions - user is granted no authorities", e);
            return List.of();
        }
    }
}
