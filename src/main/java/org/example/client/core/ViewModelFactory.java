package org.example.client.core;

import org.example.client.networking.ServerProxy;
import org.example.client.viewmodel.adminverification.AdminVerificationViewModel;
import org.example.client.viewmodel.browselistings.BrowseListingsViewModel;
import org.example.client.viewmodel.createlisting.CreateListingViewModel;
import org.example.client.viewmodel.login.LoginViewModel;
import org.example.client.viewmodel.main.MainViewModel;
import org.example.client.viewmodel.manageaccount.ManageAccountViewModel;
import org.example.client.viewmodel.mylistings.MyListingsViewModel;
import org.example.client.viewmodel.register.RegisterViewModel;
import org.example.client.viewmodel.reservation.ReservationViewModel;

public class ViewModelFactory {

  private final ServerProxy serverProxy;
  private final Session session;

  public ViewModelFactory(ServerProxy serverProxy, Session session) {
    this.serverProxy = serverProxy;
    this.session = session;
  }

  public LoginViewModel getLoginViewModel() {
    return new LoginViewModel(serverProxy, session);
  }

  public RegisterViewModel getRegisterViewModel() {
    return new RegisterViewModel(serverProxy);
  }

  public MainViewModel getMainViewModel() {
    return new MainViewModel(session);
  }

  public ManageAccountViewModel getManageAccountViewModel() {
    return new ManageAccountViewModel(serverProxy, session);
  }

  public CreateListingViewModel getCreateListingViewModel() {
    return new CreateListingViewModel(serverProxy, session);
  }

  public MyListingsViewModel getMyListingsViewModel() {
    return new MyListingsViewModel(serverProxy, session);
  }

  public AdminVerificationViewModel getAdminVerificationViewModel() {
    return new AdminVerificationViewModel(serverProxy, session);
  }

  public BrowseListingsViewModel getBrowseListingsViewModel() {
    return new BrowseListingsViewModel(serverProxy, session);
  }

  public ReservationViewModel getReservationViewModel() {
    return new ReservationViewModel(serverProxy, session);
  }
}
