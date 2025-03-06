package com.nexusbank.application.service;

import com.nexusbank.application.port.out.LoadAccountPort;
import com.nexusbank.application.port.out.LoadUserPort;
import com.nexusbank.application.port.out.SaveAccountPort;
import com.nexusbank.domain.exception.AccountNotFoundException;
import com.nexusbank.domain.exception.UserNotFoundException;
import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;
import com.nexusbank.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

  @Mock
  private SaveAccountPort saveAccountPort;

  @Mock
  private LoadAccountPort loadAccountPort;

  @Mock
  private LoadUserPort loadUserPort;

  private AccountService accountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    accountService = new AccountService(saveAccountPort, loadAccountPort, loadUserPort);
  }

  @Test
  void createAccount_shouldCreateAndSaveAccount() {
    // Given
    String userId = "user-123";
    Double initialDeposit = 100.0;

    User mockUser = User.createNew(userId, "Test User", "test@example.com", "hashed_pwd");
    when(loadUserPort.loadUser(userId)).thenReturn(Optional.of(mockUser));

    // Configuramos el comportamiento del saveAccountPort sin crear una variable
    // innecesaria
    when(saveAccountPort.saveAccount(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // When
    Account result = accountService.createAccount(userId, initialDeposit);

    // Then
    assertNotNull(result);
    assertEquals(userId, result.getUserId());
    assertEquals("USD", result.getBalance().getCurrency());

    // Verify the correct initial deposit was made - usando toDouble() en lugar de
    // getAmount()
    assertEquals(100.0, result.getBalance().toDouble());

    // Verify interactions with ports
    verify(loadUserPort).loadUser(userId);

    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    verify(saveAccountPort).saveAccount(accountCaptor.capture());

    Account capturedAccount = accountCaptor.getValue();
    assertEquals(userId, capturedAccount.getUserId());
  }

  @Test
  void createAccount_shouldThrowExceptionWhenUserNotFound() {
    // Given
    String userId = "non-existent-user";
    Double initialDeposit = 100.0;

    when(loadUserPort.loadUser(userId)).thenReturn(Optional.empty());

    // When & Then
    UserNotFoundException exception = assertThrows(
        UserNotFoundException.class,
        () -> accountService.createAccount(userId, initialDeposit));

    assertEquals("User with id non-existent-user not found", exception.getMessage());
    verify(loadUserPort).loadUser(userId);
    verify(saveAccountPort, never()).saveAccount(any(Account.class));
  }

  @Test
  void getAccountById_shouldReturnAccountWhenFound() {
    // Given
    String accountId = "account-123";
    Account mockAccount = Account.createNew(accountId, "user-123", "USD");
    mockAccount.deposit(new Money(500.0, "USD"));

    when(loadAccountPort.loadAccount(accountId)).thenReturn(Optional.of(mockAccount));

    // When
    Account result = accountService.getAccountById(accountId);

    // Then
    assertNotNull(result);
    assertEquals(accountId, result.getId());
    // Usando toDouble() para la comparación correcta
    assertEquals(500.0, result.getBalance().toDouble());
    verify(loadAccountPort).loadAccount(accountId);
  }

  @Test
  void getAccountById_shouldThrowExceptionWhenAccountNotFound() {
    // Given
    String accountId = "non-existent-account";
    when(loadAccountPort.loadAccount(accountId)).thenReturn(Optional.empty());

    // When & Then
    AccountNotFoundException exception = assertThrows(
        AccountNotFoundException.class,
        () -> accountService.getAccountById(accountId));

    assertEquals("Account with id non-existent-account not found", exception.getMessage());
    verify(loadAccountPort).loadAccount(accountId);
  }

  @Test
  void getAccountsByUserId_shouldReturnAccountsList() {
    // Given
    String userId = "user-123";
    Account account1 = Account.createNew("account-1", userId, "USD");
    Account account2 = Account.createNew("account-2", userId, "USD");
    List<Account> expectedAccounts = Arrays.asList(account1, account2);

    when(loadAccountPort.loadAccountsByUserId(userId)).thenReturn(expectedAccounts);

    // When
    List<Account> result = accountService.getAccountsByUserId(userId);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(expectedAccounts, result);
    verify(loadAccountPort).loadAccountsByUserId(userId);
  }
}