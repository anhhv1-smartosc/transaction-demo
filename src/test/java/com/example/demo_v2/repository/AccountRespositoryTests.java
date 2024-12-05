package com.example.demo_v2.repository;

import com.example.demo_v2.enity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
public class AccountRespositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setName("John Doe");
        account.setEmail("john.doe@example.com");
        account.setBalance(1000.0);
        account.setCreatedAt(new java.util.Date());
        account = testEntityManager.persistAndFlush(account);
    }

    @Test
    void testFindById() {
        Account foundAccount = accountRepository.findById(account.getId()).orElse(null);
        assertNotNull(foundAccount);
        assertEquals(account.getId(), foundAccount.getId());
    }

    @Test
    void testFindByEmail() {
        Account foundAccount =
                accountRepository.findByEmail(account.getEmail());
        assertNotNull(foundAccount);
        assertEquals(account.getEmail(), foundAccount.getEmail());
    }

    @Test
    void testCreateAccount() {
        Account newAccount = new Account();
        newAccount.setName("Jane Doe");
        newAccount.setEmail("jane.doe@example.com");
        newAccount.setBalance(500.0);
        newAccount.setCreatedAt(new java.util.Date());

        Account savedAccount = accountRepository.save(newAccount);
        assertNotNull(savedAccount.getId());
        assertEquals(newAccount.getName(), savedAccount.getName());
        assertEquals(newAccount.getEmail(), savedAccount.getEmail());
    }

    @Test
    void testDeleteAccount() {
        accountRepository.delete(account);
        Account deletedAccount = accountRepository.findById(account.getId()).orElse(null);
        assertNull(deletedAccount);
    }

    @Test
    void testUpdateAccountBalance() {
        account.setBalance(2000.0);
        Account updatedAccount = accountRepository.save(account);
        assertNotNull(updatedAccount);
        assertEquals(2000.0, updatedAccount.getBalance());
    }
}
