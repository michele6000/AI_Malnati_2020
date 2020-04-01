package it.polito.ai;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet("/image")
public class SecondServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedImage bi = new BufferedImage(400,400,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bi.createGraphics();
        g.setPaint(Color.BLUE);
        g.setStroke(new BasicStroke(3.0f));
        g.drawRect(10, 10, 380, 380);
        ImageIO.write(bi,"png", resp.getOutputStream());
        resp.setContentType("image/png");


    }
}

// se invece dobbiamo fornire del contenuto statico lo inseriamo nella cartella webapp