package com.nexusbank.application.port.in;

public interface CreateUserUseCase {
  /**
   * Creates a new user in the system.
   * 
   * @param command Details of the user to create
   * @return ID of the newly created user
   */
  String createUser(CreateUserCommand command);

  /**
   * Command for creating a user.
   */
  record CreateUserCommand(String name, String email, String password) {
    public CreateUserCommand {
      if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("Name cannot be null or empty");
      }
      if (email == null || email.isBlank()) {
        throw new IllegalArgumentException("Email cannot be null or empty");
      }
      if (password == null || password.isBlank()) {
        throw new IllegalArgumentException("Password cannot be null or empty");
      }
    }
  }
}