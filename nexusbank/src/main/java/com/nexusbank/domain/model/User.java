package com.nexusbank.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * User entity representing a customer in the system.
 * This is an aggregate root in the DDD context.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For JPA
public class User {
  private String id;
  private String name;
  private String email;
  private String passwordHash;
  private boolean active;
  private Set<String> accounts;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

  private User(String id, String name, String email, String passwordHash) {
    validateId(id);
    validateName(name);
    validateEmail(email);
    validatePasswordHash(passwordHash);

    this.id = id;
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
    this.active = true;
    this.accounts = new HashSet<>();
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  /**
   * Factory method to create a new user.
   */
  public static User createNew(String id, String name, String email, String passwordHash) {
    return new User(id, name, email, passwordHash);
  }

  /**
   * Adds an account to the user.
   */
  public void addAccount(String accountId) {
    if (accountId == null || accountId.trim().isEmpty()) {
      throw new IllegalArgumentException("Account id cannot be null or empty");
    }

    if (accounts.contains(accountId)) {
      throw new IllegalArgumentException("Account is already associated with this user");
    }

    accounts.add(accountId);
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Removes an account from the user.
   */
  public void removeAccount(String accountId) {
    if (!accounts.contains(accountId)) {
      throw new IllegalArgumentException("Account is not associated with this user");
    }

    accounts.remove(accountId);
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Deactivates the user.
   */
  public void deactivate() {
    this.active = false;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Activates the user.
   */
  public void activate() {
    this.active = true;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates the user's name.
   */
  public void updateName(String name) {
    validateName(name);
    this.name = name;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates the user's email.
   */
  public void updateEmail(String email) {
    validateEmail(email);
    this.email = email;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates the user's password hash.
   */
  public void updatePasswordHash(String passwordHash) {
    validatePasswordHash(passwordHash);
    this.passwordHash = passwordHash;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Gets an unmodifiable view of the accounts.
   */
  public Set<String> getAccounts() {
    return Collections.unmodifiableSet(accounts);
  }

  private void validateId(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("User id cannot be null or empty");
    }
  }

  private void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("User name cannot be null or empty");
    }
  }

  private void validateEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("User email cannot be null or empty");
    }

    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("Invalid email format");
    }
  }

  private void validatePasswordHash(String passwordHash) {
    if (passwordHash == null || passwordHash.trim().isEmpty()) {
      throw new IllegalArgumentException("Password hash cannot be null or empty");
    }
  }
}