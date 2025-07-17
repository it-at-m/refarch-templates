package de.muenchen.refarch.configuration.security;

import de.muenchen.refarch.configuration.filter.RequestResponseLoggingFilter;
import de.muenchen.refarch.configuration.filter.RequestResponseLoggingFilter.LoggingMode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.validation.annotation.Validated;

/**
 * Properties class that holds configuration data relevant for security mechanisms
 */
@ConfigurationProperties(prefix = "refarch.security")
@Validated
@Profile("!no-security")
@Data
public class SecurityProperties {
    /**
     * ID of the used oAuth client.
     */
    @NotBlank private String clientId;

    /**
     * URI of the userinfo endpoint to use for fetching data relevant for authorization (e.g. roles or
     * authorities), see also {@link UserInfoAuthoritiesConverter}.
     *
     * @deprecated Use {@link KeycloakRolesAuthoritiesConverter}
     */
    @Deprecated
    @NotBlank private String userInfoUri;

    /**
     * Logging mode for incoming HTTP requests, see also {@link RequestResponseLoggingFilter}
     */
    @NotNull private LoggingMode loggingMode = LoggingMode.NONE;

    /**
     * List of paths to ignore when logging HTTP requests, see also {@link RequestResponseLoggingFilter}
     */
    @NotNull private List<PathPatternRequestMatcher> loggingIgnoreList = List.of(PathPatternRequestMatcher.withDefaults().matcher("/actuator/**"));

    @SuppressFBWarnings(value = "EI_EXPOSE_REP", matchType = SuppressMatchType.EXACT)
    public List<PathPatternRequestMatcher> getLoggingIgnoreListAsMatchers() {
        return loggingIgnoreList;
    }

    public List<String> getLoggingIgnoreList() {
        return loggingIgnoreList.stream().map(PathPatternRequestMatcher::toString).collect(Collectors.toList());
    }

    public void setLoggingIgnoreList(final List<String> patterns) {
        this.loggingIgnoreList = patterns.stream().map(pattern -> PathPatternRequestMatcher.withDefaults().matcher(pattern)).collect(Collectors.toList());
    }
}
