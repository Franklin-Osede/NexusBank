package com.nexusbank.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

  @Test
  void shouldCreateUserWithBasicInfo() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "John Doe";
    String email = "john.doe@example.com";
    String passwordHash = "hashedPassword123";

    // When
    User user = User.createNew(id, name, email, passwordHash);

    // Then
    assertEquals(id, user.getId());
    assertEquals(name, user.getName());
    assertEquals(email, user.getEmail());
    assertEquals(passwordHash, user.getPasswordHash());
    assertTrue(user.isActive());
    assertNotNull(user.getCreatedAt());
    assertEquals(0, user.getAccounts().size());
  }

  @Test
  void shouldNotCreateUserWithNullId() {
    // Given
    String id = null;
    String name = "John Doe";
    String email = "john.doe@example.com";
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithEmptyId() {
    // Given
    String id = "";
    String name = "John Doe";
    String email = "john.doe@example.com";
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithNullName() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = null;
    String email = "john.doe@example.com";
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithEmptyName() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "";
    String email = "john.doe@example.com";
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithNullEmail() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "John Doe";
    String email = null;
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithEmptyEmail() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "John Doe";
    String email = "";
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithInvalidEmail() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "John Doe";
    String email = "invalid-email";
    String passwordHash = "hashedPassword123";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithNullPasswordHash() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "John Doe";
    String email = "john.doe@example.com";
    String passwordHash = null;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldNotCreateUserWithEmptyPasswordHash() {
    // Given
    String id = UUID.randomUUID().toString();
    String name = "John Doe";
    String email = "john.doe@example.com";
    String passwordHash = "";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> User.createNew(id, name, email, passwordHash));
  }

  @Test
  void shouldAddAccountToUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    String accountId = UUID.randomUUID().toString();

    // When
    user.addAccount(accountId);

    // Then
    assertEquals(1, user.getAccounts().size());
    assertTrue(user.getAccounts().contains(accountId));
  }

  @Test
  void shouldNotAddNullAccountToUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.addAccount(null));
  }

  @Test
  void shouldNotAddEmptyAccountToUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.addAccount(""));
  }

  @Test
  void shouldNotAddDuplicateAccountToUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    String accountId = UUID.randomUUID().toString();
    user.addAccount(accountId);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.addAccount(accountId));
  }

  @Test
  void shouldRemoveAccountFromUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    String accountId = UUID.randomUUID().toString();
    user.addAccount(accountId);

    // When
    user.removeAccount(accountId);

    // Then
    assertEquals(0, user.getAccounts().size());
  }

  @Test
  void shouldNotRemoveNonExistentAccount() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.removeAccount(UUID.randomUUID().toString()));
  }

  @Test
  void shouldDeactivateUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    assertTrue(user.isActive());

    // When
    user.deactivate();

    // Then
    assertFalse(user.isActive());
  }

  @Test
  void shouldActivateUser() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    user.deactivate();
    assertFalse(user.isActive());

    // When
    user.activate();

    // Then
    assertTrue(user.isActive());
  }

  @Test
  void shouldUpdateUserName() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    String newName = "Jane Doe";

    // When
    user.updateName(newName);

    // Then
    assertEquals(newName, user.getName());
  }

  @Test
  void shouldNotUpdateUserNameToNull() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updateName(null));
  }

  @Test
  void shouldNotUpdateUserNameToEmpty() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updateName(""));
  }

  @Test
  void shouldUpdateUserEmail() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    String newEmail = "jane.doe@example.com";

    // When
    user.updateEmail(newEmail);

    // Then
    assertEquals(newEmail, user.getEmail());
  }

  @Test
  void shouldNotUpdateUserEmailToNull() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updateEmail(null));
  }

  @Test
  void shouldNotUpdateUserEmailToEmpty() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updateEmail(""));
  }

  @Test
  void shouldNotUpdateUserEmailToInvalid() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updateEmail("invalid-email"));
  }

  @Test
  void shouldUpdateUserPassword() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");
    String newPasswordHash = "newHashedPassword456";

    // When
    user.updatePasswordHash(newPasswordHash);

    // Then
    assertEquals(newPasswordHash, user.getPasswordHash());
  }

  @Test
  void shouldNotUpdateUserPasswordToNull() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updatePasswordHash(null));
  }

  @Test
  void shouldNotUpdateUserPasswordToEmpty() {
    // Given
    User user = User.createNew(
        UUID.randomUUID().toString(),
        "John Doe",
        "john.doe@example.com",
        "hashedPassword123");

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> user.updatePasswordHash(""));
  }
}