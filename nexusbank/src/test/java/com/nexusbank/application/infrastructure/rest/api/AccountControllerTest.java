package com.nexusbank.application.infrastructure.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nexusbank.application.port.in.CreateAccountUseCase;
import com.nexusbank.application.port.in.GetAccountUseCase;
import com.nexusbank.domain.exception.AccountNotFoundException;
import com.nexusbank.domain.exception.UserNotFoundException;
import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;
import com.nexusbank.infrastructure.rest.controller.AccountController;
import com.nexusbank.infrastructure.rest.dto.request.CreateAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

  private MockMvc mockMvc;

  @Mock
  private CreateAccountUseCase createAccountUseCase;

  @Mock
  private GetAccountUseCase getAccountUseCase;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    // Se instancia el controlador utilizando los puertos de entrada simulados
    AccountController accountController = new AccountController(createAccountUseCase, getAccountUseCase);
    mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()); // Permite la serialización de LocalDateTime
  }

  @Test
  void createAccount_shouldReturnCreatedStatus() throws Exception {
    // Datos de entrada
    CreateAccountRequest request = new CreateAccountRequest("user-123", 100.0);

    // Se simula la creación de la cuenta
    Account mockAccount = Account.createNew("acc-123", "user-123", "USD");
    mockAccount.deposit(new Money(100.0, "USD"));

    when(createAccountUseCase.createAccount(anyString(), anyDouble())).thenReturn(mockAccount);

    // Se realiza la petición POST y se verifican los resultados esperados
    mockMvc.perform(post("/api/accounts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("acc-123"))
        .andExpect(jsonPath("$.userId").value("user-123"))
        // Se asume que el balance se devuelve como objeto anidado con campos "amount" y
        // "currency"
        .andExpect(jsonPath("$.balance.amount").value(100.0))
        .andExpect(jsonPath("$.balance.currency").value("USD"));

    verify(createAccountUseCase).createAccount("user-123", 100.0);
  }

  @Test
  void createAccount_shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
    // Datos de entrada para un usuario no existente
    CreateAccountRequest request = new CreateAccountRequest("non-existent-user", 100.0);
    when(createAccountUseCase.createAccount(anyString(), anyDouble()))
        .thenThrow(new UserNotFoundException("non-existent-user"));

    mockMvc.perform(post("/api/accounts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  void getAccount_shouldReturnAccount() throws Exception {
    String accountId = "acc-123";
    Account mockAccount = Account.createNew(accountId, "user-123", "USD");
    mockAccount.deposit(new Money(500.0, "USD"));

    when(getAccountUseCase.getAccountById(accountId)).thenReturn(mockAccount);

    mockMvc.perform(get("/api/accounts/{accountId}", accountId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(accountId))
        .andExpect(jsonPath("$.userId").value("user-123"))
        .andExpect(jsonPath("$.balance.amount").value(500.0))
        .andExpect(jsonPath("$.balance.currency").value("USD"));

    verify(getAccountUseCase).getAccountById(accountId);
  }

  @Test
  void getAccount_shouldReturnNotFoundWhenAccountDoesNotExist() throws Exception {
    String accountId = "non-existent-account";
    when(getAccountUseCase.getAccountById(accountId))
        .thenThrow(new AccountNotFoundException(accountId));

    mockMvc.perform(get("/api/accounts/{accountId}", accountId))
        .andExpect(status().isNotFound());
  }

  @Test
  void getAccountsByUserId_shouldReturnListOfAccounts() throws Exception {
    String userId = "user-123";
    Account account1 = Account.createNew("acc-1", userId, "USD");
    account1.deposit(new Money(500.0, "USD"));
    Account account2 = Account.createNew("acc-2", userId, "USD");
    account2.deposit(new Money(300.0, "USD"));

    List<Account> accounts = Arrays.asList(account1, account2);
    when(getAccountUseCase.getAccountsByUserId(userId)).thenReturn(accounts);

    mockMvc.perform(get("/api/accounts/user/{userId}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value("acc-1"))
        .andExpect(jsonPath("$[0].balance.amount").value(500.0))
        .andExpect(jsonPath("$[1].id").value("acc-2"))
        .andExpect(jsonPath("$[1].balance.amount").value(300.0));

    verify(getAccountUseCase).getAccountsByUserId(userId);
  }
}
