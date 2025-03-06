package com.nexusbank.infrastructure.persistence.mapper;

import com.nexusbank.domain.model.Account;
import com.nexusbank.domain.model.Money;
import com.nexusbank.infrastructure.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountMapper {

  public Account toDomainEntity(AccountEntity entity) {
    Account account = Account.createNew(entity.getId(), entity.getUserId(), entity.getCurrency());

    // Asumiendo que el método createNew crea una cuenta con balance cero
    // Si el balance de la entidad no es cero, necesitamos depositarlo
    if (entity.getBalance().compareTo(java.math.BigDecimal.ZERO) > 0) {
      account.deposit(new Money(entity.getBalance(), entity.getCurrency()));
    }

    // Establecer el estado activo/inactivo
    if (!entity.isActive()) {
      account.deactivate();
    }

    return account;
  }

  public AccountEntity toJpaEntity(Account account) {
    AccountEntity entity = new AccountEntity();
    entity.setId(account.getId());
    entity.setUserId(account.getUserId());
    entity.setBalance(account.getBalance().getAmount());
    entity.setCurrency(account.getBalance().getCurrency());
    entity.setActive(account.isActive());

    // Si es una entidad nueva, establecer las fechas de creación y actualización
    LocalDateTime now = LocalDateTime.now();
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    return entity;
  }
}