package com.nexusbank.infrastructure.persistence.adapter;

import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;
import com.nexusbank.infrastructure.persistence.entity.AccountEntity;
import com.nexusbank.infrastructure.persistence.mapper.AccountMapper;
import com.nexusbank.infrastructure.persistence.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountPersistenceAdapterTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AccountMapper accountMapper;

  private AccountPersistenceAdapter accountPersistenceAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    accountPersistenceAdapter = new AccountPersistenceAdapter(accountRepository, accountMapper);
  }

  @Test
  void loadAccount_shouldReturnAccountWhenFound() {
    // Given
    String accountId = "acc-123";
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setId(accountId);
    accountEntity.setBalance(new BigDecimal("100.00"));
    accountEntity.setCurrency("USD");

    Account domainAccount = Account.createNew(accountId, "user-123", "USD");
    domainAccount.deposit(new Money(100.0, "USD"));

    when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
    when(accountMapper.toDomainEntity(accountEntity)).thenReturn(domainAccount);

    // When
    Optional<Account> result = accountPersistenceAdapter.loadAccount(accountId);

    // Then
    assertTrue(result.isPresent());
    assertEquals(accountId, result.get().getId());
    verify(accountRepository).findById(accountId);
    verify(accountMapper).toDomainEntity(accountEntity);
  }

  @Test
  void loadAccount_shouldReturnEmptyWhenNotFound() {
    // Given
    String accountId = "non-existent-id";
    when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

    // When
    Optional<Account> result = accountPersistenceAdapter.loadAccount(accountId);

    // Then
    assertFalse(result.isPresent());
    verify(accountRepository).findById(accountId);
    verifyNoInteractions(accountMapper);
  }

  @Test
  void loadAccountsByUserId_shouldReturnAccountsList() {
    // Given
    String userId = "user-123";

    AccountEntity entity1 = new AccountEntity();
    entity1.setId("acc-1");
    entity1.setUserId(userId);

    AccountEntity entity2 = new AccountEntity();
    entity2.setId("acc-2");
    entity2.setUserId(userId);

    List<AccountEntity> entities = Arrays.asList(entity1, entity2);

    Account account1 = Account.createNew("acc-1", userId, "USD");
    Account account2 = Account.createNew("acc-2", userId, "USD");

    when(accountRepository.findByUserId(userId)).thenReturn(entities);
    when(accountMapper.toDomainEntity(entity1)).thenReturn(account1);
    when(accountMapper.toDomainEntity(entity2)).thenReturn(account2);

    // When
    List<Account> results = accountPersistenceAdapter.loadAccountsByUserId(userId);

    // Then
    assertEquals(2, results.size());
    assertEquals("acc-1", results.get(0).getId());
    assertEquals("acc-2", results.get(1).getId());
    verify(accountRepository).findByUserId(userId);
    verify(accountMapper).toDomainEntity(entity1);
    verify(accountMapper).toDomainEntity(entity2);
  }

  @Test
  void saveAccount_shouldSaveAndReturnAccount() {
    // Given
    String accountId = "acc-123";
    String userId = "user-123";
    Account account = Account.createNew(accountId, userId, "USD");
    account.deposit(new Money(200.0, "USD"));

    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setId(accountId);
    accountEntity.setUserId(userId);
    accountEntity.setBalance(new BigDecimal("200.00"));
    accountEntity.setCurrency("USD");

    when(accountMapper.toJpaEntity(account)).thenReturn(accountEntity);
    when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
    when(accountMapper.toDomainEntity(accountEntity)).thenReturn(account);

    // When
    Account savedAccount = accountPersistenceAdapter.saveAccount(account);

    // Then
    assertNotNull(savedAccount);
    assertEquals(accountId, savedAccount.getId());
    assertEquals(userId, savedAccount.getUserId());
    verify(accountMapper).toJpaEntity(account);
    verify(accountRepository).save(accountEntity);
    verify(accountMapper).toDomainEntity(accountEntity);
  }
}