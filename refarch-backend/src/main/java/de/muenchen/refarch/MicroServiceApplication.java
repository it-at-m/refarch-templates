package de.muenchen.refarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Application class for starting the microservice.
 */
@EnableJpaRepositories(
        basePackages = {
                "de.muenchen.refarch"
        }
)
@SpringBootApplication(scanBasePackages = "de.muenchen.refarch")
@SuppressWarnings("PMD.UseUtilityClass")
public class MicroServiceApplication {
    public static void main(final String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
}
