package com.nexusbank.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Account entity representing a user's wallet/account in the system.
 * This is an aggregate root in the DDD context.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For JPA
public class Account {
  private String id;
  private String userId;
  private Money balance;
  private boolean active;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Constructor is private to enforce factory method usage
  private Account(String id, String userId, Money initialBalance) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Account id cannot be null or empty");
    }
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException("User id cannot be null or empty");
    }
    if (initialBalance == null) {
      throw new IllegalArgumentException("Initial balance cannot be null");
    }

    this.id = id;
    this.userId = userId;
    this.balance = initialBalance;
    this.active = true;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  /**
   * Factory method to create a new account with zero balance
   * 
   * @param id       Account identifier
   * @param userId   Owner's identifier
   * @param currency Currency for the account
   * @return new Account instance
   */
  public static Account createNew(String id, String userId, String currency) {
    return new Account(id, userId, Money.zero(currency));
  }

  /**
   * Deposits money into the account
   * 
   * @param amount Money to deposit
   * @throws IllegalStateException    if account is inactive
   * @throws IllegalArgumentException if currencies don't match
   */
  public void deposit(Money amount) {
    validateAccountIsActive();
    validateSameCurrency(amount);
    this.balance = this.balance.add(amount);
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Withdraws money from the account
   * 
   * @param amount Money to withdraw
   * @throws IllegalStateException    if account is inactive
   * @throws IllegalArgumentException if insufficient balance or currency mismatch
   */
  public void withdraw(Money amount) {
    validateAccountIsActive();
    validateSameCurrency(amount);

    if (balance.isLessThan(amount)) {
      throw new IllegalArgumentException("Insufficient balance for withdrawal");
    }

    this.balance = this.balance.subtract(amount);
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Activates the account
   */
  public void activate() {
    this.active = true;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Deactivates the account
   */
  public void deactivate() {
    this.active = false;
    this.updatedAt = LocalDateTime.now();
  }

  private void validateAccountIsActive() {
    if (!this.active) {
      throw new IllegalStateException("Account is not active");
    }
  }

  private void validateSameCurrency(Money money) {
    if (!this.balance.getCurrency().equals(money.getCurrency())) {
      throw new IllegalArgumentException(
          "Currency mismatch: account is in " + this.balance.getCurrency() +
              " but operation attempted with " + money.getCurrency());
    }
  }
}