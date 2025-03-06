package com.nexusbank.infrastructure.persistence.adapter;

import com.nexusbank.application.port.out.SaveTransactionPort;
import com.nexusbank.domain.model.Transaction;
import com.nexusbank.infrastructure.persistence.entity.TransactionEntity;
import com.nexusbank.infrastructure.persistence.mapper.TransactionMapper;
import com.nexusbank.infrastructure.persistence.repository.TransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionPersistenceAdapter implements SaveTransactionPort {

  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;

  public TransactionPersistenceAdapter(TransactionRepository transactionRepository,
      TransactionMapper transactionMapper) {
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
  }

  @Override
  public Transaction saveTransaction(Transaction transaction) {
    TransactionEntity transactionEntity = transactionMapper.toJpaEntity(transaction);
    TransactionEntity savedEntity = transactionRepository.save(transactionEntity);
    return transactionMapper.toDomainEntity(savedEntity);
  }

  @Override
  public List<Transaction> loadTransactionsByAccountId(String accountId) {
    return transactionRepository.findByAccountId(accountId).stream()
        .map(transactionMapper::toDomainEntity)
        .collect(Collectors.toList());
  }
}