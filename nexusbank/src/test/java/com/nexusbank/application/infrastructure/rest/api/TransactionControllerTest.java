package com.nexusbank.application.infrastructure.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nexusbank.application.port.in.DepositMoneyUseCase;
import com.nexusbank.application.port.in.TransferMoneyUseCase;
import com.nexusbank.domain.exception.AccountNotFoundException;
import com.nexusbank.domain.exception.InsufficientBalanceException;
import com.nexusbank.domain.model.Money;
import com.nexusbank.domain.model.Transaction;
import com.nexusbank.infrastructure.rest.controller.TransactionController;
import com.nexusbank.infrastructure.rest.dto.request.DepositRequest;
import com.nexusbank.infrastructure.rest.dto.request.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

  private MockMvc mockMvc;

  @Mock
  private DepositMoneyUseCase depositMoneyUseCase;

  @Mock
  private TransferMoneyUseCase transferMoneyUseCase;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    TransactionController transactionController = new TransactionController(depositMoneyUseCase, transferMoneyUseCase);
    mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()); // Para serializar LocalDateTime
  }

  @Test
  void deposit_shouldReturnOkStatus() throws Exception {
    // Given
    String accountId = "acc-123";
    DepositRequest request = new DepositRequest(100.0);
    Transaction mockTransaction = Transaction.createDeposit("tx-123", accountId, new Money(100.0, "USD"));

    when(depositMoneyUseCase.depositMoney(anyString(), anyDouble())).thenReturn(mockTransaction);

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("tx-123"))
        .andExpect(jsonPath("$.accountId").value(accountId))
        .andExpect(jsonPath("$.amount.amount").value(100.0))
        .andExpect(jsonPath("$.type").value("DEPOSIT"));

    verify(depositMoneyUseCase).depositMoney(accountId, 100.0);
  }

  @Test
  void deposit_shouldReturnBadRequestWhenInvalidAmount() throws Exception {
    // Given
    String accountId = "acc-123";
    DepositRequest request = new DepositRequest(-50.0); // Negative amount

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deposit_shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
    // Given
    String accountId = "non-existent-account";
    DepositRequest request = new DepositRequest(100.0);

    when(depositMoneyUseCase.depositMoney(anyString(), anyDouble()))
        .thenThrow(new AccountNotFoundException(accountId));

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void transfer_shouldReturnOkStatus() throws Exception {
    // Given
    String sourceAccountId = "source-acc";
    String targetAccountId = "target-acc";
    TransferRequest request = new TransferRequest(targetAccountId, 150.0);
    Transaction mockTransaction = Transaction.createTransfer("tx-456", sourceAccountId, targetAccountId,
        new Money(150.0, "USD"));

    when(transferMoneyUseCase.transferMoney(anyString(), anyString(), anyDouble())).thenReturn(mockTransaction);

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("tx-456"))
        .andExpect(jsonPath("$.accountId").value(sourceAccountId))
        .andExpect(jsonPath("$.targetAccountId").value(targetAccountId))
        .andExpect(jsonPath("$.amount.amount").value(150.0))
        .andExpect(jsonPath("$.type").value("TRANSFER"));

    verify(transferMoneyUseCase).transferMoney(sourceAccountId, targetAccountId, 150.0);
  }

  @Test
  void transfer_shouldReturnBadRequestWhenInvalidAmount() throws Exception {
    // Given
    String sourceAccountId = "source-acc";
    TransferRequest request = new TransferRequest("target-acc", 0.0); // Zero amount

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void transfer_shouldReturnNotFoundWhenSourceAccountDoesNotExist() throws Exception {
    // Given
    String sourceAccountId = "non-existent-account";
    String targetAccountId = "target-acc";
    TransferRequest request = new TransferRequest(targetAccountId, 100.0);

    when(transferMoneyUseCase.transferMoney(anyString(), anyString(), anyDouble()))
        .thenThrow(new AccountNotFoundException("Source account with id " + sourceAccountId + " not found"));

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void transfer_shouldReturnNotFoundWhenTargetAccountDoesNotExist() throws Exception {
    // Given
    String sourceAccountId = "source-acc";
    String targetAccountId = "non-existent-account";
    TransferRequest request = new TransferRequest(targetAccountId, 100.0);

    when(transferMoneyUseCase.transferMoney(anyString(), anyString(), anyDouble()))
        .thenThrow(new AccountNotFoundException("Target account with id " + targetAccountId + " not found"));

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void transfer_shouldReturnBadRequestWhenInsufficientBalance() throws Exception {
    // Given
    String sourceAccountId = "source-acc";
    String targetAccountId = "target-acc";
    TransferRequest request = new TransferRequest(targetAccountId, 1000.0); // Too much

    when(transferMoneyUseCase.transferMoney(anyString(), anyString(), anyDouble()))
        .thenThrow(new InsufficientBalanceException("Insufficient balance in account " + sourceAccountId));

    // When & Then
    mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
