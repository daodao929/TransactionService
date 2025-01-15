package com.homework.TransactionService.controller;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.exception.DuplicateTransactionException;
import com.homework.TransactionService.exception.TransactionNotFoundException;
import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import com.homework.TransactionService.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void should_return_200_when_get_transactions_given_have_one_transaction() throws Exception {
        Page<Transaction> mockData = createMockResponse();
        when(transactionService.getTransactions(0, 1)).thenReturn(mockData);

        mockMvc.perform(get("/transactions?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"transactionId\":\"T001\",\"transactionType\":\"PAYMENT\",\"amount\":100.50,\"currency\":\"USD\",\"result\":\"SUCCESS\",\"created\":\"2025-01-14T14:07:02Z\",\"lastUpdated\":\"2025-01-14T14:07:02Z\"}],\"pageable\":{\"pageNumber\":0,\"pageSize\":1,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"offset\":0,\"paged\":true,\"unpaged\":false},\"last\":true,\"totalPages\":1,\"totalElements\":1,\"size\":1,\"number\":0,\"sort\":{\"empty\":true,\"unsorted\":true,\"sorted\":false},\"first\":true,\"numberOfElements\":1,\"empty\":false}"));
    }

    @Test
    void should_return_201_when_save_transaction_given_valid_info() throws Exception {
        when(transactionService.saveTransaction(any(TransactionRequest.class))).thenReturn(new Transaction("T001", TransactionType.PAYMENT, new BigDecimal("100.50"),
                "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"),
                Instant.parse("2025-01-14T14:07:02Z")));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"1-231\",\"transactionType\":\"PAYMENT\",\"amount\":199.98,\"currency\":\"USD\",\"result\":\"SUCCESS\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"transactionId\":\"T001\",\"transactionType\":\"PAYMENT\",\"amount\":100.50,\"currency\":\"USD\",\"result\":\"SUCCESS\",\"created\":\"2025-01-14T14:07:02Z\",\"lastUpdated\":\"2025-01-14T14:07:02Z\"}"));
    }

    @Test
    void should_return_400_when_save_transaction_given_invalid_info() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":199.98}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":400,\"message\":\"Transaction id must not be null\"}"));
    }

    @Test
    void should_return_400_when_save_transaction_given_invalid_transaction_result() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"result\":\"invalid\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_409_when_save_transaction_given_transaction_already_exists() throws Exception {
        when(transactionService.saveTransaction(any(TransactionRequest.class))).thenThrow(
                new DuplicateTransactionException("Transaction already exist")
        );

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"1-231\",\"transactionType\":\"PAYMENT\",\"amount\":199.98,\"currency\":\"USD\",\"result\":\"SUCCESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("{\"status\":409,\"message\":\"Transaction already exist\"}"));
    }

    @Test
    void should_return_200_when_delete_transaction_given_transaction_exist() throws Exception {
        doNothing().when(transactionService).deleteTransaction("fake-uuid");

        mockMvc.perform(delete("/transactions/fake-uuid"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction deleted successfully"));
    }

    @Test
    void should_return_404_when_delete_transaction_given_transaction_not_exist() throws Exception {
        doThrow(new TransactionNotFoundException("fake-uuid not exists."))
                .when(transactionService).deleteTransaction("fake-uuid");

        mockMvc.perform(delete("/transactions/fake-uuid"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":404,\"message\":\"fake-uuid not exists.\"}"));
    }


    @Test
    void should_return_200_when_update_transaction_given_valid_info() throws Exception {
        when(transactionService.updateTransaction(anyString(), any(), any(), any())).thenReturn(
                new Transaction("c-123", TransactionType.REFUND, new BigDecimal("13.34"), "USD", TransactionResult.FAILURE, Instant.parse("2025-01-14T14:07:02Z"), Instant.parse("2025-01-14T16:07:02Z")));

        mockMvc.perform(put("/transactions/c-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"c-123\",\"transactionType\":\"REFUND\",\"amount\":13.34,\"currency\":\"USD\",\"result\":\"FAILURE\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"transactionId\":\"c-123\",\"transactionType\":\"REFUND\",\"amount\":13.34,\"currency\":\"USD\",\"result\":\"FAILURE\",\"created\":\"2025-01-14T14:07:02Z\",\"lastUpdated\":\"2025-01-14T16:07:02Z\"}"));
    }

    @Test
    void should_return_404_when_update_transaction_given_transaction_not_exist() throws Exception {
        when(transactionService.updateTransaction(anyString(), any(), any(), any()))
                .thenThrow(new TransactionNotFoundException("Transaction fake-uuid does not exists."));

        mockMvc.perform(put("/transactions/fake-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"fake-uuid\",\"transactionType\":\"PAYMENT\",\"amount\":13.34,\"currency\":\"USD\",\"result\":\"FAILURE\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":404,\"message\":\"Transaction fake-uuid does not exists.\"}"));
    }

    @Test
    void should_return_400_when_update_transaction_given_transactionId_does_not_match() throws Exception {
        when(transactionService.updateTransaction(anyString(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Transaction id does not match."));

        mockMvc.perform(put("/transactions/fake-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionId\":\"1-1234\",\"transactionType\":\"REFUND\",\"amount\":13.34,\"currency\":\"USD\",\"result\":\"FAILURE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":400,\"message\":\"Transaction id does not match.\"}"));
    }

    private Page<Transaction> createMockResponse() {
        List<Transaction> transactions = List.of(
                new Transaction("T001", TransactionType.PAYMENT, new BigDecimal("100.50"),
                        "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z"),
                        Instant.parse("2025-01-14T14:07:02Z")));

        PageRequest pageable = PageRequest.of(0, 1); // Page 0, size 3

        return new PageImpl<>(transactions, pageable, 1); // Total elements = 10
    }
}
