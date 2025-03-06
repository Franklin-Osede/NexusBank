package com.nexusbank.infrastructure.persistence.entity;

import com.nexusbank.domain.model.TransactionStatus;
import com.nexusbank.domain.model.TransactionType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEntityTest {

  @Test
  void shouldCreateTransactionEntity() {
    // Given
    String id = "tx-123";
    String accountId = "acc-456";
    String targetAccountId = "acc-789";
    BigDecimal amount = new BigDecimal("50.00");
    String currency = "USD";
    TransactionType type = TransactionType.TRANSFER;
    TransactionStatus status = TransactionStatus.COMPLETED;
    String description = "Test transfer";
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt;

    // When
    TransactionEntity entity = new TransactionEntity();
    entity.setId(id);
    entity.setAccountId(accountId);
    entity.setTargetAccountId(targetAccountId);
    entity.setAmount(amount);
    entity.setCurrency(currency);
    entity.setType(type);
    entity.setStatus(status);
    entity.setDescription(description);
    entity.setCreatedAt(createdAt);
    entity.setUpdatedAt(updatedAt);

    // Then
    assertEquals(id, entity.getId());
    assertEquals(accountId, entity.getAccountId());
    assertEquals(targetAccountId, entity.getTargetAccountId());
    assertEquals(amount, entity.getAmount());
    assertEquals(currency, entity.getCurrency());
    assertEquals(type, entity.getType());
    assertEquals(status, entity.getStatus());
    assertEquals(description, entity.getDescription());
    assertEquals(createdAt, entity.getCreatedAt());
    assertEquals(updatedAt, entity.getUpdatedAt());
  }

  @Test
  void shouldImplementEqualsAndHashCodeBasedOnId() {
    // Given
    String commonId = "tx-123";

    TransactionEntity entity1 = new TransactionEntity();
    entity1.setId(commonId);
    entity1.setAccountId("acc-1");

    TransactionEntity entity2 = new TransactionEntity();
    entity2.setId(commonId);
    entity2.setAccountId("acc-2"); // Different account but same ID

    TransactionEntity entity3 = new TransactionEntity();
    entity3.setId("tx-456"); // Different ID
    entity3.setAccountId("acc-1");

    // Then
    assertEquals(entity1, entity2);
    assertEquals(entity1.hashCode(), entity2.hashCode());
    assertNotEquals(entity1, entity3);
    assertNotEquals(entity1.hashCode(), entity3.hashCode());
  }
}