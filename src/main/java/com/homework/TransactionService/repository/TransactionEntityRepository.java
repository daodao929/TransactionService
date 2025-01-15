package com.homework.TransactionService.repository;

import com.homework.TransactionService.repository.entity.TransactionEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {

    void deleteByTransactionId(@NotNull String transactionId);

    Optional<TransactionEntity> findByTransactionId(@NotNull String transactionId);
}
