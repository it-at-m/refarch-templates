package de.muenchen.oss.refarch.backend.configuration.security;

import de.muenchen.oss.refarch.backend.security.AuthUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * {@link AuditorAware} implementation that uses the username of the
 * currently authenticated user for Spring Data JPA auditing via {@link AuthUtils} bean.
 */
@Component("usernameAuditorAware")
@RequiredArgsConstructor
public class UsernameAuditorAware implements AuditorAware<String> {

    private final AuthUtils authUtils;

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(authUtils.getUsername());
    }

}
