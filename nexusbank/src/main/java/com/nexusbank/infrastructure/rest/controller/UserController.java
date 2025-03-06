package com.nexusbank.infrastructure.rest.controller;

import com.nexusbank.application.port.in.CreateUserUseCase;
import com.nexusbank.application.port.in.CreateUserUseCase.CreateUserCommand;
import com.nexusbank.infrastructure.rest.dto.request.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "users", description = "API para gestión de usuarios")
public class UserController {

  private final CreateUserUseCase createUserUseCase;

  public UserController(CreateUserUseCase createUserUseCase) {
    this.createUserUseCase = createUserUseCase;
  }

  @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema con la información proporcionada")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario creado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
      @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos o email ya existente", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
  })
  @PostMapping
  public ResponseEntity<Map<String, String>> createUser(
      @Parameter(description = "Datos del usuario a crear", required = true) @Valid @RequestBody CreateUserRequest request) {

    String userId = createUserUseCase.createUser(
        new CreateUserCommand(
            request.name(),
            request.email(),
            request.password()));

    return new ResponseEntity<>(
        Map.of("userId", userId, "message", "User created successfully"),
        HttpStatus.CREATED);
  }

  // Aquí se pueden añadir más endpoints para gestión de usuarios
}