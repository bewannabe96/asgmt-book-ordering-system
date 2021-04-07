package api;

import java.sql.*;
import java.io.*;
import java.util.function.BiConsumer;
import java.util.concurrent.atomic.AtomicInteger;

import util.*;
import model.*;

import java.io.File;
import java.util.Scanner;

public class SchemaAPI {
    public static void createTables() throws SQLException {
        Database db = new Database();
        Statement stmt = db.connection().createStatement();
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS customer ("
            + " cid VARCHAR(10),"
            + " name VARCHAR(50),"
            + " ship_addr VARCHAR(200),"
            + " card_no VARCHAR(19),"
            + " PRIMARY KEY(cid)"
            + ")"
        );
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS book ("
            + " isbn VARCHAR(13),"
            + " title VARCHAR(100),"
            + " unit_price INT,"
            + " no_copies INT,"
            + " PRIMARY KEY(isbn)"
            + ")"
        );
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS author ("
            + " isbn VARCHAR(13),"
            + " name VARCHAR(50),"
            + " PRIMARY KEY(isbn, name),"
            + " FOREIGN KEY(isbn) REFERENCES book (isbn)"
            + ")"
        );
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS orders ("
            + " oid VARCHAR(8),"
            + " cid VARCHAR(10),"
            + " order_date DATETIME,"
            + " ship_status CHAR(1),"
            + " PRIMARY KEY(oid),"
            + " FOREIGN KEY(cid) REFERENCES customer (cid)"
            + ")"
        );
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS ordering ("
            + " oid VARCHAR(8),"
            + " isbn VARCHAR(13),"
            + " quantity INT,"
            + " PRIMARY KEY(oid, isbn),"
            + " FOREIGN KEY(oid) REFERENCES orders (oid),"
            + " FOREIGN KEY(isbn) REFERENCES book (isbn)"
            + ")"
        );
        // stmt.executeUpdate(
        //     "CREATE VIEW IF NOT EXISTS order_summary AS"
        //     + " SELECT oid, cid, order_date, ship_status, charge FROM orders O"
        //     + "     JOIN ("
        //     + "         SELECT O.oid, SUM(O.quantity * B.unit_price) AS charge from ordering O"
        //     + "             JOIN book B ON O.isbn=B.isbn"
        //     + "         GROUP BY O.oid"
        //     + "     ) C ON C.oid=O.oid"
        // );
        db.close();
    }

    public static void deleteTables() throws SQLException {
        Database db = new Database();
        Statement stmt = db.connection().createStatement();

        stmt.executeUpdate("DROP TABLE IF EXISTS ordering");
        stmt.executeUpdate("DROP TABLE IF EXISTS orders");

        stmt.executeUpdate("DROP TABLE IF EXISTS author");
        stmt.executeUpdate("DROP TABLE IF EXISTS book");

        stmt.executeUpdate("DROP TABLE IF EXISTS customer");

        db.close();
    }

    private static void loadFile(String filepath, String query, BiConsumer<PreparedStatement,String[]> executor) throws Exception {
        File datafile;
        Scanner scanner;
        String[] datarow;

        try {
            datafile = new File(filepath);
            scanner = new Scanner(datafile);
        } catch (FileNotFoundException e) {
            throw new Exception("File \"" + filepath + "\" not found");
        }

        try {
            System.out.println("Loading \"" + filepath + "\"...");
            Database db = new Database();
            PreparedStatement stmt = db.connection().prepareStatement(query);
            while (scanner.hasNextLine()) {
                executor.accept(stmt, scanner.nextLine().split("\\|"));
            }
            db.close(); 
        } catch (SQLException e) {
            throw new Exception("There is problem with the file format");
        }

        System.out.println("Successfully loaded \"" + filepath + "\"...\n");
    }

    public static void loadData(String path) throws Exception {
        String filepath;
        AtomicInteger success;
        
        filepath = path + "/customer.txt";
        loadFile(
            filepath,
            "INSERT INTO customer (cid, name, ship_addr, card_no) VALUES (?, ?, ?, ?);",
            (stmt, datarow) -> {
                try {
                    stmt.setString(1, datarow[0]);
                    stmt.setString(2, datarow[1]);
                    stmt.setString(3, datarow[2]);
                    stmt.setString(4, datarow[3]);
                    stmt.executeUpdate();
                } catch(SQLException e) {}
            }
        );

        filepath = path + "/book.txt";
        loadFile(
            filepath,
            "INSERT INTO book (isbn, title, unit_price, no_copies) VALUES (?, ?, ?, ?);",
            (stmt, datarow) -> {
                try {
                    stmt.setString(1, datarow[0]);
                    stmt.setString(2, datarow[1]);
                    stmt.setString(3, datarow[2]);
                    stmt.setString(4, datarow[3]);
                    stmt.executeUpdate();
                } catch(SQLException e) {}
            }
        );

        filepath = path + "/book_author.txt";
        loadFile(
            filepath,
            "INSERT INTO author (isbn, name) VALUES (?, ?);",
            (stmt, datarow) -> {
                try {
                    stmt.setString(1, datarow[0]);
                    stmt.setString(2, datarow[1]);
                    stmt.executeUpdate();
                } catch(SQLException e) {}
            }
        );

        filepath = path + "/orders.txt";
        loadFile(
            filepath,
            "INSERT INTO orders (oid, cid, order_date, ship_status) VALUES (?, ?, ?, ?);",
            (stmt, datarow) -> {
                try {
                    stmt.setString(1, datarow[0]);
                    stmt.setString(2, datarow[4]);
                    stmt.setString(3, datarow[1]);
                    stmt.setString(4, datarow[2]);
                    stmt.executeUpdate();
                } catch(SQLException e) {}
            }
        );

        filepath = path + "/ordering.txt";
        loadFile(
            filepath,
            "INSERT INTO ordering (oid, isbn, quantity) VALUES (?, ?, ?);",
            (stmt, datarow) -> {
                try {
                    stmt.setString(1, datarow[0]);
                    stmt.setString(2, datarow[1]);
                    stmt.setString(3, datarow[2]);
                    stmt.executeUpdate();
                } catch(SQLException e) {}
            }
        );
    }

