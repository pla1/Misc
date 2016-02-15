package com.pla.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Utils {
  private static Properties properties;

  static {
    String fileName = "/etc/com.pla.properties";
    System.out.println("Loading properties from file: " + fileName);
    try {
      InputStream input = new FileInputStream(new File(fileName));
      properties = new Properties();
      properties.load(input);
      System.out.println("Properties loaded from file: " + fileName);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Properties not loaded from file: " + fileName + " " + e.getLocalizedMessage());
    }
  }

  public static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static void close(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static void close(PreparedStatement ps, Connection connection) {
    close(ps);
    close(connection);
  }

  public static void close(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public static void close(ResultSet rs, PreparedStatement ps, Connection connection) {
    close(rs);
    close(ps);
    close(connection);
  }

  public static String encodeUrlString(String urlString) {
    try {
      URL url = new URL(urlString);
      URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
          url.getRef());
      return uri.toASCIIString();
    } catch (MalformedURLException | URISyntaxException e) {
      e.printStackTrace();
      return "";
    }
  }

  public static Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "dbuser", properties.getProperty("dbuser.password"));
  }

  public static String getIpAddress() {
    String ipAddress = "";
    try {
      Socket socket = new Socket("google.com", 80);
      InetAddress inetAddress = socket.getLocalAddress();
      if (inetAddress != null) {
        ipAddress = inetAddress.getHostAddress();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ipAddress;
  }

  public static boolean isDevelopmentEnvironment() {
    return getIpAddress().startsWith("192.168.1.");
  }

  public static void main(String[] args) throws Exception {
    Utils.getConnection();
  }

}
