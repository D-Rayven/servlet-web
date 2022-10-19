package com.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    
    final String URL = "jdbc:postgresql://localhost:5432/tpuser";
    final String USER = "tpuser";
    final String PASSWORD = "tpuser";
    final String DRIVER = "org.postgresql.Driver";
    Connection conn = null;

    Map<String, Integer> visiters = new HashMap<String, Integer>();

    public void init() {
        try { 
            // Register PostgreSQL Driver 
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM servlet;";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                visiters.putIfAbsent(rs.getString("visiter"), rs.getInt("nb_visit"));
            }

        } 
        catch (ClassNotFoundException | SQLException e) { 
              e.printStackTrace();
              System.exit(1); 
        }
    }
    
    public void createUser(String name, Statement stmt) {
        try {
            String sql = "INSERT INTO servlet (visiter, nb_visit) VALUES ('" + name + "' , 1);";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateUser(Map<String, Integer> visiters, String name, Statement stmt) {
        try {
            String sql = "UPDATE servlet SET nb_visit = nb_visit + 1 WHERE visiter = '" + name + "';";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String name = req.getParameter("name");
            Statement stmt = conn.createStatement();

            PrintWriter out = resp.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>");
            out.println("</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>");
            
            if (req.getParameter("name") == null || req.getParameter("name").isEmpty()) {
                out.println("Bonjour");
            } else {
                if (visiters.containsKey(name)) {
                    updateUser(visiters, name, stmt);
                } else {
                    createUser(name, stmt);
                }
                visiters = increment(visiters, name);
                
                out.println("</h1>");
                out.println("<table border=1><tr>" + "<td><b>Nom du visiteur</b></td>" + "<td><b>Nombre de visites</b></td>" + "</tr>");
                String sql = "SELECT * FROM servlet;";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String visiter = rs.getString("visiter");
                    int nb_visit = rs.getInt("nb_visit");

                    out.println("<tr>" + "<td>" + visiter + "</td>" + "<td>" + nb_visit + "</td>" + "</tr>");
                }
            }
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
