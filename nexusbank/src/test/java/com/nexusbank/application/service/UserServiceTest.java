package com.nexusbank.application.service;

import com.nexusbank.application.port.in.CreateUserUseCase.CreateUserCommand;
import com.nexusbank.application.port.out.LoadUserPort;
import com.nexusbank.application.port.out.SaveUserPort;
import com.nexusbank.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private SaveUserPort saveUserPort;

  @Mock
  private LoadUserPort loadUserPort;

  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(saveUserPort, loadUserPort);
  }

  @Test
  void createUser_shouldCreateAndSaveUser() {
    // Given
    CreateUserCommand command = new CreateUserCommand(
        "John Doe",
        "john.doe@example.com",
        "password123");

    when(loadUserPort.findUserByEmail(anyString())).thenReturn(Optional.empty());

    // When
    String userId = userService.createUser(command);

    // Then
    assertNotNull(userId);

    // Verify loadUserPort was called to check if email exists
    verify(loadUserPort).findUserByEmail("john.doe@example.com");

    // Capture the User object passed to saveUserPort
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(saveUserPort).saveUser(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("john.doe@example.com", savedUser.getEmail());
    assertEquals("John Doe", savedUser.getName());
    // Check that password was hashed
    assertTrue(savedUser.getPasswordHash().startsWith("hashed_"));
  }

  @Test
  void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
    // Given
    CreateUserCommand command = new CreateUserCommand(
        "John Doe",
        "existing@example.com",
        "password123");

    // Mock that the email already exists
    User existingUser = User.createNew("existing-id", "Existing User", "existing@example.com", "hashed_pwd");
    when(loadUserPort.findUserByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> userService.createUser(command));

    assertEquals("User with email existing@example.com already exists", exception.getMessage());
    verify(loadUserPort).findUserByEmail("existing@example.com");
    verify(saveUserPort, never()).saveUser(any(User.class));
  }
}