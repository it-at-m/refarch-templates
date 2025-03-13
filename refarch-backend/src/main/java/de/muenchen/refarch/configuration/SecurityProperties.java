package de.muenchen.refarch.configuration;

import de.muenchen.refarch.security.RequestResponseLoggingFilter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.annotations.SuppressMatchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Properties class that holds configuration data relevant for security mechanisms
 */
@ConfigurationProperties(prefix = "security")
@Validated
@Profile("!no-security")
@NoArgsConstructor
public class SecurityProperties {

    /**
     * Logging mode for incoming HTTP requests, see also {@link RequestResponseLoggingFilter}
     */
    @NotNull
    @Setter
    @Getter
    @SuppressWarnings("PMD.UnusedAssignment")
    private RequestResponseLoggingFilter.LoggingMode loggingMode = RequestResponseLoggingFilter.LoggingMode.NONE;

    /**
     * URI of the userinfo endpoint to use for fetching data relevant for authorization (e.g. roles or
     * authorities), see also {@link UserInfoAuthoritiesService}
     */
    @NotBlank
    @Setter
    @Getter
    private String userInfoUri;

    /**
     * List of paths to ignore when logging HTTP requests, see also {@link RequestResponseLoggingFilter}
     */
    @NotNull
    @SuppressWarnings("PMD.UnusedAssignment")
    private List<String> loggingIgnoreList = List.of("/actuator/**");

    /**
     * Internal matcher representation of the provided {@link #loggingIgnoreList}
     */
    private List<AntPathRequestMatcher> loggingIgnoreListAsMatchers = null;

    @SuppressWarnings("unused")
    public SecurityProperties(final RequestResponseLoggingFilter.LoggingMode loggingMode, final String userInfoUri, final List<String> loggingIgnoreList) {
        this.loggingMode = loggingMode;
        this.userInfoUri = userInfoUri;
        this.loggingIgnoreList = loggingIgnoreList;
    }

    /**
     * Sets the logging ignore list and updates the internal matcher representation
     *
     * @param loggingIgnoreList ignore list to set
     */
    @SuppressWarnings("PMD.NullAssignment")
    public void setLoggingIgnoreList(final List<String> loggingIgnoreList) {
        this.loggingIgnoreList = loggingIgnoreList;
        this.loggingIgnoreListAsMatchers = null;
    }

    /**
     * Getter for the internal matcher representation implemented as singleton
     *
     * @return matcher representations
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", matchType = SuppressMatchType.EXACT)
    public List<AntPathRequestMatcher> getLoggingIgnoreListAsMatchers() {
        if (loggingIgnoreListAsMatchers == null) {
            loggingIgnoreListAsMatchers = loggingIgnoreList.stream().map(AntPathRequestMatcher::antMatcher).toList();
        }
        return loggingIgnoreListAsMatchers;
    }

}
