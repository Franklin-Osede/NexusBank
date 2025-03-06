package com.nexusbank.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

  @Test
  void shouldCreateAccountWithZeroBalance() {
    // Given
    String id = UUID.randomUUID().toString();
    String userId = UUID.randomUUID().toString();
    String currency = "USD";

    // When
    Account account = Account.createNew(id, userId, currency);

    // Then
    assertEquals(id, account.getId());
    assertEquals(userId, account.getUserId());
    // Usar el método equals para BigDecimal o convertir a double con tolerancia
    assertEquals(0, BigDecimal.ZERO.compareTo(account.getBalance().getAmount()));
    assertEquals(currency, account.getBalance().getCurrency());
    assertTrue(account.isActive());
    assertNotNull(account.getCreatedAt());
  }

  @Test
  void shouldDepositMoney() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    Money deposit = new Money(new BigDecimal("100.00"), "USD");

    // When
    account.deposit(deposit);

    // Then
    assertEquals(0, new BigDecimal("100.00").compareTo(account.getBalance().getAmount()));
  }

  @Test
  void shouldDepositMoneyMultipleTimes() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");

    // When
    account.deposit(new Money(new BigDecimal("100.00"), "USD"));
    account.deposit(new Money(new BigDecimal("50.50"), "USD"));

    // Then
    assertEquals(0, new BigDecimal("150.50").compareTo(account.getBalance().getAmount()));
  }

  @Test
  void shouldNotDepositMoneyWithDifferentCurrency() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    Money deposit = new Money(new BigDecimal("100.00"), "EUR");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> account.deposit(deposit));
  }

  @Test
  void shouldWithdrawMoney() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    account.deposit(new Money(new BigDecimal("100.00"), "USD"));
    Money withdrawal = new Money(new BigDecimal("60.00"), "USD");

    // When
    account.withdraw(withdrawal);

    // Then
    assertEquals(0, new BigDecimal("40.00").compareTo(account.getBalance().getAmount()));
  }

  @Test
  void shouldNotWithdrawMoneyWithInsufficientBalance() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    account.deposit(new Money(new BigDecimal("50.00"), "USD"));
    Money withdrawal = new Money(new BigDecimal("60.00"), "USD");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> account.withdraw(withdrawal));
  }

  @Test
  void shouldNotWithdrawMoneyWithDifferentCurrency() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    account.deposit(new Money(new BigDecimal("100.00"), "USD"));
    Money withdrawal = new Money(new BigDecimal("50.00"), "EUR");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> account.withdraw(withdrawal));
  }

  @Test
  void shouldNotDepositToInactiveAccount() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    account.deactivate();
    Money deposit = new Money(new BigDecimal("100.00"), "USD");

    // When & Then
    assertThrows(IllegalStateException.class, () -> account.deposit(deposit));
  }

  @Test
  void shouldNotWithdrawFromInactiveAccount() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    account.deposit(new Money(new BigDecimal("100.00"), "USD"));
    account.deactivate();
    Money withdrawal = new Money(new BigDecimal("50.00"), "USD");

    // When & Then
    assertThrows(IllegalStateException.class, () -> account.withdraw(withdrawal));
  }

  @Test
  void shouldActivateAccount() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    account.deactivate();
    assertFalse(account.isActive());

    // When
    account.activate();

    // Then
    assertTrue(account.isActive());
  }

  @Test
  void shouldDeactivateAccount() {
    // Given
    Account account = Account.createNew(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        "USD");
    assertTrue(account.isActive());

    // When
    account.deactivate();

    // Then
    assertFalse(account.isActive());
  }
}