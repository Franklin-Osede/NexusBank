package com.nexusbank.application.infrastructure.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusbank.application.port.in.CreateUserUseCase;
import com.nexusbank.application.port.in.CreateUserUseCase.CreateUserCommand;
import com.nexusbank.infrastructure.rest.controller.UserController;
import com.nexusbank.infrastructure.rest.dto.request.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private CreateUserUseCase createUserUseCase;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    UserController userController = new UserController(createUserUseCase);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void createUser_shouldReturnCreatedStatus() throws Exception {
    // Given
    CreateUserRequest request = new CreateUserRequest("John Doe", "john.doe@example.com", "password123");
    when(createUserUseCase.createUser(any(CreateUserCommand.class))).thenReturn("user-123");

    // When & Then
    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value("user-123"))
        .andExpect(jsonPath("$.message").value("User created successfully"));

    // Verificar que el comando se pasa correctamente
    ArgumentCaptor<CreateUserCommand> commandCaptor = ArgumentCaptor.forClass(CreateUserCommand.class);
    verify(createUserUseCase).createUser(commandCaptor.capture());

    CreateUserCommand capturedCommand = commandCaptor.getValue();
    assertEquals("John Doe", capturedCommand.name());
    assertEquals("john.doe@example.com", capturedCommand.email());
    assertEquals("password123", capturedCommand.password());
  }

  @Test
  void createUser_shouldReturnBadRequestWhenInvalidData() throws Exception {
    // Given
    CreateUserRequest request = new CreateUserRequest("", "invalid-email", "pwd");

    // When & Then
    mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