    public static void loadDataa(String path) throws Exception, SQLException, FileNotFoundException {
        File dir = new File(path);
        if(!dir.isDirectory()) throw new Exception("Path is not directory");

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) throw new Exception("Directory is empty");

        for (File file : files) {
            String filename = file.getName();
            Scanner scanner = new Scanner(file);

            if (filename.equals("book.txt")) {
                System.out.println("Loading book data...");

                Book b = new Book();
                while (scanner.hasNextLine()) {
                    String[] data_tokens = scanner.nextLine().split("\\|");
                    
                    b.isbn = data_tokens[0]; //string
                    b.title = data_tokens[1]; //string
                    b.price = Integer.parseInt(data_tokens[2]); //int
                    b.availableCopies = Integer.parseInt(data_tokens[3]);//int

                    Database db = new Database();
                    String psql = "INSERT INTO Book Values(?,?,?,?)";
                    PreparedStatement pstmt = db.connection().prepareStatement(psql);
                    pstmt.setString(1, b.isbn);
                    pstmt.setString(2, b.title);
                    pstmt.setInt(3, b.price);
                    pstmt.setInt(4, b.availableCopies);
                    pstmt.executeUpdate();
                }
            } else if (filename.equals("customer.txt")) {
                System.out.println("Loading customer data...");

                Customer c = new Customer();
                while (scanner.hasNextLine()) {
                    String[] data_tokens = scanner.nextLine().split("\\|");
                    
                    c.cid = data_tokens[0]; //string
                    c.name = data_tokens[1]; //string
                    c.addr = data_tokens[2]; //string
                    c.cardNo = data_tokens[3];//string

                    Database db = new Database();
                    String psql = "INSERT INTO Customer Values(?,?,?,?)";
                    PreparedStatement pstmt = db.connection().prepareStatement(psql);
                    pstmt.setString(1, c.cid);
                    pstmt.setString(2, c.name);
                    pstmt.setString(3, c.addr);
                    pstmt.setString(4, c.cardNo);
                    pstmt.executeUpdate();
                }

            }
            else if (filename.equals("orders.txt")) {
                System.out.println("Loading order data...");

                Order o = new Order();
                while (scanner.hasNextLine()) {
                    String[] data_tokens = scanner.nextLine().split("\\|");
                    
                    o.oid = data_tokens[0]; //string
                    //o.date = data_tokens[1]; //date
                    Date sqldate = Date.valueOf(data_tokens[1]); //Date
                    o.status = data_tokens[2].charAt(0); //char
                    o.charge = Integer.parseInt(data_tokens[3]); //int
                    o.cid = data_tokens[4]; //string

                    Database db = new Database();
                    String psql = "INSERT INTO Orders Values(?,?,?,?,?)";
                    PreparedStatement pstmt = db.connection().prepareStatement(psql);
                    pstmt.setString(1, o.oid);
                    pstmt.setString(2, o.cid);
                    pstmt.setDate(3, sqldate);
                    pstmt.setInt(4, o.charge);
                    pstmt.setString(5, data_tokens[2]);
                    pstmt.execute();
                }

            }
            scanner.close();
        }

        for (File file : files) {
            String filename = file.getName();
            Scanner scanner = new Scanner(file);
            
            if (filename.equals("book_author.txt")) {
                System.out.println("Loading author data...");

                Book au = new Book();
                while (scanner.hasNextLine()) {
                    String[] data_tokens = scanner.nextLine().split("\\|");
                    
                    au.isbn = data_tokens[0]; //string
                    //au.authors = Arrays.asList(data_tokens[2].split("\\s*,\\s*"));
                    Database db = new Database();
                    String psql = "INSERT INTO Author Values(?,?)";
                    PreparedStatement pstmt = db.connection().prepareStatement(psql);
                    pstmt.setString(1, au.isbn);
                    pstmt.setString(2, data_tokens[1]);
                    pstmt.executeUpdate();
                }
            }

            else if (filename.equals("ordering.txt")) {
                System.out.println("Loading order detail data...");

                Order ordering = new Order();
                while (scanner.hasNextLine()) {
                    String[] data_tokens = scanner.nextLine().split("\\|");
                    
                    ordering.oid = data_tokens[0]; //string, oid
                    //ordering.orders.put(data_tokens[1], Integer.parseInt(data_tokens[2])); //isbn, quantity

                    Database db = new Database();
                    String psql = "INSERT INTO Ordering Values(?,?,?)";
                    PreparedStatement pstmt = db.connection().prepareStatement(psql);

                    pstmt.setString(1, ordering.oid);
                    pstmt.setString(2, data_tokens[1]);
                    pstmt.setInt(3, Integer.parseInt(data_tokens[2]));
                    pstmt.executeUpdate();
                }

            }
            scanner.close();
        }
    }
}