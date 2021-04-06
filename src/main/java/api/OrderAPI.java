package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class OrderAPI {
    public static Order selectOrderById(String oid) throws Exception, SQLException {
        // TODO
        // throw Exception('Order is not found') when no result
        return new Order();
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

    public static void updateOrder(Order order) throws SQLException {
        // Reference: Documentation 5.2 - 3
        // Reference: Documentation 5.3 - 1
        // TODO
    }
}