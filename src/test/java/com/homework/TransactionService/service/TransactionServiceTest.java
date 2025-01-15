package com.homework.TransactionService.service;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.exception.DuplicateTransactionException;
import com.homework.TransactionService.exception.TransactionNotFoundException;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import com.homework.TransactionService.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionServiceTest {

    @MockitoBean
    private TransactionRepository transactionRepository;  // Mocking TransactionDB

    @Autowired
    private TransactionService transactionService;

    @Test
    void should_return_all_transactions_when_get_transactions() {
        when(transactionRepository.findAll()).thenReturn(
                List.of(new Transaction("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"), Instant.parse("2025-01-14T16:07:02Z"))));

        List<Transaction> queryResult = transactionService.getTransactions();
        assertThat(queryResult.size(), equalTo(1));

        Transaction transaction = queryResult.getFirst();
        assertThat(transaction.transactionId(), equalTo("c-123"));
        assertThat(transaction.amount().toString(), equalTo("13.34"));
        assertThat(transaction.transactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(transaction.currency(), equalTo("USD"));
        assertThat(transaction.result(), equalTo(TransactionResult.SUCCESS));
    }

    @Test
    void should_return_empty_list_when_get_transactions_given_no_transaction() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        List<Transaction> queryResult = transactionService.getTransactions();
        assertThat(queryResult.size(), equalTo(0));
    }

    @Test
    void should_return_transaction_id_when_save_transaction_given_valid_transaction() {
        when(transactionRepository.save(any(TransactionRequest.class))).thenReturn("1-231");

        TransactionRequest request = new TransactionRequest("1-231", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        assertThat(transactionService.saveTransaction(request), equalTo("1-231"));
    }

    @Test
    void should_throw_error_when_save_transaction_given_duplicate_transaction() {
        when(transactionRepository.save(any(TransactionRequest.class)))
                .thenThrow(new DuplicateTransactionException("Transaction 1-231 already exists"));

        TransactionRequest request = new TransactionRequest("1-231", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        DuplicateTransactionException exception = assertThrows(DuplicateTransactionException.class,
                () -> transactionService.saveTransaction(request));
        assertThat(exception.getMessage(), equalTo("Transaction 1-231 already exists"));
    }

    @Test
    void should_return_transaction_id_when_delete_transaction_given_exist_transaction_id() {
        when(transactionRepository.delete("1-231")).thenReturn("1-231");

        assertThat(transactionService.deleteTransaction("1-231"), equalTo("1-231"));
    }

    @Test
    void should_throw_error_when_delete_transaction_given_transaction_id_does_not_exist() {
        when(transactionRepository.delete("1-231"))
                .thenThrow(new TransactionNotFoundException("Transaction 1-231 does not exist"));

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
                () -> transactionService.deleteTransaction("1-231"));
        assertThat(exception.getMessage(), equalTo("Transaction 1-231 does not exist"));
    }

    @Test
    void should_return_updated_transaction_when_update_transaction_given_valid_request() {
        Transaction transaction = new Transaction("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"),
                "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"),
                Instant.parse("2025-01-14T16:07:02Z"));
        when(transactionRepository.update(anyString(), any(TransactionRequest.class))).thenReturn(transaction);

        TransactionRequest request = new TransactionRequest("c-123", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        assertThat(transactionService.updateTransaction("c-123", request), equalTo(transaction));
    }

    @Test
    void should_throw_exception_when_update_transaction_given_repository_throw_exception() {
        when(transactionRepository.update(anyString(), any(TransactionRequest.class)))
                .thenThrow(new IllegalArgumentException("Transaction id does not match"));

        TransactionRequest request = new TransactionRequest("1-231", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.updateTransaction("1-231", request));
        assertThat(exception.getMessage(), equalTo("Transaction id does not match"));
    }
}
