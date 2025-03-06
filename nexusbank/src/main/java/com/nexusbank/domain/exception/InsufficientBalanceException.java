package com.nexusbank.domain.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {

  public InsufficientBalanceException(String accountId, BigDecimal currentBalance, BigDecimal requiredAmount) {
    super("Insufficient balance in account " + accountId + ": current balance is " +
        currentBalance + ", but " + requiredAmount + " is required");
  }

  public InsufficientBalanceException(String message) {
    super(message);
  }
}