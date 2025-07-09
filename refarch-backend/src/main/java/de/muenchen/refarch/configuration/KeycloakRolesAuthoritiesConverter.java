package de.muenchen.refarch.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
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
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        return Stream.concat(
                defaultConverter.convert(jwt).stream(),
                extractRoles(jwt).stream())
                .collect(Collectors.toSet());
    }

    private Collection<? extends GrantedAuthority> extractRoles(final Jwt jwt) {
        return Optional.ofNullable(
                jwt.getClaimAsMap(CLAIM_NAME))
                .map(resourceAccess -> (Map<String, Collection<String>>) resourceAccess.get(securityProperties.getClientId()))
                .map(client -> client.get("roles"))
                .map(roles -> roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }
}
