package com.example.demo_v2.service;

import com.example.demo_v2.dto.request.TransactionRequest;
import com.example.demo_v2.dto.response.TransactionResponse;
import com.example.demo_v2.enity.Account;
import com.example.demo_v2.enity.Transaction;
import com.example.demo_v2.mapper.TransactionMapper;
import com.example.demo_v2.repository.AccountRepository;
import com.example.demo_v2.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private AccountRepository accountRepository;


    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findTransactionByAccountId(accountId);
        return transactions.stream()
                .map(transactionMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsBySenderAndReceiver(Long senderAccountId, Long receiverAccountId) {
        List<Transaction> transactions = transactionRepository.findTransactionsBySenderAndReceiver(senderAccountId, receiverAccountId);

        return transactions.stream()
                .map(transactionMapper::toTransactionResponse)
                .collect(Collectors.toList());
    }


    public TransactionResponse createTransaction(TransactionRequest request) {
        Account senderAccount = accountRepository.findById(request.getSenderAccount())
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiverAccount = accountRepository.findById(request.getReceiverAccount())
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (senderAccount.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance in sender's account");
        }

        senderAccount.setBalance(senderAccount.getBalance() - request.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + request.getAmount());


        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);


        Transaction transaction = new Transaction();
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(new Date());
        Transaction savedTransaction = transactionRepository.save(transaction);


        return transactionMapper.toTransactionResponse(savedTransaction);
    }
}
