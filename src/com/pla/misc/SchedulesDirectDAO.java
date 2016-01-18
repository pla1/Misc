package com.pla.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SchedulesDirectDAO {
  private final static String username = "htplainf";
  private final static String password = "SUPERSECRETPASSWORD";
  private final static String BASE_URL = "https://json.schedulesdirect.org/20140530/";
  private static String token;
  private final static String postalCode = "29405";
  private static final String jsonTest = "{ \"60030\": { \"type\": \"Over-the-Air\", \"location\": \"60030\", \"lineups\": [ { \"name\": \"Antenna\", \"uri\": \"/20131021/lineups/USA-OTA-60030\" } ] }, \"4DTV\": { \"type\": \"Satellite\", \"location\": \"USA\", \"lineups\": [ { \"name\": \"4DTV\", \"uri\": \"/20131021/lineups/USA-4DTV-DEFAULT\" } ] } }";

  public static void main(String[] args) throws Exception {
    SchedulesDirectDAO dao = new SchedulesDirectDAO();
    if (true) {
      Pattern pattern = Pattern.compile(" \"([0-9a-zA-Z]+)\": \\{");
      Matcher matcher = pattern.matcher(jsonTest);
      while (matcher.find()) {
        System.out.println("FOUND: " + matcher.group(1));
      }
      System.out.println(jsonTest);
      System.exit(0);
    }
    if (false) {
      dao.setToken();
      dao.checkStatus();
      dao.getHeadends();
      System.out.println(token);
    }
  }

  public SchedulesDirectDAO() {
  }

  private void getHeadends() throws NoSuchAlgorithmException, IllegalStateException, IOException {
    HttpGet request = new HttpGet(BASE_URL + "headends?country=USA&postalcode=" + postalCode);
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-type", "application/json");
    request.setHeader("User-Agent", "Patrick.Archibald@gmail.com");
    request.setHeader("token", token);
    HttpResponse response = new DefaultHttpClient().execute(request);
    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
    JSONObject json = (JSONObject) JSONValue.parse(reader);
    System.out.println(json.toJSONString());
  }

  private void checkStatus() throws NoSuchAlgorithmException, IllegalStateException, IOException {
    HttpGet request = new HttpGet(BASE_URL + "status");
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-type", "application/json");
    request.setHeader("User-Agent", "Patrick.Archibald@gmail.com");
    request.setHeader("token", token);
    HttpResponse response = new DefaultHttpClient().execute(request);
    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
    JSONObject json = (JSONObject) JSONValue.parse(reader);
    System.out.println(json.toJSONString());
    JSONArray jsonArray = (JSONArray) json.get("systemStatus");
    JSONObject systemStatus = (JSONObject) jsonArray.get(0);
    String status = (String) systemStatus.get("status");
    System.out.println("System status: " + status);
    if (!"Online".equals(status)) {
      throw new IOException("Status is not Online. Stop accessing the server. Try again later.");
    }

  }

  private void setToken() throws NoSuchAlgorithmException, IllegalStateException, IOException {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    digest.update(password.getBytes("utf8"));
    byte[] digestBytes = digest.digest();
    String digestStr = javax.xml.bind.DatatypeConverter.printHexBinary(digestBytes);
    digestStr = digestStr.toLowerCase();
    HttpPost request = new HttpPost(BASE_URL + "token");
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-type", "application/json");
    request.setHeader("User-Agent", "Patrick.Archibald@gmail.com");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("username", username);
    jsonObject.put("password", digestStr);
    StringEntity entity = new StringEntity(jsonObject.toString());
    request.setEntity(entity);
    HttpResponse response = new DefaultHttpClient().execute(request);
    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
    JSONObject json = (JSONObject) JSONValue.parse(reader);
    System.out.println("JSON RESULT: " + json.toJSONString());
    token = (String) json.get("token");
  }
}
