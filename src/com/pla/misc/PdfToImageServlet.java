package com.pla.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hometelco.commons.Utilities;

public class PdfToImageServlet extends HttpServlet {
  private static final long serialVersionUID = -8960219753629640363L;
  private final String TAG = this.getClass().getCanonicalName();

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String pdfFile = request.getParameter("pdfFile");
    String resizeTo = request.getParameter("resizeTo");
    System.out.format("PDF file name: %s Resize to: %s - %s\n", pdfFile, resizeTo, TAG);
    if (Utilities.isBlank(pdfFile)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter: pdfFile");
      return;
    }
    File file = new File(pdfFile);
    if (!file.exists()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("File %s not found.", file.getAbsolutePath()));
      return;
    }
    if (!file.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          String.format("File %s does not end with .pdf.", file.getAbsolutePath()));
      return;
    }
    String imageFileName = String.format("/tmp/%s", file.getName().replace(".pdf", ".png"));
    String pageOneFileName = String.format("%s[0]", file.getAbsolutePath());
    if (Utilities.isBlank(resizeTo)) {
      resizeTo = "25%";
    }
    String[] commandParts = { "/usr/bin/convert", "-density", "300", pageOneFileName, "-resize", resizeTo, imageFileName };
    Runtime.getRuntime().exec(commandParts);
    ServletContext servletContext = request.getServletContext();
    String mime = servletContext.getMimeType(imageFileName);
    if (mime == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          String.format("Image mime type is null for image file: %s", imageFileName));
      return;
    }
    response.setContentType(mime);
    File imageFile = new File(imageFileName);
    response.setContentLength((int) imageFile.length());
    FileInputStream in = new FileInputStream(imageFile);
    OutputStream out = response.getOutputStream();
    byte[] buf = new byte[1024];
    int count = 0;
    while ((count = in.read(buf)) >= 0) {
      out.write(buf, 0, count);
    }
    out.close();
    in.close();
  }
}

