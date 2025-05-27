package de.muenchen.refarch.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIDocumentationConfig {

    private OpenAPIProperties openAPIProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(openAPIProperties.getTitle()).description(openAPIProperties.getDescription()).version(openAPIProperties.getVersion()));
    }
}
