package de.muenchen.refarch.configuration.filter.nfcconverter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>
 * Spring filter that performs an NFC normalization of all <em>safe textual</em> content.
 * </p>
 *
 * <strong>Please note:</strong>
 * <ul>
 * <li>All data streams associated with multipart requests are not normalized according to NFC.
 * The reason for this is that binary data streams are transferred here and these are generally not
 * simple text.
 * If necessary or useful, the application logic or a suitable library can or must carry out NFC
 * normalization.</li>
 * <li>NFC normalization can only be performed at the character level
 * and the conversion of binary data streams requires knowledge of the data format,
 * which implies knowledge of the charset used.
 * This makes NFC normalization in a generic filter seem sensible.</li>
 * </ul>
 *
 * @see java.text.Normalizer
 * @see HttpServletRequest#getPart(String)
 * @see HttpServletRequest#getParts()
 */
@Component
@FilterRegistration(urlPatterns = "/*")
@Slf4j
public class NfcRequestFilter extends OncePerRequestFilter {

    private static final Set<String> CONTENT_TYPES = new HashSet<>(Arrays.asList("text/plain", "application/json", "text/html"));

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Request-Type={}", request.getClass().getName());
        log.debug("Intercepting request for URI {}", request.getRequestURI());

        final String contentType = request.getContentType();
        log.debug("ContentType for request with URI: \"{}\"", contentType);
        if (contentType != null && CONTENT_TYPES.stream().anyMatch(contentType::startsWith)) {
            log.debug("Processing request {}.", request.getRequestURI());
            filterChain.doFilter(new NfcRequest(request), response);
        } else {
            log.debug("Skip processing of HTTP request since it's content type \"{}\" is not in whitelist.", contentType);
            filterChain.doFilter(request, response);
        }
    }

}
