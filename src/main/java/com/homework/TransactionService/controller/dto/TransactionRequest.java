package com.homework.TransactionService.controller.dto;

import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;

import java.math.BigDecimal;

public record TransactionRequest(
        String transactionId,
        TransactionType transactionType,
        BigDecimal amount,
        String currency,
        TransactionResult result) {

    public TransactionRequest {
        if (transactionId == null) {
            throw new IllegalArgumentException("Transaction id must not be null");
        }
        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type must not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
        if (result == null) {
            throw new IllegalArgumentException("Result must not be null");
        }
    }
}
