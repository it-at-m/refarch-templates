package de.muenchen.refarch.configuration.filter;

import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * This class provides the {@link ForwardedHeaderFilter} to handle
 * the headers of type "Forwarded" and "X-Forwarded-*".
 */
@Configuration
public class ForwardedHeaderFilterConfiguration {

    @Bean
    @FilterRegistration(urlPatterns = "/*")
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

}
