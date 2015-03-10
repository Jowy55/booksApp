import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class bookList extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String url="jdbc:odbc:books";
            connection=DriverManager.getConnection(url); 
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
        res.setContentType("text/html");
        PrintWriter toClient = res.getWriter();
        toClient.println("<!DOCTYPE HTML>");
        toClient.println("<html>");
        toClient.println("<head><title>Books</title></head>");
        toClient.println("<body>");
        toClient.println("<a href=\"index.html\">Home</A>");
        toClient.println("<h2>List of books</h2>");
        
        HttpSession session = req.getSession(false);
        if (session != null) {
            String name = (String)session.getAttribute("name");
            if (name != null) {
                toClient.println("<h2>name: " + name + "</h2>");
            }
        }

        toClient.print("<form action=\"bookOpinion\" method=GET>");
        toClient.println("<table border='1'>");
        
        String sql = "Select code, title, author FROM books";
        System.out.println(sql);
        try {
            Statement statement=connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()) {
                toClient.println("<tr>");
                String codeStr = result.getString("code");
                toClient.println("<td><input type=\"radio\" name=\"book" +
                    "\" value=\"" + codeStr + "\"></td>");
                toClient.println("<td>" + codeStr + "</td>");
                toClient.println("<td>" + result.getString("title") + "</td>");
                toClient.println("<td>" + result.getString("author") + "</td>");
                toClient.println("</tr>");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Resulset: " + sql + " Exception: " + e);
        }
        toClient.println("</table>");
        toClient.println("<textarea rows=\"8\" cols=\"60\" name=\"comment\"></textarea><BR>");
        toClient.println("<input type=submit>");
        toClient.println("</form>");
        toClient.println("</body>");
        toClient.println("</html>");
        toClient.close();
    }
}