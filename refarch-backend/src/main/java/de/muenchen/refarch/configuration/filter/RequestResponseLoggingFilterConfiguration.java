package de.muenchen.refarch.configuration.filter;

import de.muenchen.refarch.configuration.security.SecurityProperties;
import de.muenchen.refarch.security.AuthUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;

@Configuration
@Profile("!no-security")
@RequiredArgsConstructor
@Slf4j
public class RequestResponseLoggingFilterConfiguration {

    private final SecurityProperties securityProperties;

    @Bean
    @FilterRegistration(urlPatterns = "/*", order = 1)
    public RequestResponseLoggingFilter requestResponseLoggingFilter() {
        return new RequestResponseLoggingFilter(securityProperties);
    }

    /**
     * This filter logs the username for requests.
     */
    @RequiredArgsConstructor
    public static class RequestResponseLoggingFilter implements Filter {

        private static final List<String> CHANGING_METHODS = List.of(HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name());

        private final SecurityProperties securityProperties;

        /**
         * Logging mode to use for incoming HTTP requests
         */
        public enum LoggingMode {
            /**
             * Logs all requests
             */
            ALL,
            /**
             * Logs only changing requests, see {@link RequestResponseLoggingFilter#CHANGING_METHODS}
             */
            CHANGING,
            /**
             * Logs no requests
             */
            NONE
        }

        /**
         * The method logs the username extracted out of the {@link SecurityContext},
         * the kind of HTTP-Request, the targeted URI and the response http status code.
         * {@inheritDoc}
         */
        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
                throws IOException, ServletException {
            chain.doFilter(request, response);
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
            final HttpServletResponse httpResponse = (HttpServletResponse) response;
            if (checkForLogging(httpRequest)) {
                log.info("User {} executed {} on URI {} with http status {}",
                        AuthUtils.getUsername(),
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        httpResponse.getStatus());
            }
        }

        /**
         * The method checks if logging the username should be done.
         *
         * @param httpServletRequest The request to check for logging.
         * @return True if logging should be done otherwise false.
         */
        private boolean checkForLogging(final HttpServletRequest httpServletRequest) {
            final boolean isLoggingMode = switch (securityProperties.getLoggingMode()) {
            case ALL -> true;
            case CHANGING -> CHANGING_METHODS.contains(httpServletRequest.getMethod());
            default -> false;
            };

            return isLoggingMode && securityProperties.getLoggingIgnoreListAsMatchers().stream().noneMatch(matcher -> matcher.matches(httpServletRequest));
        }

    }

}
