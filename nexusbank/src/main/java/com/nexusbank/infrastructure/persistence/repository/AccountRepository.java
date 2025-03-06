package com.nexusbank.infrastructure.persistence.repository;

import com.nexusbank.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
  List<AccountEntity> findByUserId(String userId);
}