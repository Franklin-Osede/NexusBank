package com.nexusbank.application.service;

import com.nexusbank.application.port.in.DepositMoneyUseCase;
import com.nexusbank.application.port.in.TransferMoneyUseCase;
import com.nexusbank.application.port.out.LoadAccountPort;
import com.nexusbank.application.port.out.SaveAccountPort;
import com.nexusbank.application.port.out.SaveTransactionPort;
import com.nexusbank.domain.exception.AccountNotFoundException;
import com.nexusbank.domain.exception.InsufficientBalanceException;
import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;
import com.nexusbank.domain.model.Transaction;

import java.util.UUID;

public class TransactionService implements DepositMoneyUseCase, TransferMoneyUseCase {

  private final LoadAccountPort loadAccountPort;
  private final SaveAccountPort saveAccountPort;
  private final SaveTransactionPort saveTransactionPort;

  public TransactionService(LoadAccountPort loadAccountPort, SaveAccountPort saveAccountPort,
      SaveTransactionPort saveTransactionPort) {
    this.loadAccountPort = loadAccountPort;
    this.saveAccountPort = saveAccountPort;
    this.saveTransactionPort = saveTransactionPort;
  }

  @Override
  public Transaction depositMoney(String accountId, Double amount) {
    Account account = loadAccountPort.loadAccount(accountId)
        .orElseThrow(() -> new AccountNotFoundException("Account with id " + accountId + " not found"));

    // Usar la moneda de la cuenta para el depósito
    Money depositAmount = new Money(amount, account.getBalance().getCurrency());
    account.deposit(depositAmount);

    saveAccountPort.saveAccount(account);

    // Usar el factory method en lugar del constructor directo
    Transaction transaction = Transaction.createDeposit(
        UUID.randomUUID().toString(),
        accountId,
        depositAmount);

    return saveTransactionPort.saveTransaction(transaction);
  }

  @Override
  public Transaction transferMoney(String sourceAccountId, String targetAccountId, Double amount) {
    Account sourceAccount = loadAccountPort.loadAccount(sourceAccountId)
        .orElseThrow(() -> new AccountNotFoundException("Source account with id " + sourceAccountId + " not found"));

    Account targetAccount = loadAccountPort.loadAccount(targetAccountId)
        .orElseThrow(() -> new AccountNotFoundException("Target account with id " + targetAccountId + " not found"));

    // Verificar que ambas cuentas usen la misma moneda
    if (!sourceAccount.getBalance().getCurrency().equals(targetAccount.getBalance().getCurrency())) {
      throw new IllegalArgumentException("Cannot transfer between accounts with different currencies");
    }

    Money transferAmount = new Money(amount, sourceAccount.getBalance().getCurrency());

    if (sourceAccount.getBalance().isLessThan(transferAmount)) {
      throw new InsufficientBalanceException("Insufficient balance in account " + sourceAccountId);
    }

    sourceAccount.withdraw(transferAmount);
    targetAccount.deposit(transferAmount);

    saveAccountPort.saveAccount(sourceAccount);
    saveAccountPort.saveAccount(targetAccount);

    // Usar el factory method en lugar del constructor directo
    Transaction transaction = Transaction.createTransfer(
        UUID.randomUUID().toString(),
        sourceAccountId,
        targetAccountId,
        transferAmount);

    return saveTransactionPort.saveTransaction(transaction);
  }
}