import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/item"})
public class Item extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        
        String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
        String USER   = "inf124grp01";
        String PASS   = "keCEt5&z";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            out.println("<!DOCTYPE html>\n<html>\n");
            request.getRequestDispatcher("/htmlhead?title=Item").include(request, response);
            String doc = "<body onload=\"window_onload();\" onbeforeunload=\"call_item_post("+request.getParameter("id")+")\">\n"
                    + "<div id=\"header\">\n"
                    + "<h1 id=\"title\">JavaSipt</h1>\n"
                    + "</div>\n"
                    + "<div id=\"nav\" class=\"nav_rel\">\n"
                    + "<ul>\n"
                    + "<li><a href=\"index.jsp\">Home</a></li><!-- display inline block :(\n"
                    + "--><li><a href=\"/project3/viewcart\">View Cart</a></li>\n"
                    + "</ul>\n</div>\n<div id=\"firstcontent\" class=\"content\">";
            out.println(doc);
            
            String id = request.getParameter("id");
            String sql = "SELECT * FROM product WHERE product_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            
            RecentQueue rq = null;
            rq = (RecentQueue) session.getAttribute("rq");
            if (rq == null) {
                rq = new RecentQueue();
                session.setAttribute("rq", rq);
            }
            rq.add(id);
            session.setAttribute("rq", rq);

            out.println("<div id=\"item_info\">");
            out.println("<h2 id=\"co_item_name\" class=\"co_item_text\">"+rs.getString("name")+"</h2>");
            out.println("<p id=\"co_item_desc\" class=\"co_item_text\">"+rs.getString("description")+"</p>");
            out.println("<h3 id=\"co_item_price\" class=\"co_item_text\">$"+rs.getString("price")+"</h3>");
            out.println("<form action=\"/project3/addtocart?id="+id+"\">");
            out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\"><input id=\"addtocart_btn\" type=\"submit\" value=\"Add to Cart\">");
            out.println("</form>");
            if (getServletContext().getAttribute("viewlist") == null) {
                getServletContext().setAttribute("viewlist", new int[Integer.parseInt(id)+1]);
            }
            int[] view_list = (int[])getServletContext().getAttribute("viewlist");
            if (Integer.parseInt(id)+1 > view_list.length) {
                view_list = Arrays.copyOf(view_list, Integer.parseInt(id)+1);
            }
            view_list[Integer.parseInt(id)] += 1;
            getServletContext().setAttribute("viewlist", view_list);
            out.println("<div class=\"viewers\">");
            out.println("Viewers: " + view_list[Integer.parseInt(id)]);
            out.println("</div>");
            out.println("</div>");
            out.println("<div id=\"item_image\">");
            out.println("<img id=\"co_item_img\" src=\""+rs.getString("image_src")+"\" alt=\""+rs.getString("name")+"\">");
            out.println("</div>");
            
            out.println("</div>\n</body>\n</html>");
            rs.close();
            pstmt.close();
            conn.close();
        }
        catch(Exception e) {
            e.printStackTrace(out);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int index = Integer.parseInt(request.getParameter("id"));
        int[] view_list = (int[])getServletContext().getAttribute("viewlist");
        if (view_list[index] >= 1)
            view_list[index] -= 1;
        getServletContext().setAttribute("viewlist", view_list);
    }
}
