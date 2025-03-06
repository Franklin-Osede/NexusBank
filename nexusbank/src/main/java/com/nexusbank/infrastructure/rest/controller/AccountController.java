package com.nexusbank.infrastructure.rest.controller;

import com.nexusbank.application.port.in.CreateAccountUseCase;
import com.nexusbank.application.port.in.GetAccountUseCase;
import com.nexusbank.domain.model.Account;
import com.nexusbank.infrastructure.rest.dto.request.CreateAccountRequest;
import com.nexusbank.infrastructure.rest.dto.response.AccountResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "accounts", description = "API para gestión de cuentas bancarias")
public class AccountController {

  private final CreateAccountUseCase createAccountUseCase;
  private final GetAccountUseCase getAccountUseCase;

  public AccountController(CreateAccountUseCase createAccountUseCase, GetAccountUseCase getAccountUseCase) {
    this.createAccountUseCase = createAccountUseCase;
    this.getAccountUseCase = getAccountUseCase;
  }

  @Operation(summary = "Crear una nueva cuenta bancaria", description = "Crea una nueva cuenta bancaria para un usuario existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cuenta creada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de cuenta inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
  })
  @PostMapping
  public ResponseEntity<AccountResponse> createAccount(
      @Parameter(description = "Datos para crear la cuenta", required = true) @Valid @RequestBody CreateAccountRequest request) {

    Account createdAccount = createAccountUseCase.createAccount(
        request.userId(),
        request.initialDeposit());

    return new ResponseEntity<>(
        AccountResponse.fromDomain(createdAccount),
        HttpStatus.CREATED);
  }

  @Operation(summary = "Obtener una cuenta por ID", description = "Recupera los detalles de una cuenta bancaria específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cuenta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
  })
  @GetMapping("/{accountId}")
  public ResponseEntity<AccountResponse> getAccount(
      @Parameter(description = "ID de la cuenta a consultar", required = true) @PathVariable String accountId) {

    Account account = getAccountUseCase.getAccountById(accountId);
    return ResponseEntity.ok(AccountResponse.fromDomain(account));
  }

  @Operation(summary = "Listar cuentas por usuario", description = "Obtiene todas las cuentas bancarias asociadas a un usuario")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cuentas recuperada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponse.class))),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
  })
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<AccountResponse>> getAccountsByUserId(
      @Parameter(description = "ID del usuario", required = true) @PathVariable String userId) {

    List<Account> accounts = getAccountUseCase.getAccountsByUserId(userId);

    List<AccountResponse> accountResponses = accounts.stream()
        .map(AccountResponse::fromDomain)
        .collect(Collectors.toList());

    return ResponseEntity.ok(accountResponses);
  }
}