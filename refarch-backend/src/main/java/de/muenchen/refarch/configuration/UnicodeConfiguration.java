package de.muenchen.refarch.configuration;

import de.muenchen.refarch.configuration.nfcconverter.NfcRequestFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * <p>
 * <em>Example</em> of configuration of the NFC request filter
 * </p>
 * <ul>
 * <li>All requests that are sent to URIs under <em>/*</em> are filtered.</li>
 * <li>Filter is contained in bean <em>nfcRequestFilter</em>.</li>
 * <li>Only requests with Content-Types <em>text/plain</em>; <em>application/json</em>
 * and <em>text/html</em> are filtered.</li>
 * </ul>
 */
@Configuration
public class UnicodeConfiguration {

    private static final String NFC_FILTER_NAME = "nfcRequestFilter";

    private static final String NFC_WHITE_LIST = "text/plain; application/json; application/hal+json; text/html";

    private static final String[] NFC_URLS = ArrayUtils.toArray("/*");

    @Bean
    public FilterRegistrationBean<NfcRequestFilter> nfcRequestFilterRegistration(final NfcRequestFilter nfcRequestFilter) {

        final FilterRegistrationBean<NfcRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(nfcRequestFilter);
        registration.setName(NFC_FILTER_NAME);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        registration.setAsyncSupported(false);

        // Set the URLs to which filters are to be applied.
        registration.addUrlPatterns(NFC_URLS);

        // Setting the white list of ContentTypes
        registration.addInitParameter(NfcRequestFilter.CONTENTTYPES_PROPERTY, NFC_WHITE_LIST);

        return registration;

    }

}
