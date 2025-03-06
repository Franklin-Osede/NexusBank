package com.nexusbank.application.service;

import com.nexusbank.application.port.in.CreateAccountUseCase;
import com.nexusbank.application.port.in.GetAccountUseCase;
import com.nexusbank.application.port.out.LoadAccountPort;
import com.nexusbank.application.port.out.LoadUserPort;
import com.nexusbank.application.port.out.SaveAccountPort;
import com.nexusbank.domain.exception.AccountNotFoundException;
import com.nexusbank.domain.exception.UserNotFoundException;
import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;

import java.util.List;
import java.util.UUID;

public class AccountService implements CreateAccountUseCase, GetAccountUseCase {

  private final SaveAccountPort saveAccountPort;
  private final LoadAccountPort loadAccountPort;
  private final LoadUserPort loadUserPort;

  public AccountService(SaveAccountPort saveAccountPort, LoadAccountPort loadAccountPort, LoadUserPort loadUserPort) {
    this.saveAccountPort = saveAccountPort;
    this.loadAccountPort = loadAccountPort;
    this.loadUserPort = loadUserPort;
  }

  @Override
  public Account createAccount(String userId, Double initialDeposit) {
    // Verificar que el usuario existe
    loadUserPort.loadUser(userId)
        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

    // Crear una cuenta usando el factory method - asumimos USD como moneda
    // predeterminada
    String newAccountId = UUID.randomUUID().toString();
    Account newAccount = Account.createNew(newAccountId, userId, "USD");

    // Si hay un depósito inicial, realizarlo
    if (initialDeposit != null && initialDeposit > 0) {
      newAccount.deposit(new Money(initialDeposit, "USD"));
    }

    return saveAccountPort.saveAccount(newAccount);
  }

  @Override
  public Account getAccountById(String accountId) {
    return loadAccountPort.loadAccount(accountId)
        .orElseThrow(() -> new AccountNotFoundException("Account with id " + accountId + " not found"));
  }

  @Override
  public List<Account> getAccountsByUserId(String userId) {
    return loadAccountPort.loadAccountsByUserId(userId);
  }
}