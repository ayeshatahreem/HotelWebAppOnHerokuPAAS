package Servlet;

import Model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class LogIn extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            User u = new User();
            u.setEmail(request.getParameter("email"));
            u.setPassword(request.getParameter("password"));
            if (User.LoginUser(request.getParameter("email"), request.getParameter("password"))) {
                User us = new User();
                us.setEmail(String.valueOf(request.getParameter("email")));
                us.setPassword(String.valueOf(request.getParameter("password")));
                us.GetUser();
                HttpSession sessionUser = request.getSession();
                sessionUser.setAttribute("user", User.findUserById(us.getUserId()));
                us.loadOrdersFromDb();
                RequestDispatcher rd1 = request.getRequestDispatcher("/index.jsp");
                rd1.forward(request, response);
            } else {
                RequestDispatcher rd1 = request.getRequestDispatcher("/Error.jsp");
                rd1.forward(request, response);
            }
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}