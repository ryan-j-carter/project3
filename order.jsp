<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<% Class.forName("com.mysql.jdbc.Driver"); %>

<!DOCTYPE html>
<!--
    CS 137
    Project 3
    Group 1

    order.jsp
-->

<html>
    <jsp:include page="/htmlhead?title=Order" flush="true" />
    <body onload="window_onload();">
        <div id="header">
            <h1 id="title">JavaSipt</h1>
        </div>
        <div id="nav" class="nav_rel">
            <ul>
                <li><a href="index.jsp">Home</a></li><!-- display inline block :(
             --><li><a href="/project3/viewcart">View Cart</a></li>
            </ul>
        </div>
        <div id="firstcontent" class="content">
            <%
                String DB_URL = "jdbc:mysql://sylvester-mccoy-v3.ics.uci.edu/inf124grp01";
                String USER   = "inf124grp01";
                String PASS   = "keCEt5&z";
        
                Connection conn1 = null;
                Connection conn2 = null;
                Statement st1 = null;
                Statement st2 = null;
                
                conn1 = DriverManager.getConnection(DB_URL, USER, PASS);
                conn2 = DriverManager.getConnection(DB_URL, USER, PASS);
            try {
                String sql1 = "SELECT * FROM order_items WHERE order_id='" + request.getParameter("order_id") + "'";
                st1 = conn1.createStatement();
                st2 = conn2.createStatement();
                ResultSet order_list = st1.executeQuery(sql1);
                order_list = st1.executeQuery(sql1);
                int total_price = 0;
            %>
            <h2>Order Details</h2>
            <div class="hr_div"></div>
            
            <table class="order">
                <tr class="first">
                    <th>Product:</th>
                    <th>Quantity:</th>
                    <th>Price:</th>
                </tr>
              <% ResultSet item_info;
                while(order_list.next()) {
                    String sql2 = "SELECT name, price FROM product WHERE product_id='" + order_list.getString("product_id") + "'";
                    item_info = st2.executeQuery(sql2);
                    item_info.next(); 
                    int quantity = Integer.parseInt(order_list.getString("quantity"));
                    int cost = quantity * Integer.parseInt(item_info.getString("price"));
                    total_price += cost;
                    out.println("<tr>");
                    out.println("<td>");
                    out.println(item_info.getString("name"));
                    out.println("</td>");
                    out.println("<td>");
                    out.println(order_list.getString("quantity"));
                    out.println("</td>");
                    out.println("<td>$"+cost+"</td>");
                    out.println("</tr>");
                }
                    
              %>
                <tr class="last">
                    <td colspan="2" class="subtotal">Subtotal: </td>
                    <td>$<%= total_price %></td>
                </tr>
                <% 
                    Thread.sleep(300);
                    sql1 = "SELECT * FROM orders WHERE order_id='" + request.getParameter("order_id") + "'";
                    ResultSet order_info = st1.executeQuery(sql1);
                    order_info.next();
                    Thread.sleep(300);
                %>
                <tr>
                    <th>Order Id:</th>
                    <td class="subtotal">Shipping: </td>
                    <td>$<%= order_info.getString("ship_method") %></td>
                </tr>
                <tr>
                    <td><%= request.getParameter("order_id") %></td>
                    <td class="subtotal"> Total Price: </td>
                    <td>$<%= Integer.parseInt(order_info.getString("ship_method")) + total_price %></td>
                </tr>
                <tr style="height:30px;"></tr>
                <tr class="first">
                    <th>Shipping Information:</th>
                    <th>Billing Information:</th>
                    <th>Card Information:</th>
                </tr>
                <tr>
                    <td>
                        <%= order_info.getString("first_name") %>
                        <%= order_info.getString("last_name") %>
                    </td>
                    <td>
                        <%= order_info.getString("first_name") %>
                        <%= order_info.getString("last_name") %>
                    </td>
                    <td><%= order_info.getString("card_name") %></td>
                </tr>
                <tr>
                    <td><%= order_info.getString("ship_address") %></td>
                    <td><%= order_info.getString("bill_address") %></td>
                    <td><%= "xxxxxxxxxxxx".concat(order_info.getString("card_number").substring(12, 16)) %></td>
                </tr>
                <tr>
                    <td><%= order_info.getString("ship_city").concat(", ") %><%= order_info.getString("ship_state").concat(" ") %><%= order_info.getString("ship_zip") %></td>
                    <td><%= order_info.getString("bill_city").concat(", ") %><%= order_info.getString("bill_state").concat(" ") %><%= order_info.getString("ship_zip") %></td>
                    <td>Exp: <%= order_info.getString("card_expiration_month").concat("/").concat(order_info.getString("card_expiration_year")) %></td>
                </tr>
                <tr class="first">
                    <td><%= order_info.getString("ship_phone") %></td>
                    <td><%= order_info.getString("bill_phone") %></td>
                    <td></td>
                </tr>
                <% order_info.close(); %>
            </table>
            <%
                order_list.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            st1.close();
            conn1.close();

            session.invalidate();
            %>
        </div>
    </body>
</html>
