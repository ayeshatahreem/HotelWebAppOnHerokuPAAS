package Model;

import Database.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Booking {
    protected static int idInc;
    protected int orderId;
    protected int buyerID;
    protected int adminID;
    protected int roomId;
    protected String whenBooked;
    protected String whenServiceBegan;
    protected int amount;
    protected int quantity;
    protected int status;
    User buyer;
    User admin;
    Room room;

    public static int getIdInc() {
        return idInc;
    }

    public static void setIdInc(int idInc) {
        Booking.idInc = idInc;
    }

    public synchronized static void incrementIdInc() {
        idInc++;
    }

    public static ArrayList<Booking> getBooking(String a, int userId) {
        ArrayList<Booking> orders = new ArrayList<Booking>();
        Booking order;
        try {
            Connection myconnection = DBConnection.con;
            String sqlString = "SELECT * FROM db.Booking WHERE " + a + "='" + userId + "'";
            Statement myStatement = myconnection.createStatement();
            ResultSet rs = myStatement.executeQuery(sqlString);
            while (rs.next()) {
                order = new Booking();
                order.orderId = rs.getInt("bookingId");
                int i = Integer.valueOf(rs.getString("buyer"));
                order.buyer = User.findUserById(i);
                i = Integer.valueOf(rs.getString("adminId"));
                order.admin = User.findUserById(i);
                i = Integer.valueOf(rs.getString("room"));
                order.room = RoomType.findItemById(i);
                order.buyerID = order.buyer.getUserId();
                order.adminID = order.admin.getUserId();
                order.roomId = order.room.getId();
                order.whenBooked = rs.getString("WhenBooked");
                order.whenServiceBegan = rs.getString("WhenServiceBegan");
                order.amount = rs.getInt("amountPaid");
                order.quantity = rs.getInt("quantity");
                order.status = rs.getInt("status");
                orders.add(order);
            }
            myStatement.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orders;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(int buyerID) {
        this.buyerID = buyerID;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int sellerID) {
        this.adminID = sellerID;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int rId) {
        this.roomId = rId;
    }

    public String getWhenOrderPlaced() {
        return whenBooked;
    }

    public void setWhenOrderPlaced(String whenOrderPlaced) {
        this.whenBooked = whenOrderPlaced;
    }

    public String getWhenDelivered() {
        return whenServiceBegan;
    }

    public void setWhenDelivered(String whenDelivered) {
        this.whenServiceBegan = whenDelivered;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User seller) {
        this.admin = seller;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room r) {
        this.room = r;
    }

    public void PlaceBooking(RoomType item, User user, int q) {
        synchronized (Booking.class) {
            orderId = idInc;
        }
        Booking.incrementIdInc();
        buyerID = user.userId;
        adminID = item.getAdmin().getUserId();
        roomId = item.getId();
        whenBooked = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        whenServiceBegan = "";
        amount = item.getPrice() * q;
        quantity = q;
        status = 1;//1 means current, 0 means previous
        buyer = user;
        admin = item.getAdmin();
        room = item;
        user.bookingsPlaced.add(this);
        item.getAdmin().bookingsTaken.add(this);
        item.setCount(item.getCount() - q);
        //change quantity in db
        item.updateCountIndb();
        if (item.count == 0) {
            RoomType.allItems.remove(item);
        }
        Connection myconnection = null;
        boolean check = false;
        try {
            myconnection = DBConnection.con;
            PreparedStatement ps = myconnection.prepareStatement("insert into db.Booking values(?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, orderId);
            ps.setInt(2, buyerID);
            ps.setInt(3, adminID);
            ps.setInt(4, roomId);
            ps.setString(5, whenBooked);
            ps.setString(6, whenServiceBegan);
            ps.setInt(7, amount);
            ps.setInt(8, quantity);
            ps.setInt(9, status);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}