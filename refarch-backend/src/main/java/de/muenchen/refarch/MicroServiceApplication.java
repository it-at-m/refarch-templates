package de.muenchen.refarch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application class for starting the microservice.
 */
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class MicroServiceApplication {
    public static void main(final String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
}
