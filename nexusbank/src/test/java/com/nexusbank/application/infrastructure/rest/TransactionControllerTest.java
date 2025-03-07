package com.nexusbank.application.infrastructure.rest;

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
import com.nexusbank.infrastructure.rest.exception.RestExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    // Initialize controller with mocked use cases
    TransactionController transactionController = new TransactionController(
        depositMoneyUseCase, transferMoneyUseCase);

    // Configure MockMvc with RestExceptionHandler
    mockMvc = MockMvcBuilders
        .standaloneSetup(transactionController)
        .setControllerAdvice(new RestExceptionHandler())
        .build();

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void deposit_shouldReturnOkStatus() throws Exception {
    // Input data
    String accountId = "acc-123";
    DepositRequest request = new DepositRequest(100.0);

    // Mock successful transaction
    String transactionId = UUID.randomUUID().toString();
    Transaction mockTransaction = Transaction.createDeposit(
        transactionId,
        accountId,
        new Money(100.0, "USD"));

    when(depositMoneyUseCase.depositMoney(eq(accountId), eq(100.0)))
        .thenReturn(mockTransaction);

    // Perform request and verify results
    mockMvc.perform(post("/api/transactions/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(transactionId))
        .andExpect(jsonPath("$.accountId").value(accountId))
        .andExpect(jsonPath("$.amount.amount").value(100.0))
        .andExpect(jsonPath("$.amount.currency").value("USD"))
        .andExpect(jsonPath("$.type").value("DEPOSIT"))
        .andExpect(jsonPath("$.status").value("COMPLETED"));

    verify(depositMoneyUseCase).depositMoney(eq(accountId), eq(100.0));
  }

  @Test
  void deposit_shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
    // Input data for non-existent account
    String accountId = "non-existent-account";
    DepositRequest request = new DepositRequest(100.0);

    doThrow(new AccountNotFoundException(accountId))
        .when(depositMoneyUseCase).depositMoney(eq(accountId), anyDouble());

    // Perform request and verify error response manually
    MvcResult result = mockMvc.perform(post("/api/transactions/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andReturn();

    // Check status code manually
    assertEquals(404, result.getResponse().getStatus());

    // Check response content contains expected error message
    String responseBody = result.getResponse().getContentAsString();
    assertTrue(responseBody.contains("Account Not Found"));
  }

  @Test
  void transfer_shouldReturnOkStatus() throws Exception {
    // Input data
    String sourceAccountId = "source-acc";
    String targetAccountId = "target-acc";
    TransferRequest request = new TransferRequest(targetAccountId, 100.0);

    // Mock successful transaction
    String transactionId = UUID.randomUUID().toString();
    Transaction mockTransaction = Transaction.createTransfer(
        transactionId,
        sourceAccountId,
        targetAccountId,
        new Money(100.0, "USD"));

    when(transferMoneyUseCase.transferMoney(eq(sourceAccountId), eq(targetAccountId), eq(100.0)))
        .thenReturn(mockTransaction);

    // Perform request and verify results
    mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(transactionId))
        .andExpect(jsonPath("$.accountId").value(sourceAccountId))
        .andExpect(jsonPath("$.targetAccountId").value(targetAccountId))
        .andExpect(jsonPath("$.amount.amount").value(100.0))
        .andExpect(jsonPath("$.amount.currency").value("USD"))
        .andExpect(jsonPath("$.type").value("TRANSFER"))
        .andExpect(jsonPath("$.status").value("COMPLETED"));

    verify(transferMoneyUseCase).transferMoney(eq(sourceAccountId), eq(targetAccountId), eq(100.0));
  }

  @Test
  void transfer_shouldReturnNotFoundWhenSourceAccountDoesNotExist() throws Exception {
    // Input data
    String sourceAccountId = "non-existent-account";
    String targetAccountId = "target-acc";
    TransferRequest request = new TransferRequest(targetAccountId, 100.0);

    doThrow(new AccountNotFoundException("Source account with id " + sourceAccountId + " not found"))
        .when(transferMoneyUseCase).transferMoney(eq(sourceAccountId), eq(targetAccountId), anyDouble());

    // Perform request and verify error response manually
    MvcResult result = mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andReturn();

    // Check status code manually
    assertEquals(404, result.getResponse().getStatus());

    // Check response content contains expected error message
    String responseBody = result.getResponse().getContentAsString();
    assertTrue(responseBody.contains("Account Not Found"));
  }

  @Test
  void transfer_shouldReturnNotFoundWhenTargetAccountDoesNotExist() throws Exception {
    // Input data
    String sourceAccountId = "source-acc";
    String targetAccountId = "non-existent-account";
    TransferRequest request = new TransferRequest(targetAccountId, 100.0);

    doThrow(new AccountNotFoundException("Target account with id " + targetAccountId + " not found"))
        .when(transferMoneyUseCase).transferMoney(eq(sourceAccountId), eq(targetAccountId), anyDouble());

    // Perform request and verify error response manually
    MvcResult result = mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andReturn();

    // Check status code manually
    assertEquals(404, result.getResponse().getStatus());

    // Check response content contains expected error message
    String responseBody = result.getResponse().getContentAsString();
    assertTrue(responseBody.contains("Account Not Found"));
  }

  @Test
  void transfer_shouldReturnBadRequestWhenInsufficientBalance() throws Exception {
    // Input data
    String sourceAccountId = "source-acc";
    String targetAccountId = "target-acc";
    TransferRequest request = new TransferRequest(targetAccountId, 1000.0);

    doThrow(new InsufficientBalanceException("Insufficient balance in account " + sourceAccountId))
        .when(transferMoneyUseCase).transferMoney(eq(sourceAccountId), eq(targetAccountId), anyDouble());

    // Perform request and verify error response manually
    MvcResult result = mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andReturn();

    // Check status code manually
    assertEquals(400, result.getResponse().getStatus());

    // Check response content contains expected error message
    String responseBody = result.getResponse().getContentAsString();
    assertTrue(responseBody.contains("Insufficient Balance"));
  }

  @Test
  void deposit_shouldReturnBadRequestWhenAmountIsInvalid() throws Exception {
    // Input data with invalid amount (negative)
    String accountId = "acc-123";
    DepositRequest request = new DepositRequest(-50.0);

    // Perform request and verify validation error
    MvcResult result = mockMvc.perform(post("/api/transactions/accounts/{accountId}/deposit", accountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andReturn();

    // Check status code manually
    assertEquals(400, result.getResponse().getStatus());
  }

  @Test
  void transfer_shouldReturnBadRequestWhenAmountIsZero() throws Exception {
    // Input data with invalid amount (zero)
    String sourceAccountId = "source-acc";
    TransferRequest request = new TransferRequest("target-acc", 0.0);

    // Perform request and verify validation error
    MvcResult result = mockMvc.perform(post("/api/transactions/accounts/{sourceAccountId}/transfer", sourceAccountId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andReturn();

    // Check status code manually
    assertEquals(400, result.getResponse().getStatus());
  }
}