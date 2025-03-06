package com.nexusbank.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un nuevo usuario.
 */
@Schema(description = "Datos para crear un nuevo usuario")
public record CreateUserRequest(
        @Schema(description = "Nombre completo del usuario", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

        @Schema(description = "Correo electrónico del usuario", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

        @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password) {
}