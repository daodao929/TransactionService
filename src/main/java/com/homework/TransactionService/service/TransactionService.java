package com.homework.TransactionService.service;

import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {
    public Transaction getTransaction(String transactionId){
        return new Transaction("id", new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS, Instant.now());
    }
}
