package org.example.server.persistence.account;

import org.example.server.model.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryAccountRepository implements AccountRepository {

  private final List<Account> accounts = new ArrayList<>();
  private final AtomicInteger idGenerator = new AtomicInteger(1);

  @Override
  public synchronized Account add(String email, String name, String passwordHash, String role, String status) {
    Account a = new Account(idGenerator.getAndIncrement(), email, name, passwordHash, role, status);
    accounts.add(a);
    return a;
  }

  @Override
  public synchronized Optional<Account> findByEmail(String email) {
    for (Account a : accounts) {
      if (a.getEmail().equalsIgnoreCase(email)) return Optional.of(a);
    }
    return Optional.empty();
  }

  @Override
  public synchronized Optional<Account> findById(int id) {
    for (Account a : accounts) {
      if (a.getId() == id) return Optional.of(a);
    }
    return Optional.empty();
  }

  @Override
  public synchronized boolean existsByEmail(String email) {
    return findByEmail(email).isPresent();
  }

  @Override
  public synchronized void update(int id, String name, String email, String passwordHash) {
    for (Account a : accounts) {
      if (a.getId() == id) {
        a.setName(name);
        a.setEmail(email);
        a.setPasswordHash(passwordHash);
        return;
      }
    }
  }

  @Override
  public synchronized void updateStatus(int id, String newStatus) {
    for (Account a : accounts) {
      if (a.getId() == id) {
        a.setStatus(newStatus);
        return;
      }
    }
  }

  @Override
  public synchronized List<Account> findByRoleAndStatus(String role, String status) {
    List<Account> out = new ArrayList<>();
    for (Account a : accounts) {
      if (a.getRole().equals(role) && a.getStatus().equals(status)) out.add(a);
    }
    return out;
  }

  @Override
  public synchronized List<Account> all() {
    return new ArrayList<>(accounts);
  }
}
