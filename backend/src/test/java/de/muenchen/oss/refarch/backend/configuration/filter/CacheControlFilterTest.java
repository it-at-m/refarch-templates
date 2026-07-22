package de.muenchen.oss.refarch.backend.configuration.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class CacheControlFilterTest {

    private static final String EXPECTED_CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";

    private final CacheControlFilter filter = new CacheControlFilter();

    @Test
    void givenMissingCacheControlHeader_thenAddDefaultHeader() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertEquals(EXPECTED_CACHE_CONTROL_HEADER_VALUES, response.getHeader(HttpHeaders.CACHE_CONTROL));
        assertSame(request, filterChain.getRequest());
        assertSame(response, filterChain.getResponse());
    }

    @Test
    void givenExistingCacheControlHeader_thenKeepExistingValue() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();
        response.addHeader(HttpHeaders.CACHE_CONTROL, "public, max-age=60");

        filter.doFilter(request, response, filterChain);

        assertEquals("public, max-age=60", response.getHeader(HttpHeaders.CACHE_CONTROL));
        assertSame(request, filterChain.getRequest());
        assertSame(response, filterChain.getResponse());
    }
}
