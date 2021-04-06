package bos;

import java.sql.*;
import java.util.*;

import util.*;
import api.*;
import model.*;

public class CustomerInterface {
    public static void run () {
        boolean running = true;
        while(running) {
            switch(printMenu()) {
                case 1:
                    CustomerInterface.performBookSearch();
                    break;

                case 2:
                    CustomerInterface.performOrderCreation();
                    break;

                case 3:
                    CustomerInterface.performOrderAltering();
                    break;

                case 4:
                    CustomerInterface.performOrderQuery();
                    break;

                case 5:
                    running = false;
                    break;
            }
        };
    }

    private static int printMenu() {
        System.out.println("\n<Customer Interface>");
        System.out.println("--------------------");
        System.out.println("1. Book Search");
        System.out.println("2. Order Creation");
        System.out.println("3. Order Altering");
        System.out.println("4. Order Query");
        System.out.println("5. Back");
        return UserInput.getChoice(5);
    }

    private static void performBookSearch() {
        List<Book> books = new ArrayList<Book>();

        System.out.println("\nWhat do you want to search?");
        System.out.println("1. ISBN");
        System.out.println("2. Book Title");
        System.out.println("3. Author Name");
        System.out.println("4. Back");

        try {
            switch(UserInput.getChoice(4)) {
                case 1:
                    String isbn = UserInput.getString("ISBN: ");
                    if(!Book.validateIsbn(isbn)) {
                        System.out.println("[Error]: ISBN is not in valid format");
                        return;
                    }
                    books.add(BookAPI.selectBookByISBN(isbn));
                    break;

                case 2:
                    String title = UserInput.getString("Book Title: ");
                    books = BookAPI.selectBooksByTitle(title);
                    break;

                case 3:
                    String author = UserInput.getString("Author Name: ");
                    books = BookAPI.selectBooksByAuthor(author);
                    break;

                case 4:
                    return;
            }

            if(books.size() == 0) {
                System.out.println("No book records found");
                return;
            }

            for(int i = 0; i < books.size(); i++) {
                System.out.println("Record " + i);
                System.out.println("ISBN:\t" + books.get(i).isbn);
                System.out.println("Book Title:\t" + books.get(i).title);
                System.out.println("Unit Price:\t" + books.get(i).price);
                System.out.println("No. Available Copies:\t" + books.get(i).availableCopies);
                System.out.println("Authors:");
                for(int j = 0; j < books.get(i).authors.size(); j++)
                    System.out.println("" + j + ". " + books.get(i).authors.get(j));
            }
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to search books");
        }
    }

    private static void performOrderCreation() {
        String cid, orderInput;
        int orderQty;
        Map<String, Integer> orders = new HashMap<String, Integer>();

        cid = UserInput.getString("Customer ID: "); 

        System.out.println("Orders: ");
        System.out.println("* Press 'L' to see list of orders");
        System.out.println("* Press 'F' to finish ordering");
        while(true) {
            orderInput = UserInput.getString("Book ISBN / 'L' / 'F': ");
            if(orderInput.equals("L")) {
                // TODO: list orders
            } else if(orderInput.equals("F")) {
                break;
            } else {
                orderQty = UserInput.getInt("[" + orderInput + "] Quantity: ");
                // TODO: lookup if order request is valid
                // TODO: if valid push it into `orders`
                // TODO: print error message otherwise
            }
        }

        try {
            OrderAPI.insertOrder(cid, orders);
            System.out.println("[Error]: Successfully created order");
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to create order");
        }
    }

    private static void performOrderAltering() {
        String oid, isbn, action;
        int quantity;
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

        // TODO: print order detail

        isbn = UserInput.getString("Book ISBN: ");
        action = UserInput.getString("Action [add, remove]: ");
        quantity = UserInput.getInt("Quantity: ");

        if(action.equals("add")) {
            // TODO: validate input
            // TODO: update order
        } else if(action.equals("remove")) {
            // TODO: validate input
            // TODO: update order
        } else {
            System.out.println("[Error]: Invalid action");
            return;
        }

        try {
            OrderAPI.updateOrder(order);
            System.out.println("[Error]: Successfully updated the order");
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to update the order");
        }
    }

    private static void performOrderQuery() {
        String cid;
        int year;
        List<Order> orders;

        cid = UserInput.getString("Customer ID: ");
        year = UserInput.getInt("Year: ");

        try {
            orders = OrderAPI.selectOrdersByCidAndYear(cid, year);
            
            // TODO: print orders
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to query orders");
        }
    }
}
