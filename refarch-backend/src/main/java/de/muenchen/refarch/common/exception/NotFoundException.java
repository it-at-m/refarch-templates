package de.muenchen.refarch.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Exception, wenn Daten nicht gefunden werden können. */
public class NotFoundException extends ResponseStatusException {
    /**
     * NotFoundException constructor
     *
     * @param message Exception msg
     */
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
