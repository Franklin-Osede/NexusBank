package com.nexusbank.domain.exception;

public class AccountNotFoundException extends RuntimeException {

  // Constructor para pasar directamente un ID de cuenta
  public AccountNotFoundException(String id) {
    super("Account with id " + id + " not found");
  }

  // Constructor para pasar un mensaje personalizado directamente
  public AccountNotFoundException(String message, boolean isCustomMessage) {
    super(isCustomMessage ? message : "Account with id " + message + " not found");
  }

  // Constructor con causa
  public AccountNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}