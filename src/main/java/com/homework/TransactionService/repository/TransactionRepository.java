package com.homework.TransactionService.repository;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.repository.data.TransactionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TransactionRepository {

    @Autowired
    private TransactionDB db;

    public List<Transaction> findAll(){
        return db.getAll();
    }

    public String save(TransactionRequest transactionRequest){
        return db.saveTransaction(transactionRequest);
    }

    public String delete(String transactionId){
        return db.deleteTransaction(transactionId);
    }

    public Transaction update(String transactionId, TransactionRequest transactionRequest){
        return db.updateTransaction(transactionId, transactionRequest);
    }
}
