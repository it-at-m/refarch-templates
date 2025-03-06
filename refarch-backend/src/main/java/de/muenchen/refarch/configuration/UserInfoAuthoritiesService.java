package de.muenchen.refarch.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Service that calls an OIDC /userinfo endpoint (with JWT Bearer Auth) and extracts the
 * "Authorities" contained there.
 */
@Slf4j
public class UserInfoAuthoritiesService {

    private static final String NAME_AUTHENTICATION_CACHE = "authentication_cache";
    private static final int AUTHENTICATION_CACHE_ENTRY_SECONDS_TO_EXPIRE = 60;

    private static final String CLAIM_AUTHORITIES = "authorities";

    private final String userInfoUri;
    private final RestTemplate restTemplate;
    private final Cache cache;

    /**
     * Creates a new instance
     *
     * @param userInfoUri userinfo endpoint URI
     * @param restTemplateBuilder a {@link RestTemplateBuilder}
     */
    public UserInfoAuthoritiesService(final String userInfoUri, final RestTemplateBuilder restTemplateBuilder) {
        this.userInfoUri = userInfoUri;
        this.restTemplate = restTemplateBuilder.build();
        this.cache = new CaffeineCache(NAME_AUTHENTICATION_CACHE,
                Caffeine.newBuilder()
                        .expireAfterWrite(AUTHENTICATION_CACHE_ENTRY_SECONDS_TO_EXPIRE, TimeUnit.SECONDS)
                        .ticker(Ticker.systemTicker())
                        .build());
    }

    /**
     * Calls the /userinfo endpoint and extracts {@link GrantedAuthority}s from the "authorities" claim.
     *
     * @param jwt the JWT
     * @return the {@link GrantedAuthority}s according to claim "authorities" of /userinfo endpoint
     */
    public Collection<SimpleGrantedAuthority> loadAuthorities(final Jwt jwt) {
        final ValueWrapper valueWrapper = this.cache.get(jwt.getSubject());
        if (valueWrapper != null) {
            // value present in cache
            @SuppressWarnings("unchecked")
            final Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) valueWrapper.get();
            log.debug("Resolved authorities (from cache): {}", authorities);
            return authorities;
        }

        log.debug("Fetching user-info for token subject: {}", jwt.getSubject());
        @SuppressWarnings("PMD.LooseCoupling")
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue());
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            final Map<String, Object> map = restTemplate.exchange(this.userInfoUri, HttpMethod.GET, entity,
                    Map.class).getBody();

            log.debug("Response from user-info Endpoint: {}", map);
            if (map != null && map.containsKey(CLAIM_AUTHORITIES)) {
                authorities = asAuthorities(map.get(CLAIM_AUTHORITIES));
            }
            log.debug("Resolved Authorities (from /userinfo Endpoint): {}", authorities);
            // store
            this.cache.put(jwt.getSubject(), authorities);
        } catch (Exception e) {
            log.error(String.format("Could not fetch user details from %s - user is granted NO authorities",
                    this.userInfoUri), e);
        }

        return authorities;
    }

    private static List<SimpleGrantedAuthority> asAuthorities(final Object object) {
        final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Object authoritiesObject = object;
        if (authoritiesObject instanceof Collection) {
            final Collection<?> collection = (Collection<?>) authoritiesObject;
            authoritiesObject = collection.toArray(new Object[0]);
        }
        if (ObjectUtils.isArray(authoritiesObject)) {
            authorities.addAll(
                    Stream.of((Object[]) authoritiesObject)
                            .map(Object::toString)
                            .map(SimpleGrantedAuthority::new)
                            .toList());
        }
        return authorities;
    }

}
