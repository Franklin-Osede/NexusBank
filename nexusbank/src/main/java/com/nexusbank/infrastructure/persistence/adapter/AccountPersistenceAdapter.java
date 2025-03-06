package com.nexusbank.infrastructure.persistence.adapter;

import com.nexusbank.application.port.out.LoadAccountPort;
import com.nexusbank.application.port.out.SaveAccountPort;
import com.nexusbank.domain.model.Account;
import com.nexusbank.infrastructure.persistence.entity.AccountEntity;
import com.nexusbank.infrastructure.persistence.mapper.AccountMapper;
import com.nexusbank.infrastructure.persistence.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountPersistenceAdapter implements LoadAccountPort, SaveAccountPort {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;

  public AccountPersistenceAdapter(AccountRepository accountRepository, AccountMapper accountMapper) {
    this.accountRepository = accountRepository;
    this.accountMapper = accountMapper;
  }

  @Override
  public Optional<Account> loadAccount(String accountId) {
    return accountRepository.findById(accountId)
        .map(accountMapper::toDomainEntity);
  }

  @Override
  public List<Account> loadAccountsByUserId(String userId) {
    return accountRepository.findByUserId(userId).stream()
        .map(accountMapper::toDomainEntity)
        .collect(Collectors.toList());
  }

  @Override
  public Account saveAccount(Account account) {
    AccountEntity accountEntity = accountMapper.toJpaEntity(account);
    AccountEntity savedEntity = accountRepository.save(accountEntity);
    return accountMapper.toDomainEntity(savedEntity);
  }
}