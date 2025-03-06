package com.nexusbank.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Transaction entity that represents a movement of money in the system.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For JPA
public class Transaction {
  private String id;
  private String accountId;
  private String targetAccountId;
  private Money amount;
  private TransactionType type;
  private TransactionStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String description;

  private Transaction(String id, String accountId, String targetAccountId, Money amount,
      TransactionType type, TransactionStatus status, String description) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Transaction id cannot be null or empty");
    }
    if (accountId == null || accountId.trim().isEmpty()) {
      throw new IllegalArgumentException("Account id cannot be null or empty");
    }
    if (amount == null) {
      throw new IllegalArgumentException("Amount cannot be null");
    }
    if (type == null) {
      throw new IllegalArgumentException("Transaction type cannot be null");
    }
    if (status == null) {
      throw new IllegalArgumentException("Transaction status cannot be null");
    }

    this.id = id;
    this.accountId = accountId;
    this.targetAccountId = targetAccountId;
    this.amount = amount;
    this.type = type;
    this.status = status;
    this.description = description;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  /**
   * Factory method to create a deposit transaction.
   */
  public static Transaction createDeposit(String id, String accountId, Money amount) {
    return new Transaction(id, accountId, null, amount, TransactionType.DEPOSIT,
        TransactionStatus.COMPLETED, "Deposit to account");
  }

  /**
   * Factory method to create a withdrawal transaction.
   */
  public static Transaction createWithdrawal(String id, String accountId, Money amount) {
    return new Transaction(id, accountId, null, amount, TransactionType.WITHDRAWAL,
        TransactionStatus.COMPLETED, "Withdrawal from account");
  }

  /**
   * Factory method to create a transfer transaction.
   */
  public static Transaction createTransfer(String id, String sourceAccountId, String targetAccountId, Money amount) {
    if (targetAccountId == null || targetAccountId.trim().isEmpty()) {
      throw new IllegalArgumentException("Target account id cannot be null or empty for transfers");
    }

    return new Transaction(id, sourceAccountId, targetAccountId, amount, TransactionType.TRANSFER,
        TransactionStatus.COMPLETED, "Transfer between accounts");
  }

  /**
   * Factory method to create a pending transfer transaction.
   */
  public static Transaction createPendingTransfer(String id, String sourceAccountId, String targetAccountId,
      Money amount) {
    if (targetAccountId == null || targetAccountId.trim().isEmpty()) {
      throw new IllegalArgumentException("Target account id cannot be null or empty for transfers");
    }

    return new Transaction(id, sourceAccountId, targetAccountId, amount, TransactionType.TRANSFER,
        TransactionStatus.PENDING, "Pending transfer between accounts");
  }

  /**
   * Marks the transaction as completed.
   */
  public void markAsCompleted() {
    this.status = TransactionStatus.COMPLETED;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Marks the transaction as failed.
   */
  public void markAsFailed() {
    this.status = TransactionStatus.FAILED;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates the description of the transaction.
   */
  public void updateDescription(String description) {
    this.description = description;
    this.updatedAt = LocalDateTime.now();
  }
}