package com.homework.TransactionService.controller;

import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@RequiredArgsConstructor
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable("id") String id) {
        Transaction result = transactionService.getTransaction(id);
        return ResponseEntity.ok().body(result);
    }
}
