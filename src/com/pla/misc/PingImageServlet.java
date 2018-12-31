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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = getIpFromRequest(request);
        if (isBlank(ip)) {
            String message = String.format("Missing parameter: IP address or host name. Examples: https://pingimage.net?q=8.8.8.8 OR https://pingimage.net/8.8.8.8 You provided: Query string: \"%s\" Path info: \"%s\"",
                    request.getQueryString(), request.getPathInfo());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
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

    private String getIpFromRequest(HttpServletRequest request) {
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String[] values = entry.getValue();
            if (values.length > 0) {
                return values[0];
            }
        }
        String path = request.getPathInfo();
        if (!isBlank(path)) {
            String[] words = path.split("/");
            if (words.length > 1) {
                return words[1];
            } else {
                return path;
            }
        }
        return null;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean isAlive(String ipAddress) {
        boolean result = false;
        String cmd = String.format("ping -c 1 -W 1 %s", ipAddress);
        BufferedReader bufferedReader = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("(0% loss)")
                        || line.contains("successful (100 %)")
                        || line.contains(" 0% packet loss,")) {
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
