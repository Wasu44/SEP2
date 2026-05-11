module FoodWasteRedistribution {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.base;
  requires java.sql;
  requires org.postgresql.jdbc;

  opens org.example.client.view.login to javafx.fxml;
  opens org.example.client.view.register to javafx.fxml;
  opens org.example.client.view.main to javafx.fxml;
  opens org.example.client.view.adminverification to javafx.fxml;
  opens org.example.client.view.browselistings to javafx.fxml;
  opens org.example.client.view.createlisting to javafx.fxml;
  opens org.example.client.view.manageaccount to javafx.fxml;
  opens org.example.client.view.mylistings to javafx.fxml;
  opens org.example.client.view.reservation to javafx.fxml;

  exports org.example.client.core;
  exports org.example.shared.dto;
  exports org.example.shared.protocol;
  exports org.example.shared.util;
  exports org.example.server.core;
  exports org.example.server.model.account;
  exports org.example.server.model.listing;
  exports org.example.server.persistence.account;
  exports org.example.server.persistence.listing;
  exports org.example.server.persistence.reservation;
  exports org.example.server.model.reservation;
  exports org.example.server.database;
}
