package com.nexusbank.infrastructure.persistence.repository;

import com.nexusbank.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
  List<TransactionEntity> findByAccountId(String accountId);

  List<TransactionEntity> findByAccountIdOrTargetAccountId(String accountId, String targetAccountId);
}