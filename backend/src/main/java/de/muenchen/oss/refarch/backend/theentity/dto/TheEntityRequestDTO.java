package de.muenchen.oss.refarch.backend.theentity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TheEntityRequestDTO(@NotNull @Size(min = 2, max = 8) String textAttribute) {
}
