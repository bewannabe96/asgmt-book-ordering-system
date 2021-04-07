package bos;

import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

import api.BookAPI;
import api.OrderAPI;
import model.Book;
import model.FormattedDate;
import model.Order;
import util.UserInput;

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

        // get order from DB
        oid = UserInput.getString("Order ID: "); 
        try {
            order = OrderAPI.selectOrderById(oid);
            if (order==null) {
                System.out.println("[INFO]: Order is not found");
                return;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR]: Failed load order detail");
            return;
        }
        
        // print order summary
        System.out.println("==========================");
        System.out.println("Order Summary");
        System.out.println("==========================");
        order.printDetail();
        System.out.println("==========================");

        if (order.status=='Y') {
            System.out.println("[INFO]: The order is already shipped");
            return;
        }

        String confirm = UserInput.getString("Mark order shipped (Y/N): ");
        if(!confirm.equals("Y")) return;

        try {
            OrderAPI.updateOrderStatus(oid, 'Y');
            System.out.println("[SUCCESS]: Successfully updated the order");
        } catch(SQLException e) {
            System.out.println("[ERROR]: Failed to update the order");
        }
    }

    private static void performOrderQuery() {
        List<Order> orders;
        String month = UserInput.getString("Month(YYYY-MM): ");

        if(!FormattedDate.validateMonth(month)) {
            System.out.println("[ERROR]: Month format is invalid");
            return;
        }

        try {
            orders = OrderAPI.selectOrdersByMonth(month);

            if(orders.size() == 0) {
                System.out.println("[INFO]: No order records found");
                return;
            }

            // count total sales of orders
            int totalSales = 0;
            for (Order order : orders)
                totalSales += order.charge;

            Order.printHeader();
            for(int i = 0; i < orders.size(); i++) {
                orders.get(i).printRow();
            }
            System.out.println("Total Sales: " + totalSales);
        } catch(SQLException e) {
            System.out.println("[ERROR]: Failed to load orders");
        }
    }

    private static void performPopularBookQuery() {
        List<Book> books;
        int limit = UserInput.getInt("Limit: ");

        if(limit <= 0) {
            System.out.println("[ERROR]: Limit must be greater than 0");
            return;
        }

        try {
            books = BookAPI.selectPopularBooks(limit);

            System.out.println(
                String.format("%-15s%-30s%-10s", "ISBN", "Title", "No.Cp Sold")
            );
            for (Book book : books)
                System.out.println(
                    String.format("%-15s%-30s%10d", book.isbn, book.title, book.availableCopies)
                );
        } catch (SQLException e) {
            System.out.println("[ERROR]: Failed to load books");
        }
    }
}