package org.example.server.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class Database {

  private static final String CONFIG_RESOURCE = "/server/config/database.properties";
  private static final String SCHEMA_RESOURCE = "/server/sql/schema.sql";

  private static String url;
  private static String user;
  private static String password;

  static {
    Properties props = new Properties();
    try (InputStream in = Database.class.getResourceAsStream(CONFIG_RESOURCE)) {
      if (in == null) {
        throw new RuntimeException("Missing config: " + CONFIG_RESOURCE);
      }
      props.load(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load " + CONFIG_RESOURCE, e);
    }
    url = props.getProperty("db.url");
    user = props.getProperty("db.user");
    password = props.getProperty("db.password");
  }

  private Database() {}

  public static Connection get() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }

  public static void bootstrapSchema() {
    try (InputStream in = Database.class.getResourceAsStream(SCHEMA_RESOURCE)) {
      if (in == null) throw new RuntimeException("Missing schema: " + SCHEMA_RESOURCE);
      String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
      try (Connection c = get(); Statement st = c.createStatement()) {
        for (String stmt : sql.split(";")) {
          String trimmed = stmt.trim();
          if (!trimmed.isEmpty()) st.execute(trimmed);
        }
      }
      System.out.println("Database schema is ready.");
    } catch (IOException | SQLException e) {
      throw new RuntimeException("Failed to bootstrap schema", e);
    }
  }
}
