package com.pla.misc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyIpServlet extends HttpServlet {

  private static final long serialVersionUID = -1516660602245791944L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");
    PrintWriter pw = response.getWriter();
    pw.write(request.getRemoteAddr());
    pw.flush();
    pw.close();
  }
}
