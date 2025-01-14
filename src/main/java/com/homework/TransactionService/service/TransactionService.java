package com.homework.TransactionService.service;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransaction(){
        return transactionRepository.findAll();
    }

    public String saveTransaction(TransactionRequest transactionRequest){
        return transactionRepository.save(transactionRequest);
    }

    public String deleteTransaction(String transactionId){
        return transactionRepository.delete(transactionId);
    }

    public Transaction updateTransaction(String transactionId, TransactionRequest transactionRequest){
        return transactionRepository.update(transactionId, transactionRequest);
    }
}
