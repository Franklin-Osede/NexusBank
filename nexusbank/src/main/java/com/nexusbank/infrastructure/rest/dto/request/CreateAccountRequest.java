package com.nexusbank.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear una nueva cuenta bancaria.
 */
@Schema(description = "Datos para crear una nueva cuenta bancaria")
public record CreateAccountRequest(
        @Schema(description = "ID del usuario propietario de la cuenta", example = "user-123", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "User ID is required") String userId,

        @Schema(description = "Depósito inicial para la cuenta (opcional, puede ser 0)", example = "100.0", defaultValue = "0.0") @DecimalMin(value = "0.0", inclusive = true, message = "Initial deposit must be zero or positive") Double initialDeposit) {
}