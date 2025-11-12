package com.login;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/logindb", "root", "password");
            
            // Prepare SQL statement
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            
            // Execute query
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Valid credentials - Create session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("loggedIn", true);
                
                // Redirect to welcome page
                out.println("<html><body>");
                out.println("<h2>Login Successful!</h2>");
                out.println("<p>Welcome, " + username + "!</p>");
                out.println("<p>Session ID: " + session.getId() + "</p>");
                out.println("<a href='welcome.html'>Go to Welcome Page</a>");
                out.println("</body></html>");
            } else {
                // Invalid credentials
                out.println("<html><body>");
                out.println("<h2>Login Failed!</h2>");
                out.println("<p>Invalid username or password.</p>");
                out.println("<a href='login.html'>Try Again</a>");
                out.println("</body></html>");
            }
            
            // Close connections
            rs.close();
            ps.close();
            con.close();
            
        } catch (ClassNotFoundException e) {
            out.println("<h3>Error: MySQL Driver not found</h3>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<h3>Error: Database connection failed</h3>");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
