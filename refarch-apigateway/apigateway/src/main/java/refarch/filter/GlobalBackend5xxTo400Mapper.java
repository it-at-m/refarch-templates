/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package refarch.filter;

import com.hazelcast.org.apache.commons.codec.binary.StringUtils;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This {@link GlobalFilter} replaces the body by a generic error body, when a server responses
 * with a {@link HttpStatus#INTERNAL_SERVER_ERROR}.
 */
@Component
@Slf4j
public class GlobalBackend5xxTo400Mapper implements GlobalFilter, Ordered {

    public static final int ORDER_GLOBAL_FILTER = -3;

    /**
     * Variable entscheidet, ob alle 5xx Fehler auf 400 gemappt werden sollen.
     **/
    @Value("${config.map5xxto400: true}")
    private boolean MAP_5xx_TO_400;

    static final String GENERIC_ERROR_400 = "{ \"status\":400, \"error\":\"Bad Request\" }";
    static final String GENERIC_ERROR_500 = "{ \"status\":500, \"error\":\"Internal Server Error\" }";

    @Override
    public int getOrder() {
        return ORDER_GLOBAL_FILTER;
    }

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        final String EMPTY_JSON_OBJECT = "{}";
        final ServerHttpResponse response = exchange.getResponse();
        final ServerHttpRequest request = exchange.getRequest();
        final DataBufferFactory dataBufferFactory = response.bufferFactory();

        final ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                final HttpStatusCode responseHttpStatus = getDelegate().getStatusCode();

                final Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                if (body instanceof Flux && responseHttpStatus.is5xxServerError()) {

                    return super.writeWith(flux.buffer().map(
                            // replace old body represented by dataBuffer by the new one

                            dataBuffer -> {
                                // Log-Ausgabe
                                DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffer);
                                byte[] content = new byte[joinedBuffers.readableByteCount()];
                                joinedBuffers.read(content);
                                String responseBody = new String(content, StandardCharsets.UTF_8);
                                log.error("Error: 5xx vom Backend:  requestId: {}, method: {}, url: {}, \nresponse body :{}, statusCode: {}", request.getId(),
                                        request.getMethod(), request.getURI(), responseBody, responseHttpStatus);

                                // Response manipulieren
                                final DataBuffer newDataBuffer;
                                if (MAP_5xx_TO_400) {
                                    getDelegate().setStatusCode(HttpStatus.BAD_REQUEST);
                                    newDataBuffer = dataBufferFactory.wrap(
                                            StringUtils.getBytesUtf8(ObjectUtils.defaultIfNull(GENERIC_ERROR_400, EMPTY_JSON_OBJECT)));
                                } else {
                                    getDelegate().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                                    newDataBuffer = dataBufferFactory.wrap(
                                            StringUtils.getBytesUtf8(ObjectUtils.defaultIfNull(GENERIC_ERROR_500, EMPTY_JSON_OBJECT)));
                                }

                                getDelegate().getHeaders().setContentLength(newDataBuffer.readableByteCount());
                                getDelegate().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                                return newDataBuffer;
                            }));
                }
                return super.writeWith(body);
            }
        };

        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }
}
