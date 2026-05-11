package org.example.shared.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
  private static final long serialVersionUID = 1L;

  public static final String REGISTER = "REGISTER";
  public static final String LOGIN = "LOGIN";
  public static final String UPDATE_ACCOUNT = "UPDATE_ACCOUNT";
  public static final String CREATE_LISTING = "CREATE_LISTING";
  public static final String MY_LISTINGS = "MY_LISTINGS";
  public static final String UPDATE_LISTING_STATUS = "UPDATE_LISTING_STATUS";

  public static final String LIST_PENDING_PROVIDERS = "LIST_PENDING_PROVIDERS";
  public static final String VERIFY_PROVIDER = "VERIFY_PROVIDER";

  public static final String BROWSE_LISTINGS = "BROWSE_LISTINGS";
  public static final String CREATE_RESERVATION = "CREATE_RESERVATION";
  public static final String MY_RESERVATIONS = "MY_RESERVATIONS";              // for customer
  public static final String LIST_PROVIDER_RESERVATIONS = "LIST_PROVIDER_RESERVATIONS"; // for provider
  public static final String UPDATE_RESERVATION_STATUS = "UPDATE_RESERVATION_STATUS";

  private final String type;
  private final Map<String, Object> payload;

  public Request(String type) {
    this.type = type;
    this.payload = new HashMap<>();
  }

  public String getType() {
    return type;
  }

  public Request set(String key, Object value) {
    payload.put(key, value);
    return this;
  }

  public Object get(String key) {
    return payload.get(key);
  }

  public String getString(String key) {
    Object v = payload.get(key);
    return v == null ? null : v.toString();
  }

  public int getInt(String key) {
    Object v = payload.get(key);
    if (v instanceof Integer) return (Integer) v;
    return Integer.parseInt(v.toString());
  }
}
