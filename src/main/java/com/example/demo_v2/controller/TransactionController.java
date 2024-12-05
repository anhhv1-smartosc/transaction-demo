package com.example.demo_v2.controller;

import com.example.demo_v2.dto.request.TransactionRequest;
import com.example.demo_v2.dto.response.TransactionResponse;
import com.example.demo_v2.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.createTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    // Get transactions between a specific sender and receiver
    @GetMapping("/list")
    public ResponseEntity<List<TransactionResponse>> getTransactionsBySenderAndReceiver(
            @RequestParam Long senderAccountId,
            @RequestParam Long receiverAccountId) {

        List<TransactionResponse> transactions = transactionService.getTransactionsBySenderAndReceiver(senderAccountId, receiverAccountId);
        return ResponseEntity.ok(transactions);
    }
}
