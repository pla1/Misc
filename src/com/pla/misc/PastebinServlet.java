package com.pla.misc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PastebinServlet extends HttpServlet {

  private static final long serialVersionUID = -6036966406664707905L;
  private String text;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");
    PrintWriter pw = response.getWriter();
    if (text != null) {
      pw.write(text);
    }
    pw.flush();
    pw.close();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    text = request.getParameter("text");
    if (text == null || text.length() == 0 || text.length() > Integer.MAX_VALUE) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter: text should be between 1 and " + Integer.MAX_VALUE
          + " in length.");
    } else {
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }
  }
}
