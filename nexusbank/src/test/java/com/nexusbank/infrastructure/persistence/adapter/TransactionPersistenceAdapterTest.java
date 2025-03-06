package com.nexusbank.infrastructure.persistence.adapter;

import com.nexusbank.domain.model.Money;
import com.nexusbank.domain.model.Transaction;
import com.nexusbank.domain.model.TransactionStatus;
import com.nexusbank.domain.model.TransactionType;
import com.nexusbank.infrastructure.persistence.entity.TransactionEntity;
import com.nexusbank.infrastructure.persistence.mapper.TransactionMapper;
import com.nexusbank.infrastructure.persistence.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionPersistenceAdapterTest {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private TransactionMapper transactionMapper;

  private TransactionPersistenceAdapter transactionPersistenceAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    transactionPersistenceAdapter = new TransactionPersistenceAdapter(transactionRepository, transactionMapper);
  }

  @Test
  void saveTransaction_shouldSaveAndReturnTransaction() {
    // Given
    String id = "tx-123";
    String accountId = "acc-456";
    Money amount = new Money(100.0, "USD");

    Transaction transaction = Transaction.createDeposit(id, accountId, amount);

    TransactionEntity transactionEntity = new TransactionEntity();
    transactionEntity.setId(id);
    transactionEntity.setAccountId(accountId);
    transactionEntity.setAmount(new BigDecimal("100.00"));
    transactionEntity.setCurrency("USD");
    transactionEntity.setType(TransactionType.DEPOSIT);
    transactionEntity.setStatus(TransactionStatus.COMPLETED);

    when(transactionMapper.toJpaEntity(transaction)).thenReturn(transactionEntity);
    when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);
    when(transactionMapper.toDomainEntity(transactionEntity)).thenReturn(transaction);

    // When
    Transaction savedTransaction = transactionPersistenceAdapter.saveTransaction(transaction);

    // Then
    assertNotNull(savedTransaction);
    assertEquals(id, savedTransaction.getId());
    assertEquals(accountId, savedTransaction.getAccountId());
    verify(transactionMapper).toJpaEntity(transaction);
    verify(transactionRepository).save(transactionEntity);
    verify(transactionMapper).toDomainEntity(transactionEntity);
  }

  @Test
  void loadTransactionsByAccountId_shouldReturnTransactionsList() {
    // Given
    String accountId = "acc-123";

    TransactionEntity entity1 = new TransactionEntity();
    entity1.setId("tx-1");
    entity1.setAccountId(accountId);
    entity1.setType(TransactionType.DEPOSIT);

    TransactionEntity entity2 = new TransactionEntity();
    entity2.setId("tx-2");
    entity2.setAccountId(accountId);
    entity2.setType(TransactionType.WITHDRAWAL);

    List<TransactionEntity> entities = Arrays.asList(entity1, entity2);

    Transaction tx1 = Transaction.createDeposit("tx-1", accountId, new Money(100.0, "USD"));
    Transaction tx2 = Transaction.createWithdrawal("tx-2", accountId, new Money(50.0, "USD"));

    when(transactionRepository.findByAccountId(accountId)).thenReturn(entities);
    when(transactionMapper.toDomainEntity(entity1)).thenReturn(tx1);
    when(transactionMapper.toDomainEntity(entity2)).thenReturn(tx2);

    // When
    List<Transaction> results = transactionPersistenceAdapter.loadTransactionsByAccountId(accountId);

    // Then
    assertEquals(2, results.size());
    assertEquals("tx-1", results.get(0).getId());
    assertEquals("tx-2", results.get(1).getId());
    verify(transactionRepository).findByAccountId(accountId);
    verify(transactionMapper).toDomainEntity(entity1);
    verify(transactionMapper).toDomainEntity(entity2);
  }
}