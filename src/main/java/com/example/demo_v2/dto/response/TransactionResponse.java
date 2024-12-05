package com.example.demo_v2.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionResponse {
    private Long id;
    private Long senderAccount;
    private Long receiverAccount;
    private Double amount;
    private Date createdAt;
}
