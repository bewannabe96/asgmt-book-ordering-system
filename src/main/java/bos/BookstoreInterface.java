package bos;

import java.sql.*;

import util.*;
import api.*;
import model.*;

public class BookstoreInterface {
    public static void run () {
        boolean running = true;
        while(running) {
            switch(printMenu()) {
                case 1:
                    BookstoreInterface.performOrderUpdate();
                    break;

                case 2:
                    BookstoreInterface.performOrderQuery();
                    break;

                case 3:
                    BookstoreInterface.performPopularBookQuery();
                    break;

                case 4:
                    running = false;
                    break;
            }
        };
    }

    private static int printMenu() {
        System.out.println("\n<Bookstore Interface>");
        System.out.println("---------------------");
        System.out.println("1. Order Update");
        System.out.println("2. Order Query");
        System.out.println("3. Popular Book Query");
        System.out.println("4. Back");
        return UserInput.getChoice(4);
    }

    private static void performOrderUpdate() {
        String oid;
        Order order;

        oid = UserInput.getString("Order ID: "); 
        try {
            order = OrderAPI.selectOrderById(oid);
        } catch(SQLException e) {
            System.out.println("[Error]: Failed load order detail");
            return;
        } catch(Exception e) {
            System.out.println("[Error]: " + e.getMessage());
            return;
        }

        // TODO: check order condition according to Documentation 5.3 - 1

        try {
            OrderAPI.updateOrder(order);
            System.out.println("[Error]: Successfully updated the order");
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to update the order");
        }
    }

    private static void performOrderQuery() {
        String month = UserInput.getString("Month(YYYY-MM): ");

        if(!FormattedDate.validateMonth(month)) {
            System.out.println("[Error]: Month format is invalid");
            return;
        }
    }

    private static void performPopularBookQuery() {
        // TODO: Popular Book Query
    }
}