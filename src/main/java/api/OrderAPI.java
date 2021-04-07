package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class OrderAPI {
    public static Order selectOrderById(String oid) throws SQLException {
        Order order = null;

        Database db = new Database();
        PreparedStatement stmt = db.connection().prepareStatement(
            "SELECT O.oid, O.cid, O.order_date, O.ship_status,"
            + "     C.quantity AS total_qty, C.charge, OI.isbn, OI.quantity FROM orders O"
            + " JOIN ("
            + "     SELECT O.oid, SUM(O.quantity) AS quantity, SUM(O.quantity * B.unit_price) AS charge from ordering O"
            + "         JOIN book B ON O.isbn=B.isbn"
            + "     GROUP BY O.oid"
            + " ) C ON C.oid=O.oid"
            + " JOIN ordering OI ON OI.oid=O.oid"
            + " WHERE O.oid=?"
        );
        stmt.setString(1, oid);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            if(order == null) {
                order = new Order();
                order.oid = rs.getString("oid");
                order.cid = rs.getString("cid");
                order.date = rs.getString("order_date");
                order.quantity = rs.getInt("total_qty");
                order.charge = rs.getInt("charge");
                order.status = rs.getString("ship_status").charAt(0);
                order.orders = new HashMap<String, Integer>();
            }
            order.orders.put(rs.getString("isbn"), rs.getInt("quantity"));
        }
        db.close();

        return order;
    }

    public static List<Order> selectOrdersByMonth(String month) throws SQLException {
        // Reference: Documentation 5.3 - 2

        Order order = null;
        List<Order> orders = new ArrayList<Order>();

        Database db = new Database();
        PreparedStatement stmt = db.connection().prepareStatement(
            "SELECT O.oid, O.cid, O.order_date, O.ship_status,"
            + "     C.quantity AS total_qty, C.charge, OI.isbn, OI.quantity FROM orders O"
            + " JOIN ("
            + "     SELECT O.oid, SUM(O.quantity) AS quantity,"
            + "         IF(O.quantity=0, 0, SUM(O.quantity * B.unit_price) + SUM(O.quantity * 10) + 10) AS charge"
            + "     FROM ordering O"
            + "         JOIN book B ON O.isbn=B.isbn"
            + "     GROUP BY O.oid"
            + " ) C ON C.oid=O.oid"
            + " JOIN ordering OI ON OI.oid=O.oid"
            + " WHERE DATE_FORMAT(O.order_date,'%Y-%m') = ?"
            + " ORDER BY O.oid ASC"
        );
        stmt.setString(1, month);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String oid = rs.getString("oid");
            if (order == null || !order.oid.equals(oid))  {
                if(order != null) orders.add(order);

                order = new Order();
                order.oid = oid;
                order.cid = rs.getString("cid");
                order.date = rs.getString("order_date");
                order.quantity = rs.getInt("quantity");
                order.charge = rs.getInt("charge");
                order.status = rs.getString("ship_status").charAt(0);
                order.orders = new HashMap<String, Integer>();
            }
            order.orders.put(rs.getString("isbn"), rs.getInt("quantity"));
        }
        orders.add(order);
        db.close();

        return orders;
    }

    public static List<Order> selectOrdersByCidAndYear(String cid, int year) throws SQLException {
        // Reference: Documentation 5.2 - 4

        Order order = null;
        List<Order> orders = new ArrayList<Order>();

        Database db = new Database();
        PreparedStatement stmt = db.connection().prepareStatement(
            "SELECT O.oid, O.cid, O.order_date, O.ship_status,"
            + "     C.quantity AS total_qty, C.charge, OI.isbn, OI.quantity FROM orders O"
            + " JOIN ("
            + "     SELECT O.oid, SUM(O.quantity) AS quantity,"
            + "         IF(O.quantity=0, 0, SUM(O.quantity * B.unit_price) + SUM(O.quantity * 10) + 10) AS charge"
            + "     FROM ordering O"
            + "         JOIN book B ON O.isbn=B.isbn"
            + "     GROUP BY O.oid"
            + " ) C ON C.oid=O.oid"
            + " JOIN ordering OI ON OI.oid=O.oid"
            + " WHERE cid = ? AND YEAR(O.order_date) = ?"
            + " ORDER BY O.oid ASC"
        );
        stmt.setString(1, cid);
        stmt.setInt(2, year);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String oid = rs.getString("oid");
            if (order == null || !order.oid.equals(oid))  {
                if(order != null) orders.add(order);

                order = new Order();
                order.oid = oid;
                order.cid = rs.getString("cid");
                order.date = rs.getString("order_date");
                order.quantity = rs.getInt("quantity");
                order.charge = rs.getInt("charge");
                order.status = rs.getString("ship_status").charAt(0);
                order.orders = new HashMap<String, Integer>();
            }
            order.orders.put(rs.getString("isbn"), rs.getInt("quantity"));
        }
        orders.add(order);
        db.close();

        return orders;
    }

    public static void insertOrder(String cid, Map<String, Integer> orders) throws Exception, SQLException {
        // Reference: Documentation 5.2 - 2

        int total_qty = 0;
        for (Map.Entry<String,Integer> entry : orders.entrySet())
            total_qty += entry.getValue();
        if (total_qty == 0) {
            throw new Exception("Order must include at least one book order");
        }

        Database db = new Database();

        int newOid = -1;
        Statement stmt1 = db.connection().createStatement();
        ResultSet rs = stmt1.executeQuery("SELECT MAX(oid) AS latest FROM orders");
        if (rs.next()) {
            newOid = rs.getInt("latest") + 1;
        }

        try {
            PreparedStatement stmt2 = db.connection().prepareStatement(
                "INSERT INTO orders (oid, cid, order_date, ship_status)"
                + " VALUES (LPAD(?,8,'0'), ?, ?, 'N')"
            );
            stmt2.setInt(1, newOid);
            stmt2.setString(2, cid);
            stmt2.setString(3, SystemTimeAPI.get());
            stmt2.executeUpdate();
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }

        PreparedStatement stmt3 = db.connection().prepareStatement(
            "INSERT INTO ordering (oid, isbn, quantity)"
            + " VALUES (LPAD(?,8,'0'), ?, ?)"
        );
        PreparedStatement stmt4 = db.connection().prepareStatement(
            "UPDATE book SET no_copies = no_copies - ? WHERE isbn = ?"
        );
        for (Map.Entry<String,Integer> entry : orders.entrySet()) {
            stmt3.setInt(1, newOid);
            stmt3.setString(2, entry.getKey());
            stmt3.setInt(3, entry.getValue());
            stmt3.executeUpdate();

            stmt4.setInt(1, entry.getValue());
            stmt4.setString(2, entry.getKey());
            stmt4.executeUpdate();
        }

        db.close();
    }

    public static void updateOrderStatus(String oid, char status) throws SQLException {
        // Reference: Documentation 5.3 - 1

        Database db = new Database();
        PreparedStatement stmt = db.connection().prepareStatement(
            "UPDATE orders SET ship_status = ? WHERE oid = ?"
        );
        stmt.setString(1, ""+status);
        stmt.setString(2, oid);

        stmt.executeUpdate();
        db.close();
    }

    public static void updateOrderQty(String oid, String isbn, String action, int qty) throws SQLException {
        // Reference: Documentation 5.2 - 3
        
        Database db = new Database();
        PreparedStatement stmt1, stmt2;

        if(action.equals("add")) {
            stmt1 = db.connection().prepareStatement(
                "UPDATE ordering SET quantity = quantity + ? WHERE oid = ? AND isbn = ?"
            );
            stmt2 = db.connection().prepareStatement(
                "UPDATE book SET no_copies = no_copies - ? WHERE isbn = ?"
            );
        } else {
            stmt1 = db.connection().prepareStatement(
                "UPDATE ordering SET quantity = quantity - ? WHERE oid = ? AND isbn = ?"
            );
            stmt2 = db.connection().prepareStatement(
                "UPDATE book SET no_copies = no_copies + ? WHERE isbn = ?"
            );
        }
        stmt1.setInt(1, qty);
        stmt1.setString(2, oid);
        stmt1.setString(3, isbn);
        stmt1.executeUpdate();

        stmt2.setInt(1, qty);
        stmt2.setString(2, isbn);
        stmt2.executeUpdate();

        db.close();
    }

    public static String selectLatestOrderTime() throws SQLException {
        String date = "";

        Database db = new Database();
        Statement stmt = db.connection().createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT DATE(order_date) AS latest FROM orders ORDER BY order_date DESC LIMIT 1"
        );

        if (rs.next()) {
            date = rs.getString("latest");
        }
        db.close();
        return date;
    }
}