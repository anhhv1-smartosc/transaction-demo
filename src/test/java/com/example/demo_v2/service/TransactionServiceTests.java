package com.example.demo_v2.service;

import com.example.demo_v2.dto.request.TransactionRequest;
import com.example.demo_v2.dto.response.TransactionResponse;
import com.example.demo_v2.enity.Account;
import com.example.demo_v2.enity.Transaction;
import com.example.demo_v2.mapper.TransactionMapper;
import com.example.demo_v2.repository.AccountRepository;
import com.example.demo_v2.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account senderAccount;
    private Account receiverAccount;
    private TransactionRequest transactionRequest;
    private Transaction transaction;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setBalance(1000.0);

        receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setBalance(500.0);

        transactionRequest = new TransactionRequest();
        transactionRequest.setSenderAccount(1L);
        transactionRequest.setReceiverAccount(2L);
        transactionRequest.setAmount(200.0);

        transaction = new Transaction();
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setAmount(200.0);
        transaction.setCreatedAt(new Date());

        transactionResponse = new TransactionResponse();
        transactionResponse.setSenderAccount(senderAccount.getId());
        transactionResponse.setReceiverAccount(receiverAccount.getId());
        transactionResponse.setAmount(200.0);
        transactionResponse.setCreatedAt(transaction.getCreatedAt());
    }

    @Test
    void testGetTransactionsByAccountId() {

        when(transactionRepository.findTransactionByAccountId(1L)).thenReturn(List.of(transaction));
        when(transactionMapper.toTransactionResponse(transaction)).thenReturn(transactionResponse);

        List<TransactionResponse> result = transactionService.getTransactionsByAccountId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(200.0, result.get(0).getAmount());
        verify(transactionRepository, times(1)).findTransactionByAccountId(1L);
        verify(transactionMapper, times(1)).toTransactionResponse(transaction);
    }

    @Test
    void testGetTransactionsBySenderAndReceiver() {
        when(transactionRepository.findTransactionsBySenderAndReceiver(1L, 2L)).thenReturn(List.of(transaction));
        when(transactionMapper.toTransactionResponse(transaction)).thenReturn(transactionResponse);


        List<TransactionResponse> result = transactionService.getTransactionsBySenderAndReceiver(1L, 2L);


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(200.0, result.get(0).getAmount());
        verify(transactionRepository, times(1)).findTransactionsBySenderAndReceiver(1L, 2L);
        verify(transactionMapper, times(1)).toTransactionResponse(transaction);
    }

    @Test
    void testCreateTransaction_success() {

        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toTransactionResponse(transaction)).thenReturn(transactionResponse);


        TransactionResponse result = transactionService.createTransaction(transactionRequest);


        assertNotNull(result);
        assertEquals(200.0, result.getAmount());
        assertEquals(senderAccount.getId(), result.getSenderAccount());
        assertEquals(receiverAccount.getId(), result.getReceiverAccount());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(receiverAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(transactionMapper, times(1)).toTransactionResponse(transaction);
    }

    @Test
    void testCreateTransaction_insufficientBalance() {

        senderAccount.setBalance(100.0);  // Insufficient balance
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(receiverAccount));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });

        assertEquals("Insufficient balance in sender's account", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_senderNotFound() {

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });

        assertEquals("Sender account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(0)).findById(2L);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_receiverNotFound() {

        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(transactionRequest);
        });

        assertEquals("Receiver account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, times(0)).save(any(Account.class));
        verify(transactionRepository, times(0)).save(any(Transaction.class));
    }
}