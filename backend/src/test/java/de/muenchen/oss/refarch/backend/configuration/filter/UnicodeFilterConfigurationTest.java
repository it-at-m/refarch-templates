package de.muenchen.oss.refarch.backend.configuration.filter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.muenchen.oss.refarch.backend.configuration.filter.nfcconverter.NfcRequestFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPart;
import org.springframework.util.StreamUtils;

class UnicodeFilterConfigurationTest {

    /**
     * Decomposed string:
     * String "Ä-é" represented with unicode letters "A◌̈-e◌́"
     */
    private static final String TEXT_ATTRIBUTE_DECOMPOSED = "\u0041\u0308-\u0065\u0301";
    /**
     * Composed string:
     * String "Ä-é" represented with unicode letters "Ä-é".
     */
    private static final String TEXT_ATTRIBUTE_COMPOSED = "\u00c4-\u00e9";
    private static final String HEADER_NAME_DECOMPOSED = "x-" + TEXT_ATTRIBUTE_DECOMPOSED;
    private static final String HEADER_NAME_COMPOSED = "x-" + TEXT_ATTRIBUTE_COMPOSED;
    private static final String PARAMETER_NAME_DECOMPOSED = "name-" + TEXT_ATTRIBUTE_DECOMPOSED;
    private static final String PARAMETER_NAME_COMPOSED = "name-" + TEXT_ATTRIBUTE_COMPOSED;

    private final NfcRequestFilter filter = new NfcRequestFilter();

    @Test
    void givenWhitelistedContentType_thenNormalizeWrappedRequest() throws ServletException, IOException {
        final var wrappedRequest = filterWhitelistedRequest();
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, wrappedRequest.getParameter(PARAMETER_NAME_COMPOSED));
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, wrappedRequest.getHeader(HEADER_NAME_COMPOSED));
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, wrappedRequest.getCookies()[0].getValue());
        assertEquals(TEXT_ATTRIBUTE_COMPOSED, wrappedRequest.getReader().readLine());
        assertArrayEquals(
                TEXT_ATTRIBUTE_DECOMPOSED.getBytes(StandardCharsets.UTF_8),
                StreamUtils.copyToByteArray(wrappedRequest.getPart("file").getInputStream()));
    }

    @Test
    void givenWhitelistedContentType_thenNormalizeInputStream() throws ServletException, IOException {
        final var wrappedRequest = filterWhitelistedRequest();

        assertArrayEquals(
                TEXT_ATTRIBUTE_COMPOSED.getBytes(StandardCharsets.UTF_8),
                StreamUtils.copyToByteArray(wrappedRequest.getInputStream()));
    }

    @Test
    void givenNonWhitelistedContentType_thenPassOriginalRequestThrough() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/octet-stream");
        request.setRequestURI("/unicode-filter-test");
        request.addHeader(HEADER_NAME_DECOMPOSED, TEXT_ATTRIBUTE_DECOMPOSED);
        request.addParameter(PARAMETER_NAME_DECOMPOSED, TEXT_ATTRIBUTE_DECOMPOSED);
        request.setCookies(new Cookie("token", TEXT_ATTRIBUTE_DECOMPOSED));
        request.setContent(TEXT_ATTRIBUTE_DECOMPOSED.getBytes(StandardCharsets.UTF_8));
        request.addPart(new MockPart("file", TEXT_ATTRIBUTE_DECOMPOSED.getBytes(StandardCharsets.UTF_8)));

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        assertSame(request, filterChain.getRequest());
    }

    private HttpServletRequest filterWhitelistedRequest() throws ServletException, IOException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json;charset=UTF-8");
        request.setRequestURI("/unicode-filter-test");
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        request.addHeader(HEADER_NAME_DECOMPOSED, TEXT_ATTRIBUTE_DECOMPOSED);
        request.addParameter(PARAMETER_NAME_DECOMPOSED, TEXT_ATTRIBUTE_DECOMPOSED);
        request.setCookies(new Cookie("token", TEXT_ATTRIBUTE_DECOMPOSED));
        request.setContent(TEXT_ATTRIBUTE_DECOMPOSED.getBytes(StandardCharsets.UTF_8));
        request.addPart(new MockPart("file", TEXT_ATTRIBUTE_DECOMPOSED.getBytes(StandardCharsets.UTF_8)));

        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(request, response, filterChain);

        return (HttpServletRequest) filterChain.getRequest();
    }
}
