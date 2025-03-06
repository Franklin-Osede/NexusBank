package com.nexusbank.infrastructure.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for depositing money into an account.
 */
public record DepositRequest(
    @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero") Double amount) {
}