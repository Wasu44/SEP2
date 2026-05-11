package org.example.client.core;

import org.example.client.networking.ServerProxy;

public class ClientFactory {
  private final ServerProxy serverProxy;
  private final Session session;
  private final ViewModelFactory viewModelFactory;
  private final ViewHandler viewHandler;

  public ClientFactory() {
    this.serverProxy = new ServerProxy("localhost", 2910);
    this.session = new Session();
    this.viewModelFactory = new ViewModelFactory(serverProxy, session);
    this.viewHandler = new ViewHandler(viewModelFactory, session);
  }

  public ViewHandler getViewHandler() { return viewHandler; }
  public ViewModelFactory getViewModelFactory() { return viewModelFactory; }
  public Session getSession() { return session; }
  public ServerProxy getServerProxy() { return serverProxy; }
}
