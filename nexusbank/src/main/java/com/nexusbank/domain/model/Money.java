package com.nexusbank.domain.model;

import lombok.Getter;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Money value object representing an amount with a currency.
 * This is immutable to ensure thread safety and integrity of financial
 * calculations.
 */
@Getter
public final class Money {

  private final BigDecimal amount;
  private final String currency;

  public Money(BigDecimal amount, String currency) {
    validateAmount(amount);
    validateCurrency(currency);
    this.amount = amount;
    this.currency = currency;
  }

  public static Money zero(String currency) {
    return new Money(BigDecimal.ZERO, currency);
  }

  public Money add(Money money) {
    validateSameCurrency(money);
    return new Money(this.amount.add(money.amount), this.currency);
  }

  public Money subtract(Money money) {
    validateSameCurrency(money);
    BigDecimal newAmount = this.amount.subtract(money.amount);
    if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Cannot have negative money amount after subtraction");
    }
    return new Money(newAmount, this.currency);
  }

  public boolean isGreaterThan(Money money) {
    validateSameCurrency(money);
    return this.amount.compareTo(money.amount) > 0;
  }

  public boolean isLessThan(Money money) {
    validateSameCurrency(money);
    return this.amount.compareTo(money.amount) < 0;
  }

  public boolean isGreaterThanOrEqual(Money money) {
    validateSameCurrency(money);
    return this.amount.compareTo(money.amount) >= 0;
  }

  public boolean isLessThanOrEqual(Money money) {
    validateSameCurrency(money);
    return this.amount.compareTo(money.amount) <= 0;
  }

  private void validateAmount(BigDecimal amount) {
    if (amount == null) {
      throw new IllegalArgumentException("Amount cannot be null");
    }
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
  }

  private void validateCurrency(String currency) {
    if (currency == null || currency.trim().isEmpty()) {
      throw new IllegalArgumentException("Currency cannot be null or empty");
    }
  }

  private void validateSameCurrency(Money money) {
    if (!this.currency.equals(money.currency)) {
      throw new IllegalArgumentException("Cannot operate on money with different currencies");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Money money = (Money) o;
    return amount.compareTo(money.amount) == 0 &&
        Objects.equals(currency, money.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public String toString() {
    return amount + " " + currency;
  }
}