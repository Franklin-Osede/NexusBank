package com.nexusbank.infrastructure.rest.dto.response;

import com.nexusbank.domain.model.Transaction;

import java.time.LocalDateTime;

/**
 * DTO for transaction information.
 */
public record TransactionResponse(
    String id,
    String accountId,
    String targetAccountId,
    BalanceResponse amount,
    String type,
    String status,
    String description,
    LocalDateTime createdAt) {
  /**
   * Factory method to create a TransactionResponse from domain model.
   */
  public static TransactionResponse fromDomain(Transaction transaction) {
    return new TransactionResponse(
        transaction.getId(),
        transaction.getAccountId(),
        transaction.getTargetAccountId(),
        new BalanceResponse(transaction.getAmount().toDouble(), transaction.getAmount().getCurrency()),
        transaction.getType().toString(),
        transaction.getStatus().toString(),
        transaction.getDescription(),
        transaction.getCreatedAt());
  }
}