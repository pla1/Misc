package com.pla.misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AttributeDAO {
  public enum names {
    IP_ADDRESS
  }

  public enum types {
    UPLOAD_IP
  }

  public static void main(String[] args) {
    AttributeDAO dao = new AttributeDAO();
    ArrayList<Attribute> arrayList = dao.get(types.UPLOAD_IP.name(), names.IP_ADDRESS.name());
    for (Attribute a : arrayList) {
      System.out.println(a);
    }
  }

  public AttributeDAO() {
  }

  public boolean found(String type, String name, String value) {
    ArrayList<Attribute> attributes = get(type, name);
    for (Attribute attribute : attributes) {
      if (value.equals(attribute.getValue())) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<Attribute> get(String type, String name) {
    ArrayList<Attribute> arrayList = new ArrayList<Attribute>();
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Utils.getConnection();
      ps = connection.prepareStatement("select * from attribute where atype = ? and aname = ?");
      ps.setString(1, type);
      ps.setString(2, name);
      rs = ps.executeQuery();
      while (rs.next()) {
        Attribute a = new Attribute();
        a.setId(rs.getInt("id"));
        a.setName(rs.getString("aname"));
        a.setValue(rs.getString("avalue"));
        a.setType(rs.getString("atype"));
        arrayList.add(a);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Utils.close(rs, ps, connection);
    }

    return arrayList;

  }
}
