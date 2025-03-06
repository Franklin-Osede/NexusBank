package com.nexusbank.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionStatusTest {

  @Test
  void shouldDefineCorrectTransactionStatuses() {
    // Then
    assertEquals(3, TransactionStatus.values().length);
    assertNotNull(TransactionStatus.PENDING);
    assertNotNull(TransactionStatus.COMPLETED);
    assertNotNull(TransactionStatus.FAILED);
  }
}