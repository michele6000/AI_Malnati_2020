package it.polito.ai;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet("/index.html")
public class FirstServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            PrintWriter pw = res.getWriter();
            pw.println("<html><head><link rel=\"stylesheet\" href=\"style.css\"></head><body><h1>Hello Servlet!<h1/>");
            pw.println("<ul>");
            Enumeration<String> names = req.getHeaderNames();
            while (names.hasMoreElements()){
                String name =names.nextElement();
                pw.println("<li>");
                pw.println(name);
                pw.println(req.getHeader(name));
                pw.println("</li>");
            }
            pw.println("</ul>");
            pw.println("<p>"+req.getSession().getId()+"</p>");
            pw.println("</body></html>");
            res.setContentType("text/html");
        }catch (IOException ioe){
            throw new RuntimeException(ioe);
        }
    }
}

//Servlet : oggetti che permettono di trasformare una richiesta in una risposta. Usati come strumenti di backbone
// usati per generare il codice che ci interessa.
// Spring dentro di se non fa nientaltro che adottare un servlet già fatto, e ci costruisce sopra dei pezzi che rendono
// il codice che noi dobbiamo mettere per generare contenuti sensati moooolto semplice :)
