import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/products"})
public class GetProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
        String USER   = "inf124grp01";
        String PASS   = "keCEt5&z";
        
        Connection conn = null;
        Statement stmt = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            
            //Output column for Gear
            out.println("<div id=\"leftcol\">");
            out.println("<h2>Gear</h2>");
            out.println("<div class=\"hr_div\"></div>");
            out.println("<table>");
            
            String sql = "SELECT * FROM product WHERE type='gear'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                out.println("<tr><td><a href=\"/project3/item?id="+rs.getString("product_id")+"\"><img src=\""+rs.getString("image_src")+"\" alt=\""+rs.getString("name")+"\"/></a>");
                out.println("<figcaption>"+rs.getString("name")+"<br>$"+rs.getString("price")+"</figcaption></td></tr>");
            }
            out.println("</table>");
            out.println("</div>");
            
            //Output column for Beans
            out.println("<div id=\"midcol\">");
            out.println("<h2>Beans</h2>");
            out.println("<div class=\"hr_div\"></div>");
            out.println("<table>");
            
            sql = "SELECT * FROM product WHERE type='beans'";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                out.println("<tr><td><a href=\"/project3/item?id="+rs.getString("product_id")+"\"><img src=\""+rs.getString("image_src")+"\" alt=\""+rs.getString("name")+"\"/></a>");
                out.println("<figcaption>"+rs.getString("name")+"<br>$"+rs.getString("price")+"</figcaption></td></tr>");
            }
            out.println("</table>");
            out.println("</div>");
            
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
