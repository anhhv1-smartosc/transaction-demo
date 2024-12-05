package com.example.demo_v2.service;

import com.example.demo_v2.dto.response.AccountResponse;
import com.example.demo_v2.enity.Account;
import com.example.demo_v2.mapper.AccountMapper;
import com.example.demo_v2.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountResponse accountResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        account.setName("Test1");
        account.setBalance(1000.0);

        accountResponse = new AccountResponse();
        accountResponse.setId(1L);
        accountResponse.setName("Test1");
        accountResponse.setBalance(1000.0);
    }

    @Test
    void testCreateAccount() {
        when(repository.save(account)).thenReturn(account);
        when(mapper.toAccountResponse(account)).thenReturn(accountResponse);

        AccountResponse result = accountService.createAccount(account);

        assertNotNull(result);
        assertEquals("Test1", result.getName());
        assertEquals(1000.0, result.getBalance());
        verify(repository, times(1)).save(account);
        verify(mapper, times(1)).toAccountResponse(account);
    }

    @Test
    void testGetAccountById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(account));
        when(mapper.toAccountResponse(account)).thenReturn(accountResponse);

        AccountResponse result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals("Test1", result.getName());
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toAccountResponse(account);
    }

    @Test
    void testGetAccountById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.getAccountById(1L);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetAllAccounts() {
        when(repository.findAll()).thenReturn(List.of(account));
        when(mapper.toAccountResponse(account)).thenReturn(accountResponse);

        List<AccountResponse> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test1", result.get(0).getName());
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toAccountResponse(account);
    }

    @Test
    void testUpdateAccount() {

        Account updatedAccount = new Account();
        updatedAccount.setId(1L);
        updatedAccount.setName("Updated Account");
        updatedAccount.setBalance(2000.0);

        AccountResponse updatedAccountResponse = new AccountResponse();
        updatedAccountResponse.setId(1L);
        updatedAccountResponse.setName("Updated Account");
        updatedAccountResponse.setBalance(2000.0);

        when(repository.findById(1L)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(updatedAccount);
        when(mapper.toAccountResponse(updatedAccount)).thenReturn(updatedAccountResponse);

        AccountResponse result = accountService.updateAccount(1L, updatedAccount);

        assertNotNull(result);
        assertEquals("Updated Account", result.getName());
        assertEquals(2000.0, result.getBalance());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(account);
        verify(mapper, times(1)).toAccountResponse(updatedAccount);
    }

    @Test
    void testDeleteAccount() {
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(account);
    }

    @Test
    void testDeleteAccount_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountService.deleteAccount(1L);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(repository, times(1)).findById(1L);
    }
}