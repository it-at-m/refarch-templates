package de.muenchen.refarch.configuration;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class SpringRestConfiguration implements RepositoryRestConfigurer {

    /**
     * The method returns a {@link Validator} to get correct validation error messages.
     *
     * @return A {@link LocalValidatorFactoryBean} to get correct validation error messages.
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * See {@link RepositoryRestConfigurer#configureRepositoryRestConfiguration}
     */
    @Override
    public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config, final CorsRegistry corsRegistry) {
        config.setRepositoryDetectionStrategy(RepositoryDetectionStrategies.DEFAULT);
    }

    /**
     * See {@link RepositoryRestConfigurer#configureValidatingRepositoryEventListener}
     */
    @Override
    @SuppressFBWarnings(justification = "Values are not further used, so they dont need to be saved in a variable")
    public void configureValidatingRepositoryEventListener(final ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("afterCreate", validator());
        validatingListener.addValidator("beforeCreate", validator());
        validatingListener.addValidator("afterSave", validator());
        validatingListener.addValidator("beforeSave", validator());
        validatingListener.addValidator("beforeLinkSave", validator());
        validatingListener.addValidator("afterLinkSave", validator());
    }

}
