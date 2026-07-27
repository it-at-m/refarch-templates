package de.muenchen.oss.refarch.backend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Utilities for authentication data.
 */
public final class AuthUtils {

    public static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";

    private AuthUtils() {
    }

     /**
      * Extracts the user name from the existing Spring Security Context via
      * {@link SecurityContextHolder}.
      *
      * @param usernameClaim name of the claim containing the username in JWT tokens
      * @return the username or an "unauthenticated" if no {@link Authentication} exists
      */
     public static String getUsername(final String usernameClaim) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            final JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            return (String) jwtAuth.getTokenAttributes().getOrDefault(usernameClaim, null);
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            final UsernamePasswordAuthenticationToken usernameAuth = (UsernamePasswordAuthenticationToken) authentication;
            return usernameAuth.getName();
        } else {
            return NAME_UNAUTHENTICATED_USER;
        }
    }

}
