package com.example.demo_v2.controller;

import com.example.demo_v2.dto.response.AccountResponse;
import com.example.demo_v2.enity.Account;
import com.example.demo_v2.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody Account account) {
        AccountResponse response = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
