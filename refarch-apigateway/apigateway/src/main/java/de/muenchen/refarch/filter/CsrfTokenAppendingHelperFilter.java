/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.filter;

import de.muenchen.refarch.configuration.SecurityConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.CsrfWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * This class subscribes the {@link ServerWebExchange} for csrf token attachment
 * within the classes {@link CookieServerCsrfTokenRepository} and {@link CsrfWebFilter}.
 * The csrf configuration done only in {@link SecurityConfiguration#springSecurityFilterChain} is
 * not sufficient for csrf token attachment to a {@link ServerHttpResponse}.
 */
@Component
@Slf4j
public class CsrfTokenAppendingHelperFilter implements WebFilter {

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.debug("Trigger to append CSRF token to response");
        Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
        return csrfToken.doOnSuccess(token -> {
            // do nothing -> CSRF-Token is added as cookie in class CookieServerCsrfTokenRepository#saveToken
        }).then(chain.filter(exchange));
    }

}
