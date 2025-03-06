package com.nexusbank.infrastructure.rest.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Datos de un usuario")
public record UserResponse(
                @Schema(description = "ID del usuario", example = "user-123") String id,
                @Schema(description = "Nombre del usuario", example = "John Doe") String name,
                @Schema(description = "Correo electrónico", example = "john.doe@example.com") String email,
                @Schema(description = "Fecha de creación", example = "2025-03-06T10:15:30") LocalDateTime createdAt,
                @Schema(description = "Fecha de última actualización", example = "2025-03-06T10:15:30") LocalDateTime updatedAt) {
}
