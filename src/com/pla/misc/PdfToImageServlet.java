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

/*-
 * Uses ImageMagick to convert page 1 of a PDF file to a PNG image. 
 * Usage: <img src='/webAppDirectory/PdfToImageServlet?pdfFile=/usr/share/cups/data/default-testpage.pdf'>
 */

public class PdfToImageServlet extends HttpServlet {
  private static final long serialVersionUID = -8960219753629640363L;
  private final String TAG = this.getClass().getCanonicalName();

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String pdfFileName = request.getParameter("pdfFile");
    String resizeTo = request.getParameter("resizeTo");
    System.out.format("PDF file name: %s Resize to: %s - %s\n", pdfFileName, resizeTo, TAG);
    if (pdfFileName == null || pdfFileName.trim().length() == 0) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter: pdfFile");
      return;
    }
    File pdfFile = new File(pdfFileName);
    if (!pdfFile.exists()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format("File %s not found.", pdfFile.getAbsolutePath()));
      return;
    }
    if (!pdfFile.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          String.format("File %s does not end with .pdf.", pdfFile.getAbsolutePath()));
      return;
    }
    String imageFileName = String.format("/tmp/%s_%d.png", pdfFile.getName(), System.currentTimeMillis());
    System.out.format("Temporary image output file: %s. - %s\n", imageFileName, TAG);
    String pageOneFileName = String.format("%s[0]", pdfFile.getAbsolutePath());
    if (resizeTo == null || resizeTo.trim().length() == 0) {
      resizeTo = "25%";
    }
    String[] commandParts = { "/usr/bin/convert", "-density", "300", pageOneFileName, "-resize", resizeTo, imageFileName };
    Process process = Runtime.getRuntime().exec(commandParts);
    try {
      process.waitFor();
    } catch (InterruptedException e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_BAD_REQUEST,
          String.format("ImageMagick convert command failed with exception: %s", e.getLocalizedMessage()));
      return;
    }
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
    System.out.format("%d length for image file: %s - %s\n", imageFile.length(), imageFileName, TAG);
    FileInputStream fileInputStream = new FileInputStream(imageFile);
    OutputStream outputStream = response.getOutputStream();
    byte[] buf = new byte[1024];
    int count = 0;
    while ((count = fileInputStream.read(buf)) >= 0) {
      outputStream.write(buf, 0, count);
    }
    outputStream.flush();
    outputStream.close();
    fileInputStream.close();
  }
}
