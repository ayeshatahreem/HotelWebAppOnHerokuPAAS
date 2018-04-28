package Servlet;

import Model.Booking;
import Model.RoomType;
import Model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BookRoom")
public class BookRoom extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int itemId = Integer.valueOf(request.getParameter("itemId"));
        String p = request.getParameter("itemQuantity");
        int q = Integer.valueOf(p);
        if (RoomType.findItemById(itemId).getCount() >= q) {
            Booking order = new Booking();
            order.PlaceBooking(((RoomType) RoomType.findItemById(itemId)), ((User) request.getSession().getAttribute("user")), q);
            request.setAttribute("Availability", "Available");
            RequestDispatcher rd1 = request.getRequestDispatcher("/index.jsp");
            rd1.forward(request, response);
        } else {
            request.setAttribute("Availability", "Not Available");
            RequestDispatcher rd1 = request.getRequestDispatcher("/index.jsp");
            rd1.forward(request, response);
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