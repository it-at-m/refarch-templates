package de.muenchen.refarch.security;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Each possible authority in this project is represented by an enum.
 * The enums are used within the {@link PagingAndSortingRepository}
 * in the annotation e.g. {@link PreAuthorize}.
 */
public final class Authorities {
    public static final String HAS_ROLE_READER = "hasRole(\"reader\")";
    public static final String HAS_ROLE_WRITER = "hasRole(\"writer\")";

    private Authorities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
