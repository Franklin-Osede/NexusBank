package com.nexusbank.infrastructure.persistence.mapper;

import com.nexusbank.domain.model.Money;
import com.nexusbank.domain.model.Transaction;
import com.nexusbank.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

  public Transaction toDomainEntity(TransactionEntity entity) {
    Money amount = new Money(entity.getAmount(), entity.getCurrency());
    Transaction transaction;

    switch (entity.getType()) {
      case DEPOSIT:
        transaction = Transaction.createDeposit(entity.getId(), entity.getAccountId(), amount);
        break;
      case WITHDRAWAL:
        transaction = Transaction.createWithdrawal(entity.getId(), entity.getAccountId(), amount);
        break;
      case TRANSFER:
        if (entity.getStatus() == com.nexusbank.domain.model.TransactionStatus.PENDING) {
          transaction = Transaction.createPendingTransfer(
              entity.getId(), entity.getAccountId(), entity.getTargetAccountId(), amount);
        } else {
          transaction = Transaction.createTransfer(
              entity.getId(), entity.getAccountId(), entity.getTargetAccountId(), amount);
        }
        break;
      default:
        throw new IllegalArgumentException("Unknown transaction type: " + entity.getType());
    }

    // Actualizar el estado si es necesario
    if (entity.getStatus() == com.nexusbank.domain.model.TransactionStatus.FAILED) {
      transaction.markAsFailed();
    }

    // Actualizar la descripción si es diferente de la predeterminada
    if (entity.getDescription() != null && !entity.getDescription().equals(transaction.getDescription())) {
      transaction.updateDescription(entity.getDescription());
    }

    return transaction;
  }

  public TransactionEntity toJpaEntity(Transaction transaction) {
    TransactionEntity entity = new TransactionEntity();
    entity.setId(transaction.getId());
    entity.setAccountId(transaction.getAccountId());
    entity.setTargetAccountId(transaction.getTargetAccountId());
    entity.setAmount(transaction.getAmount().getAmount());
    entity.setCurrency(transaction.getAmount().getCurrency());
    entity.setType(transaction.getType());
    entity.setStatus(transaction.getStatus());
    entity.setDescription(transaction.getDescription());

    // Establecer fechas de creación y actualización
    LocalDateTime now = LocalDateTime.now();
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);

    return entity;
  }
}