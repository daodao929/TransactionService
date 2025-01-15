package com.homework.TransactionService.repository;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.exception.DuplicateTransactionException;
import com.homework.TransactionService.exception.TransactionNotFoundException;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void should_return_all_transactions_when_get_transactions() {
        List<Transaction> queryResult = transactionRepository.findAll();
        assertThat(queryResult.size(), equalTo(30));
    }

    @Test
    void should_return_transaction_id_when_save_transaction_successfully_given_transaction_is_valid() {
        TransactionRequest request = new TransactionRequest("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        String saveResult = transactionRepository.save(request);
        assertThat(saveResult, equalTo("c-123"));

        Transaction res = transactionRepository.findById("c-123");
        assertThat(res.transactionId(), equalTo("c-123"));
        assertThat(res.amount().toString(), equalTo("13.34"));
        assertThat(res.transactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(res.currency(), equalTo("USD"));
        assertThat(res.result(), equalTo(TransactionResult.SUCCESS));
    }

    @Test
    void should_throw_DuplicateTransactionException_when_save_transaction_given_transaction_already_exists() {
        String transactionId = generateUniqueId();
        TransactionRequest request = new TransactionRequest(transactionId, TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);
        transactionRepository.save(request);

        DuplicateTransactionException exception = assertThrows(DuplicateTransactionException.class, () -> transactionRepository.save(request));
        assertThat(exception.getMessage(), equalTo("Transaction " + transactionId + " already exists"));
    }

    @Test
    void should_return_transaction_id_when_delete_transaction_successfully_given_transaction_exists() {
        String transactionId = generateUniqueId();
        TransactionRequest request = new TransactionRequest(transactionId, TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);
        transactionRepository.save(request);

        String deleteResponse = transactionRepository.delete(transactionId);
        assertThat(deleteResponse, equalTo(transactionId));

        Transaction deletedTransaction = transactionRepository.findDeletedTransactionById(transactionId);
        assertThat(deletedTransaction.transactionId(), equalTo(transactionId));
        assertThat(deletedTransaction.amount().toString(), equalTo("13.34"));
        assertThat(deletedTransaction.transactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(deletedTransaction.currency(), equalTo("USD"));
        assertThat(deletedTransaction.result(), equalTo(TransactionResult.SUCCESS));
    }

    @Test
    void should_throw_TransactionNotFoundException_when_delete_transaction_given_transaction_does_not_exists() {
        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
                () -> transactionRepository.delete("transaction_id"));
        assertThat(exception.getMessage(), equalTo("Transaction transaction_id does not exist."));
    }

    @Test
    void should_return_updated_transaction_when_update_transaction_successfully_given_valid_transaction() {
        String transactionId = generateUniqueId();
        TransactionRequest request = new TransactionRequest(transactionId, TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);
        transactionRepository.save(request);

        TransactionRequest updateRequest = new TransactionRequest(transactionId, TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.DECLINED);

        transactionRepository.update(transactionId, updateRequest);

        Transaction updatedTransaction = transactionRepository.findById(transactionId);
        assertThat(updatedTransaction.transactionId(), equalTo(transactionId));
        assertThat(updatedTransaction.amount().toString(), equalTo("13.34"));
        assertThat(updatedTransaction.transactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(updatedTransaction.currency(), equalTo("USD"));
        assertThat(updatedTransaction.result(), equalTo(TransactionResult.DECLINED));
    }

    @Test
    void should_throw_TransactionNotFoundException_when_update_transaction_given_transaction_does_not_exists() {
        String transactionId = generateUniqueId();
        TransactionRequest request = new TransactionRequest(transactionId, TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class,
                () -> transactionRepository.update(transactionId, request));
        assertThat(exception.getMessage(), equalTo("Transaction " + transactionId + " does not exist."));
    }

    @Test
    void should_throw_IllegalArgumentException_when_update_transaction_given_transaction_id_does_not_match() {
        String transactionId = generateUniqueId();
        TransactionRequest request = new TransactionRequest(transactionId, TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);
        transactionRepository.save(request);

        TransactionRequest updateRequest = new TransactionRequest("random_id", TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.DECLINED);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionRepository.update(transactionId, updateRequest));
        assertThat(exception.getMessage(), equalTo("Transaction id does not match."));
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
