package com.nexusbank.application.port.in;

import com.nexusbank.domain.model.Transaction;

public interface DepositMoneyUseCase {
  /**
   * Realiza un depósito de dinero en una cuenta.
   * 
   * @param accountId El ID de la cuenta donde depositar
   * @param amount    El monto a depositar
   * @return La transacción generada por el depósito
   */
  Transaction depositMoney(String accountId, Double amount);
}