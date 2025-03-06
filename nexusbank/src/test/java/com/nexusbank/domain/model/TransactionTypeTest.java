package com.nexusbank.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

  @Test
  void shouldDefineCorrectTransactionTypes() {
    // Then
    assertEquals(3, TransactionType.values().length);
    assertNotNull(TransactionType.DEPOSIT);
    assertNotNull(TransactionType.WITHDRAWAL);
    assertNotNull(TransactionType.TRANSFER);
  }
}