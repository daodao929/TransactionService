package com.homework.TransactionService.controller;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<Page<Transaction>> getTransaction(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Page<Transaction> result = transactionService.getTransactions(page, size);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> saveTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction result = transactionService.saveTransaction(transactionRequest);
        return ResponseEntity.status(201).body(result);
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> saveTransaction(@PathVariable("transactionId") String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @PutMapping("/transactions/{transactionId}")
    public ResponseEntity<Transaction> saveTransaction(@PathVariable("transactionId") String transactionId,
                                                       @RequestParam Optional<BigDecimal> amount,
                                                       @RequestParam Optional<TransactionResult> transactionResult,
                                                       @RequestParam Optional<String> currency) {
        Transaction result = transactionService.updateTransaction(transactionId, amount, transactionResult, currency);
        return ResponseEntity.ok().body(result);
    }
}
