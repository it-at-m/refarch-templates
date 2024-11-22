package de.muenchen.refarch.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Exception if data cannot be found. */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class NotFoundException extends ResponseStatusException {
    /**
     * NotFoundException constructor
     *
     * @param message Exception message
     */
    public NotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
