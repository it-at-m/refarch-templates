package de.muenchen.oss.refarch.eai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@SuppressWarnings("PMD.UseUtilityClass")
public class MicroServiceApplication {
    /* package */ static void main(final String... args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
}
