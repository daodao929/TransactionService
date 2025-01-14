package com.homework.TransactionService.controller;

import com.homework.TransactionService.model.Transaction;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.Instant;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void should_return_200_when_get_transaction_by_id_given_valid_id() throws Exception {
        when(transactionService.getTransaction("c-123")).thenReturn(
                new Transaction("c-123", new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS, Instant.parse("2025-01-14T14:07:02Z")));

        mockMvc.perform(get("/transactions/c-123"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"transactionId\":\"c-123\",\"amount\":13.34,\"currency\":\"USD\",\"result\":\"SUCCESS\",\"created\":\"2025-01-14T14:07:02Z\"}"));
    }
}
