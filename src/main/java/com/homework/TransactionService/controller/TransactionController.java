package com.homework.TransactionService.controller;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransaction() {
        List<Transaction> result = transactionService.getTransactions();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/transactions")
    public ResponseEntity<String> saveTransaction(@RequestBody TransactionRequest transactionRequest) {
        String result = transactionService.saveTransaction(transactionRequest);
        return ResponseEntity.status(201).body(result);
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> saveTransaction(@PathVariable("transactionId") String transactionId) {
        String result = transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/transactions/{transactionId}")
    public ResponseEntity<Transaction> saveTransaction(@PathVariable("transactionId") String transactionId,
                                                  @RequestBody TransactionRequest transactionRequest) {
        Transaction result = transactionService.updateTransaction(transactionId, transactionRequest);
        return ResponseEntity.ok().body(result);
    }
}
