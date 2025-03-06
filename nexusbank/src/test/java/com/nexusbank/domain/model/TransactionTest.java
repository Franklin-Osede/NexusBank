package com.nexusbank.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

  @Test
  void shouldCreateDepositTransaction() {
    // Given
    String id = UUID.randomUUID().toString();
    String accountId = UUID.randomUUID().toString();
    Money amount = new Money(new BigDecimal("100.00"), "USD");

    // When
    Transaction transaction = Transaction.createDeposit(id, accountId, amount);

    // Then
    assertEquals(id, transaction.getId());
    assertEquals(accountId, transaction.getAccountId());
    assertNull(transaction.getTargetAccountId());
    assertEquals(amount, transaction.getAmount());
    assertEquals(TransactionType.DEPOSIT, transaction.getType());
    assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
    assertNotNull(transaction.getCreatedAt());
  }

  @Test
  void shouldCreateWithdrawalTransaction() {
    // Given
    String id = UUID.randomUUID().toString();
    String accountId = UUID.randomUUID().toString();
    Money amount = new Money(new BigDecimal("50.00"), "USD");

    // When
    Transaction transaction = Transaction.createWithdrawal(id, accountId, amount);

    // Then
    assertEquals(id, transaction.getId());
    assertEquals(accountId, transaction.getAccountId());
    assertNull(transaction.getTargetAccountId());
    assertEquals(amount, transaction.getAmount());
    assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
    assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
    assertNotNull(transaction.getCreatedAt());
  }

  @Test
  void shouldCreateTransferTransaction() {
    // Given
    String id = UUID.randomUUID().toString();
    String sourceAccountId = UUID.randomUUID().toString();
    String targetAccountId = UUID.randomUUID().toString();
    Money amount = new Money(new BigDecimal("75.00"), "USD");

    // When
    Transaction transaction = Transaction.createTransfer(id, sourceAccountId, targetAccountId, amount);

    // Then
    assertEquals(id, transaction.getId());
    assertEquals(sourceAccountId, transaction.getAccountId());
    assertEquals(targetAccountId, transaction.getTargetAccountId());
    assertEquals(amount, transaction.getAmount());
    assertEquals(TransactionType.TRANSFER, transaction.getType());
    assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
    assertNotNull(transaction.getCreatedAt());
  }

  @Test
  void shouldCreatePendingTransaction() {
    // Given
    String id = UUID.randomUUID().toString();
    String accountId = UUID.randomUUID().toString();
    String targetAccountId = UUID.randomUUID().toString();
    Money amount = new Money(new BigDecimal("200.00"), "USD");

    // When
    Transaction transaction = Transaction.createPendingTransfer(id, accountId, targetAccountId, amount);

    // Then
    assertEquals(TransactionStatus.PENDING, transaction.getStatus());
  }

  @Test
  void shouldMarkTransactionAsCompleted() {
    // Given
    Transaction transaction = Transaction.createPendingTransfer(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        new Money(new BigDecimal("100.00"), "USD"));

    // When
    transaction.markAsCompleted();

    // Then
    assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
  }

  @Test
  void shouldMarkTransactionAsFailed() {
    // Given
    Transaction transaction = Transaction.createPendingTransfer(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        new Money(new BigDecimal("100.00"), "USD"));

    // When
    transaction.markAsFailed();

    // Then
    assertEquals(TransactionStatus.FAILED, transaction.getStatus());
  }
}