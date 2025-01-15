package com.homework.TransactionService.repository;

import com.homework.TransactionService.repository.entity.TransactionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransactionEntityRepositoryWithPaging extends PagingAndSortingRepository<TransactionEntity, Long> {
}
