package com.nexusbank.application.port.in;

import com.nexusbank.domain.model.Transaction;

public interface TransferMoneyUseCase {
  /**
   * Transfiere dinero de una cuenta a otra.
   * 
   * @param sourceAccountId ID de la cuenta origen
   * @param targetAccountId ID de la cuenta destino
   * @param amount          Monto a transferir
   * @return La transacción generada por la transferencia
   */
  Transaction transferMoney(String sourceAccountId, String targetAccountId, Double amount);
}