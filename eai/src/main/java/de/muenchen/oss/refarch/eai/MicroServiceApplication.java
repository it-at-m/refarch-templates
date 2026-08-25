package de.muenchen.oss.refarch.eai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class MicroServiceApplication {
    static void main(final String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
}
