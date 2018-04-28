package Model;

import Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Room {
    protected static int idInc;
    protected int id;
    protected String name;
    protected int adminId;
    protected int count;
    protected String description;
    protected String imagePath;
    protected int price;
    protected String startTime;
    protected String deleteTime;
    User Admin;                    //person who posted this room

    public synchronized static void incrementIdInc() {
        idInc++;
    }

    public static int getIdInc() {
        return idInc;
    }

    public static void setIdInc(int id) {
        idInc = id;
    }

    public static ArrayList<Room> getRoomsFromDb(String a) {
        ArrayList<Room> f = null;
        if (a == "RoomTypes") {
            f = getItems();
        } else {
        }
        return f;
    }

    protected static ArrayList<Room> getItems() {
        RoomType item;
        ArrayList<Room> items = new ArrayList<Room>();
        Connection myconnection = null;
        try {
            myconnection = DBConnection.con;
            PreparedStatement ps = myconnection.prepareStatement("select * from db.Room where roomType=?");
            ps.setString(1, "RoomType");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                item = new RoomType();
                item.setId(rs.getInt(1));
                item.setName(rs.getString(2));
                item.setAdminId(rs.getInt(3));
                item.setCount(rs.getInt(4));
                item.setDescription(rs.getString(5));
                item.setImagePath(rs.getString(6));
                item.setPrice(rs.getInt(7));
                item.setStartTime(rs.getString(8));
                item.setDeleteTime(rs.getString(9));
                item.setAdmin(User.findUserById(item.adminId));
                items.add(item);
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public int getId() {
        return id;
    }

    public void setId(int idd) {
        id = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int d) {
        this.adminId = d;
    }

    public User getAdmin() {
        return Admin;
    }

    public void setAdmin(User admin) {
        Admin = admin;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int c) {
        count = c;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String i) {
        imagePath = i;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int p) {
        price = p;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public void AddRoom(User user, String str) {
        Connection myconnection = null;
        boolean check = false;
        try {
            myconnection = DBConnection.con;
            PreparedStatement ps = myconnection.prepareStatement("insert into db.Room values(?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setInt(3, user.getUserId());
            ps.setInt(4, count);
            ps.setString(5, description);
            ps.setString(6, imagePath);
            ps.setInt(7, price);
            ps.setString(8, startTime);
            ps.setString(9, deleteTime);
            ps.setString(10, str);
            int rs = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}