/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.refarch.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * This class supplies the endpoint which provides the gui.
 *
 * The default path to the gui entry point is "classpath:/static/index.html".
 */
@Configuration
@Slf4j
public class GuiConfiguration {

    /**
     * A router which returns the index.html as a resource.
     *
     * @param indexHtml The path to the index.html which serves as the starting point.
     * @return the index.html as a resource.
     */
    @Bean
    public RouterFunction<ServerResponse> indexRouter(
            @Value("classpath:/static/index.html") final Resource indexHtml) {
        log.debug("Location gui entry point: {}", indexHtml);
        return route(GET("/"),
                request -> ok().contentType(MediaType.TEXT_HTML)
                        .bodyValue(indexHtml));
    }

}
