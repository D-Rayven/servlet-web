<%@ page import="java.sql.*" %>
<jsp:useBean id="calc" class="com.servlet.Calculator" />
<html>
    <body>
        <h1>Page depuis le fichier JSP</h1>

        <%
            final String URL = "jdbc:postgresql://localhost:5432/tpuser";
            final String USER = "tpuser";
            final String PASSWORD = "tpuser";
            final String DRIVER = "org.postgresql.Driver";
            Connection conn = null;
            {
                try {
                    try {
                        Class.forName(DRIVER);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    conn = DriverManager.getConnection(URL, USER, PASSWORD);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            String name = request.getParameter("name");
            
            if (name != null && name != "") {
                // Compteur de visite
                int counter = 0;
                try {
                    PreparedStatement stmt;
                    assert conn != null;
                    stmt = conn.prepareStatement("SELECT nb_visit FROM servlet WHERE visiter = ?");
                    stmt.setString(1, name);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        counter = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                counter++;

                try {
                    PreparedStatement stmt;
                    stmt = conn.prepareStatement(
                            "INSERT INTO servlet (visiter, nb_visit) VALUES(?, ?) " +
                                    "ON CONFLICT (visiter) DO UPDATE SET nb_visit = ? WHERE servlet.visiter = ?"
                    );
                    stmt.setString(1, name);
                    stmt.setInt(2, counter);
                    stmt.setInt(3, counter);
                    stmt.setString(4, name);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                out.println("Bonjour " + name + " c'est votre visite numero " + counter + " !");

                session = request.getSession(true);
                String sessionName = (String) session.getAttribute("sessionName");
                if (sessionName == null) {
                    out.println("Le visiteur avant nous ne s'est pas identifie !");
                } else {
                    out.println("Le dernier visiteur avant vous : " + sessionName);
                }
                session.setAttribute("sessionName", name);
            } else {
                out.println("Bonjour inconnu");
            }
            int res_somme = calc.somme(1,3);
            int res_multiplier = calc.multiplier(12,4);
            double res_division = calc.division(8,4);
            double res_puissance = calc.puissance(3,4);
            out.println("1 + 3 = " + res_somme);
            out.println("12 * 4 = " + res_multiplier);
            out.println("8 * 4 = " + res_division);
            out.println("3 * 4 = " + res_puissance);
        %>
    </body>
</html>