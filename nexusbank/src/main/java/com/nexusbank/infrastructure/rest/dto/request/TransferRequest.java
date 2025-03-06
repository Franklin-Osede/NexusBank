package com.nexusbank.infrastructure.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para realizar una transferencia entre cuentas.
 */
@Schema(description = "Datos para realizar una transferencia entre cuentas")
public record TransferRequest(
        @Schema(description = "ID de la cuenta destino", example = "account-456", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Target account ID is required") String targetAccountId,

        @Schema(description = "Monto a transferir (debe ser mayor que cero)", example = "100.0", requiredMode = Schema.RequiredMode.REQUIRED) @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero") Double amount) {
}