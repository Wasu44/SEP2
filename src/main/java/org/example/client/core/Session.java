package org.example.client.core;

import org.example.shared.dto.AccountDTO;

public class Session {
  private AccountDTO currentAccount;

  public void setCurrentAccount(AccountDTO account) { this.currentAccount = account; }
  public AccountDTO getCurrentAccount() { return currentAccount; }
  public boolean isLoggedIn() { return currentAccount != null; }
  public void clear() { currentAccount = null; }
}
