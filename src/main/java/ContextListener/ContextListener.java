package ContextListener;

import Database.DBConnection;
import Model.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebListener
public class ContextListener implements ServletContextListener {
    public void contextDestroyed(ServletContextEvent arg0) {
        Connection con = DBConnection.con;
        try {
            PreparedStatement ps = con.prepareCall("UPDATE db.config SET roomsId=?, bookingsId = ?, reviewsId=?, usersId=? WHERE id=1 ");
            ps.setInt(1, Room.getIdInc());
            ps.setInt(2, Booking.getIdInc());
            ps.setInt(3, Review.getIdInc());
            ps.setInt(4, User.getIdInc());

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextInitialized(ServletContextEvent arg0) {

        createFolderForImages(arg0);
        Connection con = DBConnection.con;

        try {
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery("select * from db.config where id=1");
            rs.next();
            Room.setIdInc(rs.getInt(2));
            Booking.setIdInc(rs.getInt(3));
            Review.setIdInc(rs.getInt(4));
            User.setIdInc(rs.getInt(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        User.getUsersFromDb();
        RoomType.getRoomsFromdb();
        for (int i = 0; i < User.getUsers().size(); i++) {
            User.getUsers().get(i).loadReviewsOfUserFromDb();
        }
    }

    public void createFolderForImages(ServletContextEvent event) {
        String path = event.getServletContext().getRealPath("/");
        int a = path.indexOf("out");
        path = path.substring(0, a);
        path = path + "web\\images\\" + "room";
        File f = new File(path);
        f.mkdir();
        event.getServletContext().setAttribute("ROOM_IMAGE_FILES_DIR", path);
        path = event.getServletContext().getRealPath("/");
        a = path.indexOf("out");
        path = path.substring(0, a);
        path = path + "web\\images\\" + "user";
        f = new File(path);
        f.mkdir();
        event.getServletContext().setAttribute("USER_IMAGE_FILES_DIR", path);
    }
}