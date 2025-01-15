package com.homework.TransactionService.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(String transactionId,
                          TransactionType transactionType,
                          BigDecimal amount,
                          String currency,
                          TransactionResult result,
                          Instant created,
                          Instant lastUpdated) {
}
