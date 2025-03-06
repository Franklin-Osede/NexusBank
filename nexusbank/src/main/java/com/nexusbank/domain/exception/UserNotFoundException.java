package com.nexusbank.domain.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String id) {
    super("User with id " + id + " not found");
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}