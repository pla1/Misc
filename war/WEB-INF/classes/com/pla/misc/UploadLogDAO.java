package com.pla.misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class UploadLogDAO {
  public static void main(String[] args) {
    UploadLogDAO dao = new UploadLogDAO();
  }

  private Properties properties;

  public UploadLogDAO() {
  }

  public String getUrlForLastUpload() {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Utils.getConnection();
      ps = connection.prepareStatement("select filename from uploadlog order by id desc");
      rs = ps.executeQuery();
      if (rs.next()) {
        String fileName = rs.getString("fileName");
        String urlString = "http://pla1.net/i/" + fileName;
        return Utils.encodeUrlString(urlString);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Utils.close(rs, ps, connection);
    }

    return "";

  }

  public void put(UploadLog uploadLog) {
    Connection connection = null;
    PreparedStatement ps = null;
    try {
      connection = Utils.getConnection();
      ps = connection.prepareStatement("insert into uploadlog (fileName, ipAddress, filelength, logtime) "
          + "values(?,?,?,current_timestamp) ");
      int i = 1;
      ps.setString(i++, uploadLog.getFileName());
      ps.setString(i++, uploadLog.getIpAddress());
      ps.setLong(i++, uploadLog.getFileLength());
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Utils.close(ps, connection);
    }

  }
}
