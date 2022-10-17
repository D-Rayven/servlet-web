package com.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    Map<String, Integer> visiters = new HashMap<String, Integer>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");

        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>");
        out.println("</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>");
        if(name == null) {
            out.println("Hello Inconnu");
        } else {
            visiters = increment(visiters, name);
            out.println("Hello " + name);
            out.println("C'est votre " + visiters.get(name) + "e visite !");
        }
        out.println("</h1>");
        out.println("</body>");
        out.println("</html>");
    }

    public static Map<String, Integer> increment(Map<String, Integer> map, String key) {
        map.putIfAbsent(key, 0);
        map.put(key, map.get(key) + 1);

        return map;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
