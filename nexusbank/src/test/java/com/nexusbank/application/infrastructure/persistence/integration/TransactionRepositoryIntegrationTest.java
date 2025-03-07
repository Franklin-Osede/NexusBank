package com.nexusbank.application.infrastructure.persistence.integration;

import com.nexusbank.application.infrastructure.persistence.container.TestDatabaseConfig;
import com.nexusbank.domain.model.TransactionStatus;
import com.nexusbank.domain.model.TransactionType;
import com.nexusbank.infrastructure.persistence.entity.TransactionEntity;
import com.nexusbank.infrastructure.persistence.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = TestDatabaseConfig.class)
public class TransactionRepositoryIntegrationTest {

  @Autowired
  private TransactionRepository transactionRepository;

  @Test
  public void testSaveAndFindTransaction() {
    // Crear una nueva TransactionEntity
    TransactionEntity transaction = new TransactionEntity();
    transaction.setId("tx-123");
    transaction.setAccountId("acc-123");
    transaction.setTargetAccountId("acc-456");
    transaction.setAmount(new BigDecimal("150.00"));
    transaction.setCurrency("USD");
    transaction.setType(TransactionType.DEPOSIT);
    transaction.setStatus(TransactionStatus.COMPLETED);
    transaction.setDescription("Deposit transaction");
    transaction.setCreatedAt(LocalDateTime.now());
    transaction.setUpdatedAt(LocalDateTime.now());

    // Guardar la transacción
    transactionRepository.save(transaction);

    // Recuperar la transacción por ID
    Optional<TransactionEntity> retrieved = transactionRepository.findById("tx-123");
    assertTrue(retrieved.isPresent());
    assertEquals("acc-123", retrieved.get().getAccountId());
    assertEquals("acc-456", retrieved.get().getTargetAccountId());
    assertEquals(new BigDecimal("150.00"), retrieved.get().getAmount());
    assertEquals("USD", retrieved.get().getCurrency());
    assertEquals(TransactionType.DEPOSIT, retrieved.get().getType());
    assertEquals(TransactionStatus.COMPLETED, retrieved.get().getStatus());
    assertEquals("Deposit transaction", retrieved.get().getDescription());
  }
}