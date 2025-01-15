package com.homework.TransactionService.repository;

import com.homework.TransactionService.controller.dto.TransactionRequest;
import com.homework.TransactionService.model.TransactionResult;
import com.homework.TransactionService.model.TransactionType;
import com.homework.TransactionService.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class TransactionEntityRepositoryTest extends JpaTestHelper {

    @Autowired
    TransactionEntityRepository transactionEntityRepository;

    @Test
    void should_return_all_transactions_when_get_transactions() {
        TransactionEntity queryResult = transactionEntityRepository.findByTransactionId("TX001").get();

        assertThat(queryResult.getTransactionId(), equalTo("TX001"));
        assertThat(queryResult.getTransactionType(), equalTo(TransactionType.PAYMENT));
    }

    @Test
    void should_return_transaction_when_save_transaction_successfully_given_transaction_is_valid() {
        TransactionRequest request = new TransactionRequest("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        TransactionEntity saveResult = transactionEntityRepository.save(TransactionEntity.from(request));

        assertThat(saveResult.getTransactionId(), equalTo("c-123"));
        assertThat(saveResult.getAmount().toString(), equalTo("13.34"));
        assertThat(saveResult.getTransactionType(), equalTo(TransactionType.PAYMENT));
        assertThat(saveResult.getCurrency(), equalTo("USD"));
        assertThat(saveResult.getResult(), equalTo(TransactionResult.SUCCESS));
    }

    @Test
    void should_return_nothing_when_delete_transaction_successfully_given_transaction_exists() {
        TransactionRequest request = new TransactionRequest("c-123", TransactionType.PAYMENT, new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS);

        transactionEntityRepository.save(TransactionEntity.from(request));

        transactionEntityRepository.deleteByTransactionId("c-123");
    }

    @Test
    void should_return_updated_transaction_when_update_transaction_successfully_given_valid_transaction() {
        TransactionEntity entity = TransactionEntity.from(new TransactionRequest("c-123", TransactionType.PAYMENT,
                new BigDecimal("13.34"), "USD", TransactionResult.SUCCESS));
        TransactionEntity originalEntity = transactionEntityRepository.save(entity);

        originalEntity.setAmount(new BigDecimal("1999"));
        originalEntity.setResult(TransactionResult.DECLINED);

        transactionEntityRepository.save(originalEntity);

        TransactionEntity updateResult = transactionEntityRepository.findByTransactionId(originalEntity.getTransactionId()).get();

        assertThat(updateResult.getId(), equalTo(originalEntity.getId()));
        assertThat(updateResult.getTransactionId(), equalTo(originalEntity.getTransactionId()));
        assertThat(updateResult.getAmount().toString(), equalTo("1999"));
        assertThat(updateResult.getResult(), equalTo(TransactionResult.DECLINED));
    }
}
