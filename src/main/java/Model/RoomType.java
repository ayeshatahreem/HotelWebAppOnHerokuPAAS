package Model;

import Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RoomType extends Room {
    public static ArrayList<RoomType> allItems = new ArrayList<RoomType>();
    protected String Details;

    public static void getRoomsFromdb() {
        User user;
        ArrayList<Room> list = Room.getRoomsFromDb("RoomTypes");
        if (list == null)
            return;
        for (int i = 0; i < list.size(); i++) {
            Connection myconnection = null;
            try {
                myconnection = DBConnection.con;
                PreparedStatement ps = myconnection.prepareCall("select * from db.RoomType where Id=?");
                ps.setInt(1, list.get(i).getId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ((RoomType) list.get(i)).setDetails(rs.getString(2));
                    if (((RoomType) list.get(i)).getCount() > 0) {
                        allItems.add(((RoomType) list.get(i)));
                    }
                }
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Room findItemById(int ii) {
        Room r = null;
        for (int i = 0; i < allItems.size(); i++) {
            if (ii == allItems.get(i).id) {
                r = allItems.get(i);
                break;
            }
        }
        return r;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String s) {
        this.Details = s;
    }

    public void AddRoom(User user, String str) {
        super.AddRoom(user, str);
        Connection myconnection = null;
        try {
            myconnection = DBConnection.con;
            PreparedStatement ps = myconnection.prepareStatement("insert into db.RoomType values(?,?)");
            ps.setInt(1, id);
            ps.setString(2, Details);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCountIndb() {
        Connection myconnection;
        try {
            myconnection = DBConnection.con;
            PreparedStatement ps = myconnection.prepareCall("UPDATE db.Room SET Count = ? WHERE Id = ?");
            ps.setInt(1, count);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}