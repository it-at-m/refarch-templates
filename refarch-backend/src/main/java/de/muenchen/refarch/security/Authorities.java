package de.muenchen.refarch.security;

import lombok.experimental.UtilityClass;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Each possible authority in this project is represented by an enum.
 * The enums are used within the {@link PagingAndSortingRepository}
 * in the annotation e.g. {@link PreAuthorize}.
 */
@UtilityClass
public class Authorities {
    public static final String HAS_AUTHORITY_READ_THEENTITY = "hasAuthority(READ_THEENTITY)";
    public static final String HAS_AUTHORITY_WRITE_THEENTITY = "hasAuthority(WRITE_THEENTITY)";
    public static final String HAS_AUTHORITY_DELETE_THEENTIT = "hasAuthority(DELETE_THEENTIT)";
    // add your authorities here and also add these new authorities to sso-authorisation.json.
}
