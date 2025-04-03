package de.muenchen.refarch.configuration;

import de.muenchen.refarch.security.RequestResponseLoggingFilter;
import de.muenchen.refarch.security.RequestResponseLoggingFilter.LoggingMode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.annotation.Validated;

/**
 * Properties class that holds configuration data relevant for security mechanisms
 */
@ConfigurationProperties(prefix = "security")
@Validated
@Profile("!no-security")
@Data
public class SecurityProperties {
    /**
     * Logging mode for incoming HTTP requests, see also {@link RequestResponseLoggingFilter}
     */
    @NotNull
    private LoggingMode loggingMode = LoggingMode.NONE;

    /**
     * URI of the userinfo endpoint to use for fetching data relevant for authorization (e.g. roles or
     * authorities), see also {@link UserInfoAuthoritiesService}
     */
    @NotBlank
    private String userInfoUri;

    /**
     * List of paths to ignore when logging HTTP requests, see also {@link RequestResponseLoggingFilter}
     */
    @NotNull
    private List<AntPathRequestMatcher> loggingIgnoreList = List.of(AntPathRequestMatcher.antMatcher("/actuator/**"));

    @SuppressFBWarnings(value = "EI_EXPOSE_REP", matchType = SuppressMatchType.EXACT)
    public List<AntPathRequestMatcher> getLoggingIgnoreListAsMatchers() {
        return loggingIgnoreList;
    }

    public List<String> getLoggingIgnoreList() {
        return loggingIgnoreList.stream().map(AntPathRequestMatcher::toString).collect(Collectors.toList());
    }

    public void setLoggingIgnoreList(final List<String> patterns) {
        this.loggingIgnoreList = patterns.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
    }
}
