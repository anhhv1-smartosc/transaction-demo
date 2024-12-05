package com.example.demo_v2.service;

import com.example.demo_v2.dto.response.AccountResponse;
import com.example.demo_v2.enity.Account;
import com.example.demo_v2.mapper.AccountMapper;
import com.example.demo_v2.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    public AccountResponse createAccount(Account account) {
        return mapper.toAccountResponse(repository.save(account));
    }

    public AccountResponse getAccountById(Long id) {
        Optional<Account> account = repository.findById(id);
        return account.map(a -> mapper.toAccountResponse(a))
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = repository.findAll();
        List<AccountResponse> accountResponses = new ArrayList<>();

        for (Account account : accounts) {
            AccountResponse response = mapper.toAccountResponse(account);
            accountResponses.add(response);
        }

        return accountResponses;
    }

    public AccountResponse updateAccount(Long id, Account accountDetails) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));


        account.setName(accountDetails.getName());
        account.setBalance(accountDetails.getBalance());

        return mapper.toAccountResponse(repository.save(account));
    }

    public void deleteAccount(Long id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        repository.delete(account);
    }
}
