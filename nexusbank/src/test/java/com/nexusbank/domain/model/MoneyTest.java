package com.nexusbank.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

  @Test
  void shouldCreateMoney() {
    // Given
    BigDecimal amount = new BigDecimal("100.00");
    String currency = "USD";

    // When
    Money money = new Money(amount, currency);

    // Then
    assertEquals(amount, money.getAmount());
    assertEquals(currency, money.getCurrency());
  }

  @Test
  void shouldCreateZeroMoney() {
    // When
    Money money = Money.zero("EUR");

    // Then
    assertEquals(BigDecimal.ZERO, money.getAmount());
    assertEquals("EUR", money.getCurrency());
  }

  @Test
  void shouldNotCreateMoneyWithNullAmount() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> new Money(null, "USD"));
  }

  @Test
  void shouldNotCreateMoneyWithNullCurrency() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("100.00"), null));
  }

  @Test
  void shouldNotCreateMoneyWithEmptyCurrency() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("100.00"), ""));
  }

  @Test
  void shouldNotCreateMoneyWithNegativeAmount() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-10.00"), "USD"));
  }

  @Test
  void shouldAddMoney() {
    // Given
    Money money1 = new Money(new BigDecimal("100.00"), "USD");
    Money money2 = new Money(new BigDecimal("50.00"), "USD");

    // When
    Money result = money1.add(money2);

    // Then
    assertEquals(new BigDecimal("150.00"), result.getAmount());
    assertEquals("USD", result.getCurrency());
  }

  @Test
  void shouldNotAddMoneyWithDifferentCurrencies() {
    // Given
    Money money1 = new Money(new BigDecimal("100.00"), "USD");
    Money money2 = new Money(new BigDecimal("50.00"), "EUR");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> money1.add(money2));
  }

  @Test
  void shouldSubtractMoney() {
    // Given
    Money money1 = new Money(new BigDecimal("100.00"), "USD");
    Money money2 = new Money(new BigDecimal("30.00"), "USD");

    // When
    Money result = money1.subtract(money2);

    // Then
    assertEquals(new BigDecimal("70.00"), result.getAmount());
    assertEquals("USD", result.getCurrency());
  }

  @Test
  void shouldNotSubtractMoneyWithDifferentCurrencies() {
    // Given
    Money money1 = new Money(new BigDecimal("100.00"), "USD");
    Money money2 = new Money(new BigDecimal("30.00"), "EUR");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> money1.subtract(money2));
  }

  @Test
  void shouldNotAllowNegativeResultAfterSubtraction() {
    // Given
    Money money1 = new Money(new BigDecimal("30.00"), "USD");
    Money money2 = new Money(new BigDecimal("50.00"), "USD");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> money1.subtract(money2));
  }

  @Test
  void shouldCompareMoneyValues() {
    // Given
    Money money1 = new Money(new BigDecimal("100.00"), "USD");
    Money money2 = new Money(new BigDecimal("100.00"), "USD");
    Money money3 = new Money(new BigDecimal("50.00"), "USD");

    // When & Then
    assertTrue(money1.isGreaterThan(money3));
    assertTrue(money1.isGreaterThanOrEqual(money2));
    assertTrue(money3.isLessThan(money1));
    assertTrue(money3.isLessThanOrEqual(money1));
    assertFalse(money1.isLessThan(money3));
  }

  @Test
  void shouldImplementEqualsAndHashCodeCorrectly() {
    // Given
    Money money1 = new Money(new BigDecimal("100.00"), "USD");
    Money money2 = new Money(new BigDecimal("100.00"), "USD");
    Money money3 = new Money(new BigDecimal("100.00"), "EUR");
    Money money4 = new Money(new BigDecimal("50.00"), "USD");

    // Then
    assertEquals(money1, money2);
    assertEquals(money1.hashCode(), money2.hashCode());
    assertNotEquals(money1, money3);
    assertNotEquals(money1, money4);
  }

  @Test
  void shouldHaveCorrectStringRepresentation() {
    // Given
    Money money = new Money(new BigDecimal("100.00"), "USD");

    // When
    String result = money.toString();

    // Then
    assertEquals("100.00 USD", result);
  }
}