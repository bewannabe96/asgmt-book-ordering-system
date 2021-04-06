package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class OrderAPI {
    public static Order selectOrderById(String oid) throws SQLException {
        Order order = null;
        
        // TODO

        return order;
    }

    public static List<Order> selectOrdersByMonth(String month) throws SQLException {
        List<Order> orders = new ArrayList<Order>();

        // Reference: Documentation 5.3 - 2
        // TODO: select orders of the month with shipping status 'Y'
        // TODO: orders should be sorted in ascending order by `Order ID`
        // NOTE: no need to sum charges here

        return orders;
    }

    public static List<Order> selectOrdersByCidAndYear(String cid, int year) throws SQLException {
        List<Order> orders = new ArrayList<Order>();

        // Reference: Documentation 5.2 - 4
        // TODO

        return orders;
    }

    public static void insertOrder(String cid, Map<String, Integer> orders) throws SQLException {
        // Reference: Documentation 5.2 - 2
        // TODO
    }

    public static void updateOrderStatus(String oid, char status) throws SQLException {
        // Reference: Documentation 5.3 - 1
        // TODO
    }

    public static void updateOrderQty(String oid, String isbn, int qty) throws SQLException {
        // Reference: Documentation 5.2 - 3
        // TODO
    }
}