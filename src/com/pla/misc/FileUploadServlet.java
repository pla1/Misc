package com.pla.misc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.pla.misc.AttributeDAO.names;
import com.pla.misc.AttributeDAO.types;

public class FileUploadServlet extends HttpServlet {
  private static final long serialVersionUID = -7792454853508645880L;
  private static int SUCCESS_RESPONSE_CODE = 200;
  private final String TAG = this.getClass().getCanonicalName();

  public void doOptions(HttpServletRequest req, HttpServletResponse resp) {
    resp.setStatus(SUCCESS_RESPONSE_CODE);
    resp.addHeader("Access-Control-Allow-Origin", "http://pla1.net");
    resp.addHeader("Access-Control-Allow-Methods", "POST");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    String ipAddress = request.getRemoteAddr();
    System.out.println(TAG + "request: " + request + " IP: " + ipAddress);
    if (!isMultipart) {
      String message = TAG + "File Not Uploaded. Request is not multi-part form data.";
      System.out.println(message);
      out.write(message);
      out.flush();
      out.close();
      return;
    }
    AttributeDAO attributeDAO = new AttributeDAO();
    if (!attributeDAO.found(types.UPLOAD_IP.name(), names.IP_ADDRESS.name(), ipAddress)) {
      String message = TAG + " Not an authorized IP address. " + ipAddress;
      System.out.println(message);
      out.write(message);
      out.flush();
      out.close();
      return;
    }
    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    List<FileItem> items = null;
    StringBuilder sb = new StringBuilder();
    try {
      items = upload.parseRequest(request);
      sb.append(TAG + "item quantity: " + items.size() + "\n");
    } catch (FileUploadException e) {
      e.printStackTrace();
      out.flush();
      out.close();
      return;
    }
    Iterator<FileItem> itr = items.iterator();
    while (itr.hasNext()) {
      FileItem fileItem = itr.next();
      if (fileItem.isFormField()) {
        String name = fileItem.getFieldName();
        String value = fileItem.getString();
        sb.append(TAG + "name: " + name + " value: " + value + "\n");
      } else {
        try {
          String fileName = fileItem.getName();
          if (fileName == null) {
            sb.append(TAG + " File name is null.\n");
            out.write(sb.toString());
            out.flush();
            out.close();
            return;
          }
          System.out.println(TAG + " fileName: " + fileName + " content type: " + fileItem.getContentType());
          if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".png")
              || "jpg".equals(fileItem.getContentType()) || "png".equals(fileItem.getContentType())) {
            processPhoto(fileName, fileItem, out, ipAddress);
            sb.append(TAG + " Processed file: " + fileName);
          } else {
            sb.append(TAG + " Do not know what to do with " + fileName + "\n");
          }
        } catch (Exception e) {
          e.printStackTrace();
          sb.append(e.getLocalizedMessage() + "\n");
        }
      }
    }
    out.write(sb.toString());
    out.flush();
    out.close();
  }

  private void processPhoto(String fileName, FileItem fileItem, PrintWriter out, String ipAddress) throws Exception {
    File outputFile = new File("/var/www/pla1.net/i/" + fileName);
    String message = "Output file is: " + outputFile.getAbsolutePath();
    System.out.println(message);
    out.write(message);
    out.write("\n");
    fileItem.write(outputFile);
    UploadLog log = new UploadLog();
    log.setFileLength(fileItem.getSize());
    log.setFileName(fileName);
    log.setIpAddress(ipAddress);
    UploadLogDAO uploadLogDAO = new UploadLogDAO();
    uploadLogDAO.put(log);
  }
}