package com.nexusbank.application.service;

import com.nexusbank.application.port.in.CreateUserUseCase;
import com.nexusbank.application.port.out.LoadUserPort;
import com.nexusbank.application.port.out.SaveUserPort;
import com.nexusbank.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

  private SaveUserPort saveUserPort;
  private LoadUserPort loadUserPort;
  private UserService userService;

  @BeforeEach
  void setUp() {
    saveUserPort = Mockito.mock(SaveUserPort.class);
    loadUserPort = Mockito.mock(LoadUserPort.class);
    userService = new UserService(saveUserPort, loadUserPort);
  }

  @Test
  void shouldCreateUser() {
    // Given
    CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand("John Doe",
        "john@example.com", "password123");

    when(loadUserPort.findUserByEmail("john@example.com")).thenReturn(Optional.empty());

    // When
    String userId = userService.createUser(command);

    // Then
    assertNotNull(userId);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(saveUserPort).saveUser(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("John Doe", savedUser.getName());
    assertEquals("john@example.com", savedUser.getEmail());
    // No verificamos el password hash directamente porque debería estar encriptado
    assertTrue(savedUser.isActive());
  }

  @Test
  void shouldNotCreateUserWithExistingEmail() {
    // Given
    CreateUserUseCase.CreateUserCommand command = new CreateUserUseCase.CreateUserCommand("John Doe",
        "john@example.com", "password123");

    User existingUser = User.createNew("existing-id", "Existing User", "john@example.com", "hashedPassword");
    when(loadUserPort.findUserByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> userService.createUser(command));
    verify(saveUserPort, never()).saveUser(any());
  }
}