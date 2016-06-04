/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ryan
 */
@WebServlet(urlPatterns = {"/htmlhead"})
public class HtmlHead extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\"/>");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("<link rel=\"stylesheet\" href=\"styles.css\"/>");
        out.println("<link rel=\"SHORTCUT ICON\" href=\"images/favicon.ico\"/>");
        out.println("<link rel=\"icon\" href=\"images/favicon.ico\" type=\"image/ico\"/>");
        out.println("<script src=\"scripts.js\"></script>");
        out.println("<title>JavaSipt - "+request.getParameter("title")+"</title>");
        out.println("</head>");
    }
}
