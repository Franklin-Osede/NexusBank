package com.nexusbank.domain.model;

/**
 * Enumeration representing the possible types of transactions in the system.
 */
public enum TransactionType {
  DEPOSIT, // Money is added to an account
  WITHDRAWAL, // Money is removed from an account
  TRANSFER // Money is moved between accounts
}