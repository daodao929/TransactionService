package com.homework.TransactionService.service;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.exception.DuplicateTransactionException;
import com.homework.TransactionService.exception.TransactionNotFoundException;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import com.homework.TransactionService.repository.entity.TransactionEntity;
import com.homework.TransactionService.repository.TransactionEntityRepository;
import com.homework.TransactionService.repository.TransactionEntityRepositoryWithPaging;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @MockitoBean
    private TransactionEntityRepository transactionEntityRepository;

    @MockitoBean
    private TransactionEntityRepositoryWithPaging transactionEntityRepositoryWithPaging;

    @Autowired
    private TransactionService transactionService;

    @Test
    void should_return_all_transactions_when_get_transactions() {
        when(transactionEntityRepositoryWithPaging.findAll(PageRequest.of(0, 1)))
                .thenReturn(createMockResponse());

        Page<Transaction> queryResult = transactionService.getTransactions(0, 1);
        assertThat(queryResult.getContent().size(), equalTo(1));

        Transaction transaction = queryResult.getContent().getFirst();
        assertThat(transaction.transactionId(), equalTo("T001"));
        assertThat(transaction.amount().toString(), equalTo("100.50"));
        assertThat(transaction.transactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(transaction.currency(), equalTo("USD"));
        assertThat(transaction.result(), equalTo(TransactionResult.SUCCESS));
    }

    @Test
    void should_return_transaction_id_when_save_transaction_given_valid_transaction() {
        TransactionEntity transactionEntity = new TransactionEntity("T001", TransactionType.PAYMENT,
                new BigDecimal("100.50"), "USD", TransactionResult.SUCCESS,
                Instant.parse("2025-01-14T14:07:02Z"), Instant.parse("2025-01-14T14:07:02Z"));
        when(transactionEntityRepository.save(any(TransactionEntity.class)))
                .thenReturn(transactionEntity);

        TransactionRequest request = new TransactionRequest("1-231", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        assertThat(transactionService.saveTransaction(request), equalTo(transactionEntity.toTransaction()));
    }

    @Test
    void should_throw_error_when_save_transaction_given_duplicate_transaction() {
        TransactionEntity transactionEntity = new TransactionEntity("1-231", TransactionType.PAYMENT,
                new BigDecimal("100.50"), "USD", TransactionResult.SUCCESS,
                Instant.parse("2025-01-14T14:07:02Z"), Instant.parse("2025-01-14T14:07:02Z"));
        when(transactionEntityRepository.findByTransactionId("1-231"))
                .thenReturn(Optional.of(transactionEntity));

        TransactionRequest request = new TransactionRequest("1-231", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        DuplicateTransactionException exception = assertThrows(DuplicateTransactionException.class,
                () -> transactionService.saveTransaction(request));
        assertThat(exception.getMessage(), equalTo("Transaction 1-231 already exists"));
    }

    @Test
    void should_return_transaction_id_when_delete_transaction_given_exist_transaction_id() {
        TransactionEntity transactionEntity = new TransactionEntity("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"),
                "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"),
                Instant.parse("2025-01-14T16:07:02Z"));
        when(transactionEntityRepository.findByTransactionId("c-123"))
                .thenReturn(Optional.of(transactionEntity));
        doNothing().when(transactionEntityRepository).deleteByTransactionId("c-123");

        transactionService.deleteTransaction("c-123");
    }

    @Test
    void should_throw_error_when_delete_transaction_given_transaction_id_does_not_exist() {
        when(transactionEntityRepository.findByTransactionId("1-231"))
                .thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
                () -> transactionService.deleteTransaction("1-231"));
        assertThat(exception.getMessage(), equalTo("Transaction 1-231 does not exist."));
    }

    @Test
    void should_return_updated_transaction_when_update_transaction_given_valid_request() {
        TransactionEntity transactionEntity = new TransactionEntity("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"),
                "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"),
                Instant.parse("2025-01-14T16:07:02Z"));
        when(transactionEntityRepository.findByTransactionId("c-123"))
                .thenReturn(Optional.of(transactionEntity));
        when(transactionEntityRepository.save(any(TransactionEntity.class)))
                .thenReturn(transactionEntity);

        assertThat(transactionService.updateTransaction("c-123", Optional.of(new BigDecimal("13.34")), Optional.of(TransactionResult.SUCCESS), Optional.of("USD")), equalTo(transactionEntity.toTransaction()));
    }

    @Test
    void should_throw_exception_when_update_transaction_given_transaction_not_found() {
        when(transactionEntityRepository.findByTransactionId("1-123"))
                .thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
                () -> transactionService.updateTransaction("1-231", Optional.of(new BigDecimal("13.34")), Optional.of(TransactionResult.SUCCESS), Optional.of("USD")));
        assertThat(exception.getMessage(), equalTo("Transaction 1-231 does not exist."));
    }

    private Page<TransactionEntity> createMockResponse() {
        List<TransactionEntity> transactionEntities = List.of(
                new TransactionEntity("T001", TransactionType.PAYMENT, new BigDecimal("100.50"),
                        "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"),
                        Instant.parse("2025-01-14T14:07:02Z")));

        PageRequest pageable = PageRequest.of(0, 1); // Page 0, size 3

        return new PageImpl<>(transactionEntities, pageable, 1); // Total elements = 10
    }

    private Page<TransactionEntity> createMockEmptyResponse() {
        List<TransactionEntity> transactionEntities = List.of();

        PageRequest pageable = PageRequest.of(0, 1); // Page 0, size 3

        return new PageImpl<>(transactionEntities, pageable, 0); // Total elements = 10
    }
}
