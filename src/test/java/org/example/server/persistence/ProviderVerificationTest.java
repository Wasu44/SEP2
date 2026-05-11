package org.example.server.persistence;

import org.example.server.model.account.Account;
import org.example.server.persistence.account.AccountRepository;
import org.example.server.persistence.account.InMemoryAccountRepository;
import org.example.shared.dto.AccountDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderVerificationTest {

  @Test
  void newProvidersComeUpInPendingList() {
    AccountRepository repo = new InMemoryAccountRepository();
    repo.add("p1@x.com", "P1", "h", AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_PENDING);
    repo.add("p2@x.com", "P2", "h", AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_PENDING);
    repo.add("p3@x.com", "P3", "h", AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_ACTIVE);
    repo.add("c1@x.com", "C1", "h", AccountDTO.ROLE_CUSTOMER, AccountDTO.STATUS_PENDING);

    List<Account> pending = repo.findByRoleAndStatus(
        AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_PENDING);
    assertEquals(2, pending.size());
  }

  @Test
  void verifyingFlipsStatusToActive() {
    AccountRepository repo = new InMemoryAccountRepository();
    Account p = repo.add("p@x.com", "P", "h", AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_PENDING);
    repo.updateStatus(p.getId(), AccountDTO.STATUS_ACTIVE);
    assertEquals(AccountDTO.STATUS_ACTIVE, repo.findById(p.getId()).get().getStatus());
    assertTrue(repo.findByRoleAndStatus(AccountDTO.ROLE_PROVIDER, AccountDTO.STATUS_PENDING).isEmpty());
  }
}
