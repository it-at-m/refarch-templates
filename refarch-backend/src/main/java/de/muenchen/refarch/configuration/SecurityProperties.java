package de.muenchen.refarch.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "security")
@Validated
public record SecurityProperties(@NotBlank String requestLogging, @NotBlank String userInfoUri) {

}
