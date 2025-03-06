package com.nexusbank.application.port.in;

import com.nexusbank.domain.model.Account;
import java.util.List;

public interface GetAccountUseCase {
  /**
   * Obtiene una cuenta por su ID.
   * 
   * @param accountId El ID de la cuenta a buscar
   * @return La cuenta encontrada
   */
  Account getAccountById(String accountId);

  /**
   * Obtiene todas las cuentas de un usuario.
   * 
   * @param userId El ID del usuario propietario de las cuentas
   * @return Lista de cuentas del usuario
   */
  List<Account> getAccountsByUserId(String userId);
}