package com.example.demo_v2.dto.request;


import lombok.Data;

@Data
public class TransactionRequest {

    private Long senderAccount;
    private Long receiverAccount;
    private Double amount;

}
