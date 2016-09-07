package com.pla.misc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PingImageServlet extends HttpServlet {

  private static final long serialVersionUID = 1772025897783209959L;
  private final int HEIGHT = 10;
  private final int WIDTH = 10;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String ip = null;
    for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
      String[] values = entry.getValue();
      if (values.length > 0) {
        ip = values[0];
      }
    }
    if (isBlank(ip)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ip address parameter.");
      return;
    }
    response.setContentType("image/png");
    Rectangle rectangle = new Rectangle(0, 0, WIDTH, HEIGHT);
    BufferedImage bufferedImage = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    if (isAlive(ip)) {
      g2d.setColor(Color.GREEN);
    } else {
      g2d.setColor(Color.RED);
    }
    g2d.fill(rectangle);
    ImageIO.write(bufferedImage, "png", response.getOutputStream());
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  private boolean isAlive(String ipAddress) {
    if (isBlank(ipAddress)) {
      return false;
    }
    boolean result = false;
    String cmd = "ping -c 1 -W 1 " + ipAddress;
    BufferedReader bufferedReader = null;
    try {
      Runtime runtime = Runtime.getRuntime();
      Process process = runtime.exec(cmd);
      bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        if (line.indexOf("(0% loss)") != -1 || line.indexOf("successful (100 %)") != -1 || line.indexOf(" 0% packet loss,") != -1) {
          result = true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
        }
      }
    }
    return result;
  }

  private boolean isBlank(String s) {
    return (s == null || s.trim().length() == 0);
  }
}
