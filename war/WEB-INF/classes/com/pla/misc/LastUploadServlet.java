package com.pla.misc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LastUploadServlet extends HttpServlet {

  private static final long serialVersionUID = -3371696220039523397L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UploadLogDAO uploadLogDAO = new UploadLogDAO();
    response.sendRedirect(uploadLogDAO.getUrlForLastUpload());
  }
}
