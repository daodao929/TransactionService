package com.homework.TransactionService.service;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.exception.DuplicateTransactionException;
import com.homework.TransactionService.exception.TransactionNotFoundException;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.repository.entity.TransactionEntity;
import com.homework.TransactionService.repository.TransactionEntityRepository;
import com.homework.TransactionService.repository.TransactionEntityRepositoryWithPaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    @Autowired
    private TransactionEntityRepositoryWithPaging transactionEntityRepositoryWithPaging;

    @Autowired
    private TransactionEntityRepository transactionEntityRepository;

    public Transaction saveTransaction(TransactionRequest transactionRequest) {
        if(transactionEntityRepository.findByTransactionId(transactionRequest.transactionId()).isPresent()){
            throw new DuplicateTransactionException("Transaction " + transactionRequest.transactionId() + " already exists");
        }
        TransactionEntity entity = TransactionEntity.from(transactionRequest);
        return transactionEntityRepository.save(entity).toTransaction();
    }

    @Transactional
    public void deleteTransaction(String transactionId) {
        transactionEntityRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction " + transactionId + " does not exist."));
        transactionEntityRepository.deleteByTransactionId(transactionId);
    }

    public Transaction updateTransaction(String transactionId, Optional<BigDecimal> amount, Optional<TransactionResult> result, Optional<String> currency) {
        TransactionEntity entity = transactionEntityRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction " + transactionId + " does not exist."));
        amount.ifPresent(entity::setAmount);
        result.ifPresent(entity::setResult);
        currency.ifPresent(entity::setCurrency);
        return transactionEntityRepository.save(entity).toTransaction();
    }

    public Page<Transaction> getTransactions(int page, int size) {
        return transactionEntityRepositoryWithPaging
                .findAll(PageRequest.of(page, size))
                .map(TransactionEntity::toTransaction);
    }
}
