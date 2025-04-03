package de.muenchen.refarch.configuration.nfcconverter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class NfcRequestFilter extends OncePerRequestFilter {

    /**
     * Name of the property for configuring the white list for content types.
     *
     * @see #getContentTypes()
     * @see #setContentTypes(String)
     */
    public static final String CONTENTTYPES_PROPERTY = "contentTypes";

    private final Set<String> contentTypes = new HashSet<>();

    /**
     * @return The property <em>contentTypes</em>
     */
    public String getContentTypes() {
        return String.join("; ", this.contentTypes);
    }

    /**
     * @param contentTypes The property <em>contentTypes</em>
     */
    @Autowired(required = false)
    public void setContentTypes(final String contentTypes) {
        this.contentTypes.clear();
        if (StringUtils.isEmpty(contentTypes)) {
            log.info("Disabling context-type filter.");

        } else {
            final Set<String> newContentTypes = Arrays.stream(contentTypes.split(";")).map(String::trim)
                    .collect(Collectors.toSet());
            this.contentTypes.addAll(newContentTypes);
            log.info("Enabled content-type filtering to NFC for: {}", getContentTypes());

        }
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Request-Type={}", request.getClass().getName());
        log.debug("Intercepting request for URI {}", request.getRequestURI());

        final String contentType = request.getContentType();
        log.debug("ContentType for request with URI: \"{}\"", contentType);
        if (contentTypes.contains(contentType)) {
            log.debug("Processing request {}.", request.getRequestURI());
            filterChain.doFilter(new NfcRequest(request, contentTypes), response);
        } else {
            log.debug("Skip processing of HTTP request since it's content type \"{}\" is not in whitelist.", contentType);
            filterChain.doFilter(request, response);
        }
    }

}
