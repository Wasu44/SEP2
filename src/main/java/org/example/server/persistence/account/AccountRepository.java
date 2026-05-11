package org.example.server.persistence.account;

import org.example.server.model.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

  Account add(String email, String name, String passwordHash, String role, String status);

  Optional<Account> findByEmail(String email);

  Optional<Account> findById(int id);

  boolean existsByEmail(String email);

  void update(int id, String name, String email, String passwordHash);

  void updateStatus(int id, String newStatus);

  List<Account> findByRoleAndStatus(String role, String status);

  List<Account> all();
}
