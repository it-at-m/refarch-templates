package de.muenchen.oss.refarch.backend.configuration.security;

import de.muenchen.oss.refarch.backend.configuration.filter.RequestResponseLoggingFilter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.validation.annotation.Validated;

/// Properties class that holds configuration data relevant for security mechanisms
@ConfigurationProperties(prefix = "refarch.security")
@Validated
@Data
public class SecurityProperties {
    /// ID of the used OAuth client.
    @NotBlank private String clientId;

    /// Name of the JWT claim that contains the username.
    ///
    /// Defaults to `preferred_username`.
    @NotBlank private String usernameClaim = "preferred_username";

    /// URI of the endpoint used for fetching permissions.
    ///
    /// See [KeycloakPermissionsAuthoritiesConverter].
    private String permissionsUri;

    /// Timeout for which resolved permissions are cached and reused.
    ///
    /// Defaults to `60s`.
    ///
    /// See [KeycloakPermissionsAuthoritiesConverter]
    @NotNull private Duration permissionsCacheLifetime = Duration.ofSeconds(60);

    /// Max number of entries the permissions cache contains.
    ///
    /// Defaults to `1000`.
    @Positive private long permissionsCacheMaxSize = 1000;

    /// Logging mode for incoming HTTP requests.
    ///
    /// Defaults to [RequestResponseLoggingFilter.LoggingMode#NONE]
    ///
    /// See [RequestResponseLoggingFilter]
    @NotNull private RequestResponseLoggingFilter.LoggingMode loggingMode = RequestResponseLoggingFilter.LoggingMode.NONE;

    /// List of paths to ignore when logging HTTP requests.
    ///
    /// Defaults to: `/actuator/**`
    ///
    /// See [RequestResponseLoggingFilter]
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
