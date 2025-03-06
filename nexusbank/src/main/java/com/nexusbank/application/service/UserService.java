package com.nexusbank.application.service;

import com.nexusbank.application.port.in.CreateUserUseCase;
import com.nexusbank.application.port.out.LoadUserPort;
import com.nexusbank.application.port.out.SaveUserPort;
import com.nexusbank.domain.model.User;

import java.util.UUID;

public class UserService implements CreateUserUseCase {

  private final SaveUserPort saveUserPort;
  private final LoadUserPort loadUserPort;

  public UserService(SaveUserPort saveUserPort, LoadUserPort loadUserPort) {
    this.saveUserPort = saveUserPort;
    this.loadUserPort = loadUserPort;
  }

  @Override
  public String createUser(CreateUserCommand command) {
    // Check if email already exists
    loadUserPort.findUserByEmail(command.email())
        .ifPresent(user -> {
          throw new IllegalArgumentException("User with email " + command.email() + " already exists");
        });

    // Generate unique ID
    String userId = UUID.randomUUID().toString();

    // Hash password (for development purposes)
    String passwordHash = hashPassword(command.password());

    // Create and save user
    User user = User.createNew(userId, command.name(), command.email(), passwordHash);
    saveUserPort.saveUser(user);

    return userId;
  }

  private String hashPassword(String password) {
    // In a real application, use a proper password hashing algorithm like BCrypt
    // For simplicity, just prefix with "hashed_" for now
    return "hashed_" + password;
  }
}