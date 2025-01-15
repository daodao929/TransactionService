package com.homework.TransactionService.repository.entity;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String transactionId;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private String currency;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionResult result;

    @NotNull
    @Column(nullable = false)
    private Instant created;

    @NotNull
    @Column(nullable = false)
    private Instant lastUpdated;

    public TransactionEntity() {
    }

    public TransactionEntity(String transactionId, TransactionType transactionType, BigDecimal amount,
                             String currency, TransactionResult result, Instant created, Instant lastUpdated) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.result = result;
        this.created = created;
        this.lastUpdated = lastUpdated;
    }

    public Transaction toTransaction() {
        return new Transaction(this.transactionId, this.transactionType, this.amount,
                this.currency, this.result, this.created, this.lastUpdated);
    }

    public static TransactionEntity from(TransactionRequest request) {
        return new TransactionEntity(request.transactionId(), request.transactionType(),
                request.amount(), request.currency(),request.result(),
                Instant.now(), Instant.now());
    }
}
