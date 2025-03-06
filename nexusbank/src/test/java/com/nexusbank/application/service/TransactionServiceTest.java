package com.nexusbank.application.service;

import com.nexusbank.application.port.out.LoadAccountPort;
import com.nexusbank.application.port.out.SaveAccountPort;
import com.nexusbank.application.port.out.SaveTransactionPort;
import com.nexusbank.domain.exception.AccountNotFoundException;
import com.nexusbank.domain.exception.InsufficientBalanceException;
import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;
import com.nexusbank.domain.model.Transaction;
import com.nexusbank.domain.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

  @Mock
  private LoadAccountPort loadAccountPort;

  @Mock
  private SaveAccountPort saveAccountPort;

  @Mock
  private SaveTransactionPort saveTransactionPort;

  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    transactionService = new TransactionService(loadAccountPort, saveAccountPort, saveTransactionPort);
  }

  @Test
  void depositMoney_shouldDepositAndCreateTransaction() {
    // Given
    String accountId = "account-123";
    Double amount = 100.0;

    Account account = Account.createNew(accountId, "user-123", "USD");
    account.deposit(new Money(50.0, "USD")); // Initial balance

    when(loadAccountPort.loadAccount(accountId)).thenReturn(Optional.of(account));
    when(saveAccountPort.saveAccount(any(Account.class))).thenReturn(account);
    when(saveTransactionPort.saveTransaction(any(Transaction.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    Transaction result = transactionService.depositMoney(accountId, amount);

    // Then
    assertNotNull(result);
    assertEquals(accountId, result.getAccountId());
    assertEquals(TransactionType.DEPOSIT, result.getType());
    assertEquals(100.0, result.getAmount().getAmount());

    // Verify the account balance was updated
    assertEquals(150.0, account.getBalance().getAmount());

    // Verify interactions with ports
    verify(loadAccountPort).loadAccount(accountId);
    verify(saveAccountPort).saveAccount(account);

    ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
    verify(saveTransactionPort).saveTransaction(transactionCaptor.capture());

    Transaction capturedTransaction = transactionCaptor.getValue();
    assertEquals(accountId, capturedTransaction.getAccountId());
    assertEquals(100.0, capturedTransaction.getAmount().getAmount());
  }

  @Test
  void depositMoney_shouldThrowExceptionWhenAccountNotFound() {
    // Given
    String accountId = "non-existent-account";
    Double amount = 100.0;

    when(loadAccountPort.loadAccount(accountId)).thenReturn(Optional.empty());

    // When & Then
    AccountNotFoundException exception = assertThrows(
        AccountNotFoundException.class,
        () -> transactionService.depositMoney(accountId, amount));

    assertEquals("Account with id non-existent-account not found", exception.getMessage());
    verify(loadAccountPort).loadAccount(accountId);
    verify(saveAccountPort, never()).saveAccount(any(Account.class));
    verify(saveTransactionPort, never()).saveTransaction(any(Transaction.class));
  }

  @Test
  void transferMoney_shouldTransferBetweenAccountsAndCreateTransaction() {
    // Given
    String sourceAccountId = "source-account";
    String targetAccountId = "target-account";
    Double amount = 100.0;

    Account sourceAccount = Account.createNew(sourceAccountId, "user-123", "USD");
    sourceAccount.deposit(new Money(200.0, "USD")); // Initial balance

    Account targetAccount = Account.createNew(targetAccountId, "user-456", "USD");
    targetAccount.deposit(new Money(50.0, "USD")); // Initial balance

    when(loadAccountPort.loadAccount(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
    when(loadAccountPort.loadAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));
    when(saveAccountPort.saveAccount(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(saveTransactionPort.saveTransaction(any(Transaction.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // When
    Transaction result = transactionService.transferMoney(sourceAccountId, targetAccountId, amount);

    // Then
    assertNotNull(result);
    assertEquals(sourceAccountId, result.getAccountId());
    assertEquals(targetAccountId, result.getTargetAccountId());
    assertEquals(TransactionType.TRANSFER, result.getType());
    assertEquals(100.0, result.getAmount().getAmount());

    // Verify the account balances were updated
    assertEquals(100.0, sourceAccount.getBalance().getAmount());
    assertEquals(150.0, targetAccount.getBalance().getAmount());

    // Verify interactions with ports
    verify(loadAccountPort).loadAccount(sourceAccountId);
    verify(loadAccountPort).loadAccount(targetAccountId);
    verify(saveAccountPort).saveAccount(sourceAccount);
    verify(saveAccountPort).saveAccount(targetAccount);

    ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
    verify(saveTransactionPort).saveTransaction(transactionCaptor.capture());

    Transaction capturedTransaction = transactionCaptor.getValue();
    assertEquals(sourceAccountId, capturedTransaction.getAccountId());
    assertEquals(targetAccountId, capturedTransaction.getTargetAccountId());
    assertEquals(100.0, capturedTransaction.getAmount().getAmount());
  }

  @Test
  void transferMoney_shouldThrowExceptionWhenSourceAccountNotFound() {
    // Given
    String sourceAccountId = "non-existent-account";
    String targetAccountId = "target-account";
    Double amount = 100.0;

    when(loadAccountPort.loadAccount(sourceAccountId)).thenReturn(Optional.empty());

    // When & Then
    AccountNotFoundException exception = assertThrows(
        AccountNotFoundException.class,
        () -> transactionService.transferMoney(sourceAccountId, targetAccountId, amount));

    assertEquals("Source account with id non-existent-account not found", exception.getMessage());
    verify(loadAccountPort).loadAccount(sourceAccountId);
    verify(loadAccountPort, never()).loadAccount(targetAccountId);
    verify(saveAccountPort, never()).saveAccount(any(Account.class));
    verify(saveTransactionPort, never()).saveTransaction(any(Transaction.class));
  }

  @Test
  void transferMoney_shouldThrowExceptionWhenTargetAccountNotFound() {
    // Given
    String sourceAccountId = "source-account";
    String targetAccountId = "non-existent-account";
    Double amount = 100.0;

    Account sourceAccount = Account.createNew(sourceAccountId, "user-123", "USD");
    sourceAccount.deposit(new Money(200.0, "USD")); // Initial balance

    when(loadAccountPort.loadAccount(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
    when(loadAccountPort.loadAccount(targetAccountId)).thenReturn(Optional.empty());

    // When & Then
    AccountNotFoundException exception = assertThrows(
        AccountNotFoundException.class,
        () -> transactionService.transferMoney(sourceAccountId, targetAccountId, amount));

    assertEquals("Target account with id non-existent-account not found", exception.getMessage());
    verify(loadAccountPort).loadAccount(sourceAccountId);
    verify(loadAccountPort).loadAccount(targetAccountId);
    verify(saveAccountPort, never()).saveAccount(any(Account.class));
    verify(saveTransactionPort, never()).saveTransaction(any(Transaction.class));
  }

  @Test
  void transferMoney_shouldThrowExceptionWhenInsufficientBalance() {
    // Given
    String sourceAccountId = "source-account";
    String targetAccountId = "target-account";
    Double amount = 300.0; // More than available balance

    Account sourceAccount = Account.createNew(sourceAccountId, "user-123", "USD");
    sourceAccount.deposit(new Money(200.0, "USD")); // Initial balance

    Account targetAccount = Account.createNew(targetAccountId, "user-456", "USD");
    targetAccount.deposit(new Money(50.0, "USD")); // Initial balance

    when(loadAccountPort.loadAccount(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
    when(loadAccountPort.loadAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));

    // When & Then
    InsufficientBalanceException exception = assertThrows(
        InsufficientBalanceException.class,
        () -> transactionService.transferMoney(sourceAccountId, targetAccountId, amount));

    assertEquals("Insufficient balance in account source-account", exception.getMessage());
    verify(loadAccountPort).loadAccount(sourceAccountId);
    verify(loadAccountPort).loadAccount(targetAccountId);
    verify(saveAccountPort, never()).saveAccount(any(Account.class));
    verify(saveTransactionPort, never()).saveTransaction(any(Transaction.class));
  }

  @Test
  void transferMoney_shouldThrowExceptionWhenDifferentCurrencies() {
    // Given
    String sourceAccountId = "source-account";
    String targetAccountId = "target-account";
    Double amount = 100.0;

    Account sourceAccount = Account.createNew(sourceAccountId, "user-123", "USD");
    sourceAccount.deposit(new Money(200.0, "USD")); // Initial balance

    Account targetAccount = Account.createNew(targetAccountId, "user-456", "EUR");
    targetAccount.deposit(new Money(50.0, "EUR")); // Initial balance with different currency

    when(loadAccountPort.loadAccount(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
    when(loadAccountPort.loadAccount(targetAccountId)).thenReturn(Optional.of(targetAccount));

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> transactionService.transferMoney(sourceAccountId, targetAccountId, amount));

    assertEquals("Cannot transfer between accounts with different currencies", exception.getMessage());
    verify(loadAccountPort).loadAccount(sourceAccountId);
    verify(loadAccountPort).loadAccount(targetAccountId);
    verify(saveAccountPort, never()).saveAccount(any(Account.class));
    verify(saveTransactionPort, never()).saveTransaction(any(Transaction.class));
  }
}