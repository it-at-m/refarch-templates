package de.muenchen.refarch.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenAPIDocumentationConfiguration {

    private final OpenAPIProperties openAPIProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(openAPIProperties.getName()).description(openAPIProperties.getDescription()).version(openAPIProperties.getVersion()));
    }
}
