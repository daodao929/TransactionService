package com.homework.TransactionService.controller.dto;

import com.homework.TransactionService.model.TransactionResult;

import java.math.BigDecimal;

public record TransactionRequest(
        BigDecimal amount,
        String currency,
        TransactionResult result) {
}
