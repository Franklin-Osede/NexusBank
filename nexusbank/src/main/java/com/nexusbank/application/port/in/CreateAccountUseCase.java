package com.nexusbank.application.port.in;

import com.nexusbank.domain.model.Account;

public interface CreateAccountUseCase {
  /**
   * Crea una nueva cuenta bancaria para un usuario existente.
   * 
   * @param userId         El ID del usuario propietario de la cuenta
   * @param initialDeposit El monto inicial a depositar (opcional)
   * @return La cuenta creada
   */
  Account createAccount(String userId, Double initialDeposit);
}