package com.nexusbank.domain.model;

/**
 * Enumeration representing the possible statuses of a transaction.
 */
public enum TransactionStatus {
  PENDING, // Transaction is in progress
  COMPLETED, // Transaction was successfully completed
  FAILED // Transaction failed to complete
}