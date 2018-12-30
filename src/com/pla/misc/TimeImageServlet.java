package com.pla.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeImageServlet extends HttpServlet {
  private static final long serialVersionUID = 1838634471441519824L;
  private final int HEIGHT = 1080;
  private final int WIDTH = 1920;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setHeader("Refresh", "60");
    Rectangle rectangle = new Rectangle(0, 0, WIDTH, HEIGHT);
    BufferedImage bufferedImage = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    g2d.setBackground(Color.WHITE);
    g2d.fill(rectangle);
    g2d.setColor(Color.BLACK);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Font font = new Font("Arial", Font.BOLD, 120);
    g2d.setFont(font);
    AffineTransform affinetransform = new AffineTransform();
    FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEE MMMMM dd, yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a z");
    String dateString = dateFormat.format(date);
    String timeString = timeFormat.format(date);
    int textheight = (int) (font.getStringBounds(dateString, frc).getHeight());
    int textwidth = (int) (font.getStringBounds(dateString, frc).getWidth());
    g2d.drawString(dateString, (WIDTH - textwidth) / 2, (HEIGHT - textheight) / 2);
    textwidth = (int) (font.getStringBounds(timeString, frc).getWidth());
    g2d.drawString(timeString, (WIDTH - textwidth) / 2, ((HEIGHT - textheight) / 2) + (int) (textheight * 1.2));
    response.setContentType("image/png");
    ImageIO.write(bufferedImage, "png", response.getOutputStream());
  }
}
