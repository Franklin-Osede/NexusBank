package com.nexusbank.infrastructure.rest.dto.response;

/**
 * DTO for account balance information.
 */
public record BalanceResponse(
    Double amount,
    String currency) {
}