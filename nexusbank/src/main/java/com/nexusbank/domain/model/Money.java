package com.nexusbank.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
  private final BigDecimal amount;
  private final String currency;

  public Money(Double amount) {
    this(amount, "USD"); // Moneda predeterminada
  }

  public Money(Double amount, String currency) {
    this.amount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN);
    this.currency = currency;
  }

  public Money(BigDecimal amount) {
    this(amount, "USD"); // Moneda predeterminada
  }

  public Money(BigDecimal amount, String currency) {
    this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    this.currency = currency;
  }

  public static Money zero(String currency) {
    return new Money(0.0, currency);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public Money add(Money money) {
    if (!this.currency.equals(money.currency)) {
      throw new IllegalArgumentException("Cannot add money with different currencies");
    }
    return new Money(this.amount.add(money.amount), this.currency);
  }

  public Money subtract(Money money) {
    if (!this.currency.equals(money.currency)) {
      throw new IllegalArgumentException("Cannot subtract money with different currencies");
    }
    return new Money(this.amount.subtract(money.amount), this.currency);
  }

  public boolean isLessThan(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot compare money with different currencies");
    }
    return this.amount.compareTo(other.amount) < 0;
  }

  public double toDouble() {
    return amount.doubleValue();
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