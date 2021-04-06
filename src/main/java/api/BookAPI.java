package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class BookAPI {
    public static Book selectBookByISBN(String isbn) throws SQLException {
        // Reference: Documentation 5.2 - 1.a

        Book book = null;

        Database db = new Database();
        PreparedStatement pstatement = db.connection().prepareStatement("SELECT B.isbn, B.title, B.unit_price, B.no_copies, GROUP_CONCAT(A.name) " + 
        " FROM Book B " +
        " JOIN Author A ON B.isbn = A.isbn " +
        " WHERE B.isbn = ? " +
        " GROUP BY B.isbn;");    
        pstatement.setString(1, isbn);    
        ResultSet rs = null;
        rs = pstatement.executeQuery();

        if (rs.next()) {
            book = new Book();
            book.authors = new ArrayList<String>();
            System.out.println(rs.getString("title"));
            book.isbn = rs.getString("isbn");
            book.title = rs.getString("title");
            book.price = rs.getInt("unit_price");
            book.availableCopies = rs.getInt("no_copies");
            String authorsstring = rs.getString("GROUP_CONCAT(A.name)");
            String[] authors = authorsstring.split(",");
            for (String a: authors) {
                book.authors.add(a);
            }
        }
                        
        db.close();

        return book;
    }

    public static List<Book> selectBooksByTitle(String title) throws SQLException {
        // Reference: Documentation 5.2 - 1.b

        List<Book> books = new ArrayList<Book>();

        Database db = new Database();
        PreparedStatement pstatement = db.connection().prepareStatement("SELECT Temp.isbn, Temp.title, Temp.unit_price, Temp.no_copies, GROUP_CONCAT(A.name) " + 
        " FROM (SELECT B.isbn, B.title, B.unit_price, B.no_copies " + 
        " FROM Book B " + 
        " WHERE B.title LIKE ? " + 
        " ) AS Temp " + 
        " JOIN Author A ON Temp.isbn = A.isbn " + 
        " WHERE Temp.isbn = A.isbn " + 
        " GROUP BY Temp.isbn;");    
        pstatement.setString(1, title);    
        ResultSet rs = null;
        rs = pstatement.executeQuery();

        while (rs.next()) {
            Book book = new Book();
            book.authors = new ArrayList<String>();
            System.out.println(rs.getString("title"));
            book.isbn = rs.getString("isbn");
            book.title = rs.getString("title");
            book.price = rs.getInt("unit_price");
            book.availableCopies = rs.getInt("no_copies");
            String authorsstring = rs.getString("GROUP_CONCAT(A.name)");
            String[] authors = authorsstring.split(",");
            for (String a: authors) {
                book.authors.add(a);
            }
            books.add(book);
        }

        return books;
    }

    public static List<Book> selectBooksByAuthor(String author) throws SQLException {
        List<Book> books = new ArrayList<Book>();

        // Reference: Documentation 5.2 - 1.c
        // TODO

        return books;
    }

    public static List<Book> selectPopularBooks(int limit) throws SQLException {
        List<Book> books = new ArrayList<Book>();

        // Reference: Documentation 5.3 - 3
        // TODO: implement according to the reference
        // TODO: N in the documentation is the `limit`

        return books;
    }
}