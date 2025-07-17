package de.muenchen.refarch.configuration.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@RequiredArgsConstructor
public class KeycloakRolesAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String CLAIM_NAME = "resource_access";

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
    private final SecurityProperties securityProperties;

    @Override
    public Collection<GrantedAuthority> convert(@Nullable final Jwt jwt) {
        if (jwt == null) {
            return Collections.emptySet();
        }
        return Stream.concat(
                Optional.of(defaultConverter.convert(jwt)).orElse(Collections.emptySet()).stream(),
                extractRoles(jwt).stream())
                .collect(Collectors.toSet());
    }

    private Collection<? extends GrantedAuthority> extractRoles(final Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap(CLAIM_NAME))
                .map(resourceAccess -> resourceAccess.get(securityProperties.getClientId()))
                .filter(client -> client instanceof Map)
                .map(client -> (Map<String, Object>) client)
                .map(client -> client.get("roles"))
                .filter(roles -> roles instanceof Collection)
                .map(roles -> (Collection<String>) roles)
                .map(roles -> roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
