/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Ein custom {@link JwtAuthenticationConverter}, der die Authorities mittels
 * {@link UserInfoAuthoritiesService} vom /userinfo Endpoint des OIDC Providers bezieht.
 */
public class JwtUserInfoAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserInfoAuthoritiesService userInfoService;

    /**
     * Erzeugt eine neue Instanz von {@link JwtUserInfoAuthenticationConverter}.
     *
     * @param userInfoService ein {@link UserInfoAuthoritiesService}
     */
    public JwtUserInfoAuthenticationConverter(UserInfoAuthoritiesService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        return new JwtAuthenticationToken(source, this.userInfoService.loadAuthorities(source));
    }

}
