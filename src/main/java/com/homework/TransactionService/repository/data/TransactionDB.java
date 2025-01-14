package com.homework.TransactionService.repository.data;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class TransactionDB {
    private final HashMap<String, Transaction> transactions = new HashMap<>();
    private final HashMap<String, Transaction> deletedTransactions = new HashMap<>();

    public TransactionDB() {
        IntStream.range(0, 10).forEach(i -> {
            String uniqueId = generateUniqueId();
            transactions.put(uniqueId+"1", new Transaction(uniqueId+"1", new BigDecimal("12.13"), "USD",
                    TransactionResult.SUCCESS, Instant.now(), Instant.now()));
            transactions.put(uniqueId+"2", new Transaction(uniqueId+"2", new BigDecimal("12.13"), "USD",
                    TransactionResult.DECLINED, Instant.now(), Instant.now()));
            transactions.put(uniqueId+"3", new Transaction(uniqueId+"3", new BigDecimal("12.13"), "USD",
                    TransactionResult.FAILURE, Instant.now(), Instant.now()));
        });
    }

    public List<Transaction> getAll(){
        return new ArrayList<>(this.transactions.values());
    }

    public String saveTransaction(TransactionRequest request) {
        String transactionId = generateUniqueId();
        transactions.put(transactionId, new Transaction(transactionId, request.amount(), request.currency(),
                request.result(), Instant.now(), Instant.now()));
        return transactionId;
    }

    public String deleteTransaction(String transactionId) {
        deletedTransactions.put(transactionId, transactions.get(transactionId));
        transactions.remove(transactionId);
        return transactionId;
    }

    public Transaction updateTransaction(String transactionId, TransactionRequest request) {
        Transaction originalTransaction = transactions.get(transactionId);
        Transaction newTransaction = new Transaction(transactionId, request.amount(), request.currency(),
                request.result(), originalTransaction.created(), Instant.now());
        transactions.put(transactionId, newTransaction);
        return newTransaction;
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
