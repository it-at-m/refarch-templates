package de.muenchen.oss.refarch.backend.configuration.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/// The filter adds a [HttpHeaders#CACHE_CONTROL] header to each HTTP response, if
/// the header is not already set.
@Component
@FilterRegistration(urlPatterns = "/*")
public class CacheControlFilter extends OncePerRequestFilter {

    private static final String CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";

    /// The method which adds the [HttpHeaders#CACHE_CONTROL] header
    /// to the [HttpServletResponse] given in the parameter,
    /// if the header is not already set.
    /// Same contract as for `super.doFilter`, but guaranteed to be
    /// just invoked once per request within a single request thread.
    ///
    /// See [OncePerRequestFilter#shouldNotFilterAsyncDispatch()] for details.
    ///
    /// Provides HttpServletRequest and HttpServletResponse arguments instead of the
    /// default ServletRequest and ServletResponse ones.
    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
            final HttpServletResponse response,
            @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final String cacheControlHeaderValue = response.getHeader(HttpHeaders.CACHE_CONTROL);
        if (!StringUtils.hasText(cacheControlHeaderValue)) {
            response.addHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_HEADER_VALUES);
        }

        filterChain.doFilter(request, response);

    }

}
