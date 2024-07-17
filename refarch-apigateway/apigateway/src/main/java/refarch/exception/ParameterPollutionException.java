/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package refarch.exception;

import refarch.filter.GlobalRequestParameterPollutionFilter;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Used in {@link GlobalRequestParameterPollutionFilter} to signal a possible parameter pollution
 * attack.
 */
@ResponseStatus(
        code = HttpStatus.BAD_REQUEST,
        reason = "parameter pollution"
)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ParameterPollutionException extends RuntimeException {
    // default Ctor
}
