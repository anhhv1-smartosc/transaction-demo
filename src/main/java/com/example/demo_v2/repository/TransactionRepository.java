package com.example.demo_v2.repository;

import com.example.demo_v2.enity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.senderAccount.id = :accountId OR t.receiverAccount.id = :accountId")
    List<Transaction> findTransactionByAccountId(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.senderAccount.id = :senderAccountId AND t.receiverAccount.id = :receiverAccountId")
    List<Transaction> findTransactionsBySenderAndReceiver(Long senderAccountId, Long receiverAccountId);
}
