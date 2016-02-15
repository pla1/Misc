package com.pla.misc;

public class Attribute {
  private int id;
  private String name;
  private String type;
  private String value;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(id).append(" ");
    sb.append(type).append(" ");
    sb.append(name).append(" ");
    sb.append(value).append(" ");
    return sb.toString();
  }
}
