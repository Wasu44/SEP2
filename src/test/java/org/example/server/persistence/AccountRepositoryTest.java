package org.example.server.persistence;

import org.example.server.model.account.Account;
import org.example.server.persistence.account.AccountRepository;
import org.example.server.persistence.account.InMemoryAccountRepository;
import org.example.shared.dto.AccountDTO;
import org.example.shared.util.PasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {

  @Test
  void addsAndFindsByEmail() {
    AccountRepository repo = new InMemoryAccountRepository();
    Account a = repo.add("a@b.com", "Alice", PasswordHasher.hash("secret1"),
        AccountDTO.ROLE_CUSTOMER, AccountDTO.STATUS_ACTIVE);
    assertTrue(repo.findByEmail("a@b.com").isPresent());
    assertEquals(a.getId(), repo.findByEmail("a@b.com").get().getId());
  }

  @Test
  void emailLookupIsCaseInsensitive() {
    AccountRepository repo = new InMemoryAccountRepository();
    repo.add("user@x.com", "U", "h", AccountDTO.ROLE_CUSTOMER, AccountDTO.STATUS_ACTIVE);
    assertTrue(repo.findByEmail("USER@x.com").isPresent());
  }

  @Test
  void existsByEmailReportsCorrectly() {
    AccountRepository repo = new InMemoryAccountRepository();
    assertFalse(repo.existsByEmail("nobody@x.com"));
    repo.add("nobody@x.com", "N", "h", AccountDTO.ROLE_CUSTOMER, AccountDTO.STATUS_ACTIVE);
    assertTrue(repo.existsByEmail("nobody@x.com"));
  }

  @Test
  void passwordHasherIsDeterministicAndDifferentForDifferentInputs() {
    assertEquals(PasswordHasher.hash("hello"), PasswordHasher.hash("hello"));
    assertNotEquals(PasswordHasher.hash("hello"), PasswordHasher.hash("world"));
  }
}
