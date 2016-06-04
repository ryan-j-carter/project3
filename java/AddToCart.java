import java.util.HashMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/addtocart"})
public class AddToCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        PrintWriter out = response.getWriter();
        
        String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
        String USER   = "inf124grp01";
        String PASS   = "keCEt5&z";
        
        String id = request.getParameter("id");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        Cart cart;
        cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        cart.addToCart(id);
        session.setAttribute("cart", cart);
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            String sql = "SELECT * FROM product WHERE product_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            
            out.println("<!DOCTYPE html>\n<html>\n");
            request.getRequestDispatcher("/htmlhead?title=Item Added").include(request, response);
            String doc = "<body onload=\"window_onload();\">\n"
                    + "<div id=\"header\">\n"
                    + "<h1 id=\"title\">JavaSipt</h1>\n"
                    + "</div>\n"
                    + "<div id=\"nav\" class=\"nav_rel\">\n"
                    + "<ul>\n"
                    + "<li><a href=\"index.jsp\">Home</a></li><!-- display inline block :(\n"
                    + "--><li><a href=\"/project3/viewcart\">View Cart</a></li>\n"
                    + "</ul>\n</div>\n<div id=\"firstcontent\" class=\"content\">";
            out.println(doc);
            
            out.println("<h2>Item successfully added to cart!</h2>");
            out.println("<table class=\"addtocart\">");
            out.println("<tr><td><img src=\""+rs.getString("image_src")+"\" alt=\""+rs.getString("name")+"\"></td>");
            out.println("<td>"+rs.getString("name")+"</td><td>$"+rs.getString("price")+"</td>");
            out.println("<td><a href=\"/project3/viewcart\" class=\"button\">View Cart</a><br><a href=\"/project3/item?id="+id+"\" class=\"button\">Go Back</a></td></tr>");
            out.println("</table>");
            
            out.println("</div>\n</body>\n</html>");
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
