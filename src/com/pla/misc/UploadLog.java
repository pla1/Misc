package com.pla.misc;

import java.sql.Timestamp;

public class UploadLog {
  private long fileLength;
  private String fileName;
  private int id;
  private String ipAddress;
  private Timestamp logTime;
  private String logTimeDisplay;
  private long logTimeMilliseconds;

  public long getFileLength() {
    return fileLength;
  }

  public String getFileName() {
    return fileName;
  }

  public int getId() {
    return id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public Timestamp getLogTime() {
    return logTime;
  }

  public String getLogTimeDisplay() {
    return logTimeDisplay;
  }

  public long getLogTimeMilliseconds() {
    return logTimeMilliseconds;
  }

  public void setFileLength(long fileLength) {
    this.fileLength = fileLength;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public void setLogTime(Timestamp logTime) {
    this.logTime = logTime;
  }

  public void setLogTimeDisplay(String logTimeDisplay) {
    this.logTimeDisplay = logTimeDisplay;
  }

  public void setLogTimeMilliseconds(long logTimeMilliseconds) {
    this.logTimeMilliseconds = logTimeMilliseconds;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(id).append(" ");
    sb.append(ipAddress).append(" ");
    sb.append(fileName).append(" ");
    sb.append(fileLength).append(" ");
    sb.append(logTimeDisplay).append(" ");
    return sb.toString();
  }

}
