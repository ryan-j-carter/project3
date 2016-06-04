import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/checkout"})
public class Checkout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        PrintWriter out = response.getWriter();
        
        String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
        String USER   = "inf124grp01";
        String PASS   = "keCEt5&z";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        Cart cart;
        cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        
        Integer order_id;
        order_id = (Integer) session.getAttribute("order_id");
        if (order_id == null) {
            Random r = new Random();
            order_id = r.nextInt(1000000000);
        }
        session.setAttribute("order_id", order_id);
        
        int subtotal;
        int total = 0;
        
        try {
            out.println("<!DOCTYPE html>\n<html>\n");
            request.getRequestDispatcher("/htmlhead?title=Checkout").include(request, response);
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
            
            out.println("<form name=\"checkout_form\" id=\"checkout_form\" onreset=\"form_reset();\">");
            out.println("<div class=\"checkout_row\">");
            out.println("<h3>Shipping Information:</h3>");
            out.println("<div class=\"checkout_col\"><p>First Name:</p><input type=\"text\" name=\"fname\" tabindex=\"1\" required></div>");
            out.println("<div class=\"checkout_col\"><p>Last Name:</p><input type=\"text\" name=\"lname\" tabindex=\"2\" required></div>");
            out.println("</div>");
            out.println("<div class=\"checkout_row last\">");
            out.println("<div class=\"checkout_col\"><p>Address:</p><input type=\"text\" name=\"addr_s\" tabindex=\"3\" required></div>");
            out.println("<div class=\"checkout_col\"><p>City:</p><input type=\"text\" name=\"city_s\" tabindex=\"4\" required></div>");
            out.println("<div class=\"checkout_col\"><p>State:</p><input type=\"text\" name=\"state_s\" tabindex=\"5\" size=\"2\" maxlength=\"2\" required></div>");
            out.println("<div class=\"checkout_col\"><p>Zip Code:</p><input type=\"text\" name=\"zip_s\" tabindex=\"6\" size=\"5\" maxlength=\"5\" required></div>");
            out.println("<div class=\"checkout_col\"><p>Phone Number:</p><input type=\"text\" name=\"phone_s\" tabindex=\"7\" size=\"10\" maxlength=\"10\" required></div>");
            out.println("</div>");
            out.println("<div class=\"checkout_row\">");
            out.println("<h3>Billing Information:</h3></div>");
            out.println("<div class=\"checkout_row\">");
            out.println("<div class=\"checkout_col\"><input type=\"checkbox\" name=\"sameship\" onchange=\"set_shipping();\" tabindex=\"8\">Check if billing address is same as above.<br></div>");
            out.println("</div>");
            out.println("<div class=\"checkout_row last\">");
            out.println("<div class=\"checkout_col\"><p>Address:</p><input type=\"text\" name=\"addr_b\" tabindex=\"9\" required></div>");
            out.println("<div class=\"checkout_col\"><p>City:</p><input type=\"text\" name=\"city_b\" tabindex=\"10\" required></div>");
            out.println("<div class=\"checkout_col\"><p>State:</p><input type=\"text\" name=\"state_b\" tabindex=\"11\" size=\"2\" maxlength=\"2\" required></div>");
            out.println("<div class=\"checkout_col\"><p>Zip Code:</p><input type=\"text\" name=\"zip_b\" tabindex=\"12\" size=\"5\" maxlength=\"5\" required></div>");
            out.println("<div class=\"checkout_col\"><p>Phone Number:</p><input type=\"text\" name=\"phone_b\" tabindex=\"13\" size=\"10\" maxlength=\"10\" required></div>");
            out.println("</div>");
            out.println("<div class=\"checkout_row\">");
            out.println("<h3>Card Information:</h3>");
            out.println("<div class=\"checkout_col\"><p>Name on Card:</p><input type=\"text\" name=\"nameoncard\" tabindex=\"14\" required><br>");
            out.println("<p>Card Number:</p><input type=\"text\" name=\"cardnumber\" maxlength=\"16\" tabindex=\"15\" required></div>");
            out.println("<div class=\"checkout_col\">");
            out.println("<p>Expiration Date:</p>\n" +
                        "<select name=\"month\" size=\"1\" tabindex=\"16\" required>\n" +
                        "<option value=\"0\">Month</option>\n" +
                        "<option value=\"1\">1</option>\n" +
                        "<option value=\"2\">2</option>\n" +
                        "<option value=\"3\">3</option>\n" +
                        "<option value=\"4\">4</option>\n" +
                        "<option value=\"5\">5</option>\n" +
                        "<option value=\"6\">6</option>\n" +
                        "<option value=\"7\">7</option>\n" +
                        "<option value=\"8\">8</option>\n" +
                        "<option value=\"9\">9</option>\n" +
                        "<option value=\"10\">10</option>\n" +
                        "<option value=\"11\">11</option>\n" +
                        "<option value=\"12\">12</option>\n" +
                        "</select>\n" +
                        "<select name=\"year\" size=\"1\" tabindex=\"17\" required>\n" +
                        "<option value=\"0\">Year</option>\n" +
                        "<option value=\"2016\">2016</option>\n" +
                        "<option value=\"2017\">2017</option>\n" +
                        "<option value=\"2018\">2018</option>\n" +
                        "<option value=\"2019\">2019</option>\n" +
                        "<option value=\"2020\">2020</option>\n" +
                        "<option value=\"2021\">2021</option>\n" +
                        "<option value=\"2022\">2022</option>\n" +
                        "<option value=\"2023\">2023</option>\n" +
                        "<option value=\"2024\">2024</option>\n" +
                        "<option value=\"2025\">2025</option>\n" +
                        "<option value=\"2026\">2026</option>\n" +
                        "<option value=\"2027\">2027</option>\n" +
                        "</select>");
            out.println("<p>Security Code:</p><input type=\"text\" name=\"securitycode\" size=\"3\" maxlength=\"3\" tabindex=\"18\" required>");
            out.println("</div>");
            out.println("<div class=\"checkout_col\"><p>Payment Method:</p>");
            out.println("<input type=\"radio\" name=\"payment\" value=\"visa\" id=\"def_payment\" tabindex=\"`9\" checked>Visa<br>");
            out.println("<input type=\"radio\" name=\"payment\" value=\"master\">MasterCard<br>");
            out.println("<input type=\"radio\" name=\"payment\" value=\"discover\">Discover<br>");
            out.println("<input type=\"radio\" name=\"payment\" value=\"amex\">American Express");
            out.println("</div>");
            out.println("<div class=\"checkout_col\"><p>Shipping Method:</p>\n" +
                        "<select name=\"shipping_method\" size=\"1\" tabindex=\"20\" required>\n" +
                        "<option value=\"3\">6-Days Ground - $3</option>\n" +
                        "<option value=\"6\">2-Days Expedited - $6</option>\n" +
                        "<option value=\"10\">Overnight - $10</option>\n" +
                        "</select></div>");
            out.println("</div>");  //End checkout_row
            out.println("<div class=\"checkout_row last\"><input type=\"reset\" value=\"Reset\" tabindex=\"21\">\n" +
                        "<input type=\"button\" value=\"Submit\" onclick=\"return form_validate("+order_id+");\" tabindex=\"22\"></div>");
            out.println("<div style=\"clear:both;\"></div>");
            out.println("</form>");
            
            out.println("<table class=\"cart\">");
            out.println("<tr id=\"cart_first\"><th colspan=\"2\">Review Items:</th><td>Quantity:</td><td>Subtotal:</td></tr>");
            
            if (cart.getCart().size() != 0) {
                Class.forName("com.mysql.jdbc.Driver");

                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                String ids = "";
                String sep = "";
                for (Map.Entry<String, Integer> item : cart.getCart()) {
                    ids += sep + item.getKey();
                    sep = ", ";
                }
                String sql = "SELECT * FROM product WHERE product_id IN (" + ids + ")";
                pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    out.println("<tr><td><img src=\""+rs.getString("image_src")+"\" alt=\""+rs.getString("name")+"\"></td><td>"+rs.getString("name")+"</td>");
                    int quantity = cart.getQuantity(rs.getString("product_id"));
                    out.println("<td>"+quantity+"</td>");
                    subtotal = rs.getInt("price") * quantity;
                    total += subtotal;
                    out.println("<td>$"+subtotal+"</td></tr>");
                }
                rs.close();
            }
            out.println("</table>");
            out.println("<div id=\"total\">$" + total + "</div>");
            
            out.println("</div>\n</body>\n</html>");
            pstmt.close();
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
        String USER   = "inf124grp01";
        String PASS   = "keCEt5&z";
        
        Integer order_id;
        order_id = (Integer) session.getAttribute("order_id");
        if (order_id == null) {
            Random r = new Random();
            order_id = r.nextInt(1000000000);
        }
        session.setAttribute("order_id", order_id);
        Connection conn1 = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        String sql1 = "INSERT INTO orders"
                + "(first_name, last_name, ship_address, ship_city, ship_state, ship_zip, ship_phone, bill_address, bill_city, bill_state, bill_zip, bill_phone, payment_method, card_number, card_name, card_expiration_month, card_expiration_year, card_security, ship_method, order_id)"
                + " VALUES('"
                + request.getParameter("first_name")            + "', '"
                + request.getParameter("last_name")             + "', '"
                + request.getParameter("ship_address")          + "', '"
                + request.getParameter("ship_city")             + "', '"
                + request.getParameter("ship_state")            + "', '"
                + request.getParameter("ship_zip")              + "', '"
                + request.getParameter("ship_phone")            + "', '"
                + request.getParameter("bill_address")          + "', '"
                + request.getParameter("bill_city")             + "', '"
                + request.getParameter("bill_state")            + "', '"
                + request.getParameter("bill_zip")              + "', '"
                + request.getParameter("bill_phone")            + "', '"
                + request.getParameter("payment_method")        + "', '"
                + request.getParameter("card_number")           + "', '"
                + request.getParameter("card_name")             + "', '"
                + request.getParameter("card_expiration_month") + "', '"
                + request.getParameter("card_expiration_year")  + "', '"
                + request.getParameter("card_security")         + "', '"
                + request.getParameter("ship_method")           + "', '"
                + order_id+"')";
        String sql2;
        Cart cart;
        cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        try {
            conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
            
            //Insert cart details into order_items
            sql2 = "INSERT INTO order_items(order_id, product_id, quantity) VALUES";
            String sep = "";
            for (Map.Entry<String, Integer> item : cart.getCart()) {
                sql2 += sep + "('"
                        +order_id + "', '"
                        +item.getKey() + "', '"
                        +item.getValue()
                        +"')";
                sep = ", ";
            }
            pstmt2 = conn1.prepareStatement(sql2);
            pstmt2.executeUpdate();
            
            //Insert user details into orders
            pstmt1 = conn1.prepareStatement(sql1);
            pstmt1.executeUpdate();
            
            pstmt1.close();
            pstmt2.close();
            conn1.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        cart.clearCart();
        session.setAttribute("cart", cart);
        return;
    }
}
