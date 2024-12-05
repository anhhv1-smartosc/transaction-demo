package com.example.demo_v2.repository;


import com.example.demo_v2.enity.Account;
import com.example.demo_v2.enity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
public class TransactionRespositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Account senderAccount;
    private Account receiverAccount;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    public void setup() {
        // Create and save sender and receiver accounts
        senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setName("Sender Account");

        receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setName("Receiver Account");

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        transaction1 = new Transaction();
        transaction1.setSenderAccount(senderAccount);
        transaction1.setReceiverAccount(receiverAccount);
        transaction1.setAmount(100.0);
        transaction1.setCreatedAt(new Date());

        transaction2 = new Transaction();
        transaction2.setSenderAccount(receiverAccount);
        transaction2.setReceiverAccount(senderAccount);
        transaction2.setAmount(50.0);
        transaction2.setCreatedAt(new Date());

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    public void testFindTransactionByAccountId() {
        Long accountId = senderAccount.getId();

        List<Transaction> transactions = transactionRepository.findTransactionByAccountId(accountId);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().anyMatch(t -> t.getSenderAccount().getId().equals(accountId)));
        assertTrue(transactions.stream().anyMatch(t -> t.getReceiverAccount().getId().equals(accountId)));
    }

    @Test
    public void testFindTransactionByAccountId_noTransactionsFound() {
        Long accountId = 999L;

        List<Transaction> transactions = transactionRepository.findTransactionByAccountId(accountId);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void testFindTransactionsBySenderAndReceiver() {
        Long senderAccountId = senderAccount.getId();
        Long receiverAccountId = receiverAccount.getId();

        List<Transaction> transactions = transactionRepository.findTransactionsBySenderAndReceiver(senderAccountId, receiverAccountId);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(senderAccountId, transactions.get(0).getSenderAccount().getId());
        assertEquals(receiverAccountId, transactions.get(0).getReceiverAccount().getId());
    }

    @Test
    public void testFindTransactionsBySenderAndReceiver_noTransactionsFound() {
        Long senderAccountId = 999L;
        Long receiverAccountId = 888L;

        List<Transaction> transactions = transactionRepository.findTransactionsBySenderAndReceiver(senderAccountId, receiverAccountId);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

}
