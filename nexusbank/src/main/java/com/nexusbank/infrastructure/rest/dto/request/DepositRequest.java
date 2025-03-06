package com.nexusbank.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para realizar un depósito en una cuenta.
 */
@Schema(description = "Datos para realizar un depósito en una cuenta")
public record DepositRequest(
        @Schema(description = "Monto a depositar (debe ser mayor que cero)", example = "100.0", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero") Double amount) {
}