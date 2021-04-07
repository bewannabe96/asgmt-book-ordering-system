package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class OrderAPI {
    public static Order selectOrderById(String oid) throws SQLException {
        Order order = null;

        // Database db = new Database();
        // PreparedStatement pstatement = db.connection().prepareStatement("SELECT Order.oid, Order.data, Order.charge,Order.status " +
        //         " FROM Order " +
        //         " WHERE Order.oid = ? "
        //         );
        // pstatement.setString(1, oid);
        // ResultSet rs = null;
        // rs = pstatement.executeQuery();
        // if (rs.next()) {
        //     order = new Order();

        //    order.oid = rs.getString("oid");
        //    order.data = rs.getString("data");
        //    order.charge = rs.getInt("charge");
        //    order.status = rs.getString("status");
        // }
        // db.close();
        return order;
    }

    public static List<Order> selectOrdersByMonth(String month) throws SQLException {
        // Reference: Documentation 5.3 - 2

        List<Order> orders = new ArrayList<Order>();
        // Database db = new Database();
        // PreparedStatement pstatement = db.connection().prepareStatement("SELECT Order.oid, Order.data, Order.charge,Order.status " +
        //         " FROM Order " +
        //         " WHERE month(order.date) = ? "
        // );
        // pstatement.setString(1, month);
        // ResultSet rs = null;
        // rs = pstatement.executeQuery();

        // while (rs.next()) {
        //     Order order = new Order();

        //     order.oid = rs.getString("oid");
        //     order.data = rs.getString("data");
        //     order.charge = rs.getInt("charge");
        //     order.status = rs.getString("status");
        //     orders.add(order);
        // }
        // db.close();
        return orders;
    }

    public static List<Order> selectOrdersByCidAndYear(String cid, int year) throws SQLException {
        // Reference: Documentation 5.2 - 4

        List<Order> orders = new ArrayList<Order>();

        // Database db = new Database();
        // PreparedStatement pstatement = db.connection().prepareStatement("SELECT Order.oid, Order.data, Order.charge,Order.status " +
        //         " FROM Order,Ordering " +
        //         " WHERE year(order.data) = ? AND cid=? "
        // );
        // pstatement.setString(1, year);
        // pstatement.setString(2, cid);
        // ResultSet rs = null;
        // rs = pstatement.executeQuery();

        // while (rs.next()) {
        //     Order order = new Order();

        //     order.oid = rs.getString("oid");
        //     order.data = rs.getString("data");
        //     order.charge = rs.getInt("charge");
        //     order.status = rs.getString("status");
        //     orders.add(order);
        // }
        // db.close();
        return orders;
    }

    public static void insertOrder(String cid, Map<String, Integer> orders) throws SQLException {
        // Reference: Documentation 5.2 - 2

        // Database db = new Database();
        // PreparedStatement pstatement = db.connection().prepareStatement("insert into Ordering(cid,isbn,quantity) values(?,?,?)");
        // String isbn="";
        // Integer qty=0;
        // for(String key:orders.keySet()){
        //     qty = orders.get(key);
        //     isbn=key;
        // }
        // pstatement.setString(1, cid);
        // pstatement.setString(2, isbn);
        // pstatement.setInt(3, qty);
        // pstatement.executeUpdate();
        // db.close();
    }

    public static void updateOrderStatus(String oid, char status) throws SQLException {
        // Reference: Documentation 5.3 - 1

        // Database db = new Database();
        // PreparedStatement pstatement = db.connection().prepareStatement("update Order set status=? where oid=?");
        // pstatement.setString(1, status);
        // pstatement.setString(2, oid);
        // pstatement.executeUpdate();
        // db.close();
    }

    public static void updateOrderQty(String oid, String isbn, int qty) throws SQLException {
        // Reference: Documentation 5.2 - 3
        
        // Database db = new Database();
        // PreparedStatement pstatement = db.connection().prepareStatement("update Ordering set isbn=?,quantity=? where oid=?");
        // pstatement.setString(1, isbn);
        // pstatement.setInt(2, qty);
        // pstatement.setString(3, oid);
        // pstatement.executeUpdate();
        // db.close();
    }

    public static String selectLatestOrderTime() throws SQLException {
        String date = "";

        Database db = new Database();
        Statement stmt = db.connection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT DATE(date) AS latest FROM Orders ORDER BY date DESC LIMIT 1");

        if (rs.next()) {
            date = rs.getString("latest");
        }
        db.close();
        return date;
    }
}