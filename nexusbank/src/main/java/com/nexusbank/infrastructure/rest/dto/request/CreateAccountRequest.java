package com.nexusbank.infrastructure.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a new account.
 */
public record CreateAccountRequest(
    @NotBlank(message = "User ID is required") String userId,

    @DecimalMin(value = "0.0", inclusive = true, message = "Initial deposit must be zero or positive") Double initialDeposit) {
}