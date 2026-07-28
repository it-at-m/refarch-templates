package de.muenchen.oss.refarch.backend.configuration.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * {@link AuditorAware} implementation that provides the username of the
 * currently authenticated user for Spring Data JPA auditing.
 * <p>
 * The implementation can also be autowired to access the username outside of Spring Data JPA
 * Auditing.
 */
@Component("usernameAuditorAware")
@RequiredArgsConstructor
public class UsernameAuditorAware implements AuditorAware<String> {

    /**
     * Auditor name used when no authenticated user is available.
     */
    public static final String NAME_UNAUTHENTICATED_USER = "unauthenticated";

    private final SecurityProperties securityProperties;

    /**
     * Returns the username of the current auditor.
     * <p>
     * The auditor is determined from the current Spring Security
     * {@link Authentication}:
     * <ul>
     * <li>For {@link JwtAuthenticationToken}, the username is read from the
     * JWT claim configured via
     * {@link SecurityProperties#getUsernameClaim()}.</li>
     * <li>For {@link UsernamePasswordAuthenticationToken}, the authenticated
     * principal name returned by {@link Authentication#getName()} is used.</li>
     * <li>If no authenticated user is available, or the configured JWT username
     * claim is missing or blank, {@link #NAME_UNAUTHENTICATED_USER} is
     * returned.</li>
     * </ul>
     *
     * @return an {@link Optional} containing the current auditor name; the
     *         returned {@link Optional} is never empty
     */
    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return switch (authentication) {
        case JwtAuthenticationToken jwtAuth -> {
            final Object claimValue = jwtAuth.getTokenAttributes().get(securityProperties.getUsernameClaim());
            if (claimValue instanceof String username && !username.isBlank()) {
                yield Optional.of(username);
            }
            yield Optional.of(NAME_UNAUTHENTICATED_USER);
        }
        case UsernamePasswordAuthenticationToken ignored -> {
            final String name = authentication.getName();
            if (!name.isBlank()) {
                yield Optional.of(name);
            }
            yield Optional.of(NAME_UNAUTHENTICATED_USER);
        }
        case null, default -> Optional.of(NAME_UNAUTHENTICATED_USER);
        };
    }

}
