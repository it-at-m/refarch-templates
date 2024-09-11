package de.muenchen.refarch.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record TheEntityDTO(UUID id, @NotNull @Size(min = 2, max = 8) String textAttribute) {
}
