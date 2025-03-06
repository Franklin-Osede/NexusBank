package com.nexusbank.application.infrastructure.persistence.integration;

import com.nexusbank.application.infrastructure.persistence.container.TestDatabaseConfig;
import com.nexusbank.infrastructure.persistence.entity.AccountEntity;
import com.nexusbank.infrastructure.persistence.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(initializers = TestDatabaseConfig.class)
public class AccountRepositoryIntegrationTest {

  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void testSaveAndFindAccount() {
    // Crear una nueva AccountEntity
    AccountEntity account = new AccountEntity();
    account.setId("acc-123");
    account.setUserId("user-123");
    account.setBalance(new BigDecimal("1000.00"));
    account.setCurrency("USD");
    account.setActive(true);
    account.setCreatedAt(LocalDateTime.now());
    account.setUpdatedAt(LocalDateTime.now());

    // Guardar la cuenta
    accountRepository.save(account);

    // Recuperar la cuenta por ID
    Optional<AccountEntity> retrieved = accountRepository.findById("acc-123");
    assertTrue(retrieved.isPresent());
    assertEquals("user-123", retrieved.get().getUserId());
    assertEquals(new BigDecimal("1000.00"), retrieved.get().getBalance());
    assertEquals("USD", retrieved.get().getCurrency());
  }
}
