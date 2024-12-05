package com.example.demo_v2.dto.response;

import lombok.*;

import java.util.Date;

@Data
public class AccountResponse {
    private Long id;
    private String name;
    private String email;
    private Double balance;
    private Date createdAt;

}
