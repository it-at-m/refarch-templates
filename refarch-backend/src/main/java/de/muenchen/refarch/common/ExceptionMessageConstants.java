package de.muenchen.refarch.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageConstants {
    public static final String MSG_NOT_FOUND = "Could not find entity with id %s";
}
