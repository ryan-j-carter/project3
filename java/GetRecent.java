
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/recent"})
public class GetRecent extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        
        String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
        String USER   = "inf124grp01";
        String PASS   = "keCEt5&z";
        
        Connection conn;
        Statement stmt;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            ResultSet rs;
            
            //Output column for Recent
            out.println("<div id=\"rightcol\">");
            out.println("<h2>Recent</h2>");
            out.println("<div class=\"hr_div\"></div>");
            out.println("<table>");
            
            RecentQueue rq;
            rq = (RecentQueue) session.getAttribute("rq");
            if(rq == null)
            {
                rq = new RecentQueue();
                session.setAttribute("rq", rq);
            }
            for(int i = 0; i < rq.getLength(); i++)
            {
                String itemAtIndex = rq.getAtIndex(i);
                if(itemAtIndex == null) break;
                sql = "SELECT * FROM product WHERE product_id=".concat(itemAtIndex).concat(" LIMIT 1");
                rs = stmt.executeQuery(sql);
                rs.next();
                out.println("<tr><td><a href=\"/project3/item?id="+rs.getString("product_id")+"\"><img src=\""+rs.getString("image_src")+"\" alt=\""+rs.getString("name")+"\"/></a>");
                out.println("<figcaption>"+rs.getString("name")+"<br>$"+rs.getString("price")+"</figcaption></td></tr>");
                rs.close();
            }
            
            out.println("</table>");
            out.println("</div>");
            
            stmt.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
