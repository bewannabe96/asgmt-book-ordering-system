package bos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.BookAPI;
import api.OrderAPI;
import model.Book;
import model.Order;
import util.UserInput;

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
        List<Book> books = null;

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
                        System.out.println("[ERROR]: ISBN is not in valid format");
                        return;
                    }
                    books = new ArrayList<Book>();
                    Book book = BookAPI.selectBookByISBN(isbn);
                    if(book!=null) books.add(book);
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
                System.out.println("[INFO]: No book records found");
                return;
            }

            Book.printHeader();
            for(int i = 0; i < books.size(); i++) {
                books.get(i).printRow();
            }
        } catch(SQLException e) {
            System.out.println("[ERROR]: Failed to search books");
        }
    }

    private static void performOrderCreation() {
        String cid, isbn;
        int quantity;
        Map<String, Integer> orders = new HashMap<String, Integer>();

        cid = UserInput.getString("Customer ID: "); 

        System.out.println("Orders: ");
        System.out.println("* Press 'L' to see list of orders");
        System.out.println("* Press 'F' to finish ordering");
        try {
            while (true) {
                isbn = UserInput.getString("Book ISBN / 'L' / 'F': ");
                if (isbn.equals("L")) {
                    System.out.println("ISBN\t\tQuantity");
                    for (Map.Entry<String,Integer> entry : orders.entrySet())
                        System.out.println(entry.getKey()+"\t"+entry.getValue());
                } else if (isbn.equals("F")) {
                    break;
                } else {
                    quantity = UserInput.getInt("[" + isbn + "] Quantity: ");

                    Book book = BookAPI.selectBookByISBN(isbn);
                    if (book== null) {
                        System.out.println("[INFO]: Book with the ISBN does not exist");
                        continue;
                    } else if (quantity > book.availableCopies) {
                        System.out.println("[INFO]: Not enough book copies available");
                        continue;
                    }

                    orders.put(isbn, quantity);
                }
            }

            OrderAPI.insertOrder(cid, orders);
            System.out.println("[SUCCESS]: Successfully created order");
        } catch (SQLException e) {
            System.out.println("[ERROR]: Failed to create order");
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
    }

    private static void performOrderAltering() {
        String oid, isbn, action;
        int quantity;
        Order order;

        oid = UserInput.getString("Order ID: "); 
        try {
            order = OrderAPI.selectOrderById(oid);
            if (order==null) {
                System.out.println("[INFO]: Order is not found");
                return;
            } else if (order.status == 'Y') {
                System.out.println("[INFO]: The order is already shipped");
                return;
            }
        } catch(SQLException e) {
            System.out.println("[ERROR]: Failed load order detail");
            return;
        }

        // print order summary
        System.out.println("==========================");
        System.out.println("Order Summary");
        System.out.println("==========================");
        order.printDetail();
        System.out.println("==========================");


        try {
            while (true) {
                isbn = UserInput.getString("Book ISBN: ");
                action = UserInput.getString("Action [add, remove]: ");
                quantity = UserInput.getInt("Quantity: ");

                if (order.orders.get(isbn) == null) {
                    System.out.println("[ERROR]: Book with the ISBN does not exist in the order");
                    continue;
                }

                Book book = BookAPI.selectBookByISBN(isbn);
                if (book == null) {
                    System.out.println("[INFO]: Book with the ISBN does not exist");
                    continue;
                }

                if(action.equals("add")) {
                    if (quantity > book.availableCopies) {
                        System.out.println("[INFO]: Not enough book copies available");
                        continue;
                    }
                    break;
                } else if(action.equals("remove")) {
                    if (quantity > order.orders.get(isbn)) {
                        System.out.println("[ERROR]: Quantity should be smaller than the ordered");
                        continue;
                    }
                    break;
                } else {
                    System.out.println("[ERROR]: Invalid action");
                    continue;
                }
            }

            OrderAPI.updateOrderQty(oid, isbn, action, quantity);
            System.out.println("[INFO]: Successfully updated the order");
        } catch(SQLException e) {
            System.out.println("[ERROR]: Failed to update the order");
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

            if(orders.size() == 0) {
                System.out.println("[INFO]: No order records found");
                return;
            }

            Order.printHeader();
            for(int i = 0; i < orders.size(); i++) {
                orders.get(i).printRow();
            }
        } catch(SQLException e) {
            System.out.println("[ERROR]: Failed to query orders");
        }
    }
}
