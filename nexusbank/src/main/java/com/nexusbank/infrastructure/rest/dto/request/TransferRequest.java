package com.nexusbank.infrastructure.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for transferring money between accounts.
 */
public record TransferRequest(
    @NotBlank(message = "Target account ID is required") String targetAccountId,

    @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero") Double amount) {
}