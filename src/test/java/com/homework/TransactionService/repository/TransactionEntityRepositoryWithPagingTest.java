package com.homework.TransactionService.repository;

import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import com.homework.TransactionService.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class TransactionEntityRepositoryWithPagingTest extends JpaTestHelper {

    @Autowired
    TransactionEntityRepositoryWithPaging transactionEntityRepositoryWithPaging;

    @Test
    void should_return_all_transactions_when_get_transactions() {
        Page<TransactionEntity> queryResult = transactionEntityRepositoryWithPaging.findAll(PageRequest.of(0, 10));

        List<TransactionEntity> entities = queryResult.getContent();
        assertThat(entities.size(), equalTo(2));
        TransactionEntity firstTransaction = entities.getFirst();
        assertThat(firstTransaction.getTransactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(firstTransaction.getTransactionId(), equalTo("TX001"));
        assertThat(firstTransaction.getAmount().toString(), equalTo("100.0000000"));
        assertThat(firstTransaction.getCurrency(), equalTo("USD"));
        assertThat(firstTransaction.getResult(), equalTo(TransactionResult.SUCCESS));
    }

}
