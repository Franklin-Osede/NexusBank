package com.nexusbank.domain.exception;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(String id) {
    super("Account with id " + id + " not found");
  }

  public AccountNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}