package com.nexusbank.infrastructure.rest.controller;

import com.nexusbank.application.port.in.DepositMoneyUseCase;
import com.nexusbank.application.port.in.TransferMoneyUseCase;
import com.nexusbank.domain.model.Transaction;
import com.nexusbank.infrastructure.rest.dto.request.DepositRequest;
import com.nexusbank.infrastructure.rest.dto.request.TransferRequest;
import com.nexusbank.infrastructure.rest.dto.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "transactions", description = "API para gestión de transacciones bancarias")
public class TransactionController {

  private final DepositMoneyUseCase depositMoneyUseCase;
  private final TransferMoneyUseCase transferMoneyUseCase;

  public TransactionController(DepositMoneyUseCase depositMoneyUseCase, TransferMoneyUseCase transferMoneyUseCase) {
    this.depositMoneyUseCase = depositMoneyUseCase;
    this.transferMoneyUseCase = transferMoneyUseCase;
  }

  @Operation(summary = "Realizar un depósito", description = "Deposita dinero en una cuenta bancaria específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Depósito realizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de depósito inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
  })
  @PostMapping("/accounts/{accountId}/deposit")
  public ResponseEntity<TransactionResponse> deposit(
      @Parameter(description = "ID de la cuenta donde se realizará el depósito", required = true) @PathVariable String accountId,

      @Parameter(description = "Detalles del depósito", required = true) @Valid @RequestBody DepositRequest request) {

    Transaction transaction = depositMoneyUseCase.depositMoney(accountId, request.amount());
    return ResponseEntity.ok(TransactionResponse.fromDomain(transaction));
  }

  @Operation(summary = "Realizar una transferencia", description = "Transfiere dinero desde una cuenta a otra")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Transferencia realizada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de transferencia inválidos o saldo insuficiente", content = @Content),
      @ApiResponse(responseCode = "404", description = "Cuenta origen o destino no encontrada", content = @Content),
      @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
  })
  @PostMapping("/accounts/{sourceAccountId}/transfer")
  public ResponseEntity<TransactionResponse> transfer(
      @Parameter(description = "ID de la cuenta de origen", required = true) @PathVariable String sourceAccountId,

      @Parameter(description = "Detalles de la transferencia", required = true) @Valid @RequestBody TransferRequest request) {

    Transaction transaction = transferMoneyUseCase.transferMoney(
        sourceAccountId,
        request.targetAccountId(),
        request.amount());

    return ResponseEntity.ok(TransactionResponse.fromDomain(transaction));
  }
}