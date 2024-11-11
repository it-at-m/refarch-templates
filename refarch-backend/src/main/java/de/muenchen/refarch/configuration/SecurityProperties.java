package de.muenchen.refarch.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(String requestLogging, String userInfoUri) {

}
