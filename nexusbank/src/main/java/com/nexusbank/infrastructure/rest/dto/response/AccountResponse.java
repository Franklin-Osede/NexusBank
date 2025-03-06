package com.nexusbank.infrastructure.rest.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for account information.
 */
public record AccountResponse(
    String id,
    String userId,
    BalanceResponse balance,
    boolean active,
    LocalDateTime createdAt) {
  /**
   * Factory method to create an AccountResponse from domain model.
   */
  public static AccountResponse fromDomain(com.nexusbank.domain.model.Account account) {
    return new AccountResponse(
        account.getId(),
        account.getUserId(),
        new BalanceResponse(account.getBalance().toDouble(), account.getBalance().getCurrency()),
        account.isActive(),
        account.getCreatedAt());
  }
}