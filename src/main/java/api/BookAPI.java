package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class BookAPI {
    public static Book selectBookByISBN(String isbn) throws SQLException {
        // Reference: Documentation 5.2 - 1.a

        Book b = new Book();
        
        b.authors = new ArrayList<String>();

        Database db = new Database();
        PreparedStatement pstatement = db.connection().prepareStatement("SELECT * FROM Book WHERE isbn=?");    
        pstatement.setString(1, isbn);    
        ResultSet rs = null;
        rs = pstatement.executeQuery();

        if (rs.next()) {
            System.out.println(rs.getString("title"));
            b.isbn = rs.getString("isbn");
            b.title = rs.getString("title");
            b.price = rs.getInt("unit_price");
            b.availableCopies = rs.getInt("no_copies");
            System.out.println(b.isbn);
            System.out.println(b.title);
            System.out.println(b.price);
            System.out.println(b.availableCopies);
        } 
        
        PreparedStatement pstatement2 = db.connection().prepareStatement("SELECT name FROM Author Where isbn = ?");    
        pstatement2.setString(1, isbn);    
        ResultSet rs2 = null;
        rs2 = pstatement2.executeQuery();

        while (rs2.next()){
            String authorsstring = rs2.getString("name");
            b.authors.add(authorsstring);
            System.out.println(authorsstring);
        }
                
        db.close();

        return b;
    }

    public static List<Book> selectBooksByTitle(String title) throws SQLException {
        // Reference: Documentation 5.2 - 1.b

        List<Book> books = new ArrayList<Book>();

        // TODO

        return books;
    }

    public static List<Book> selectBooksByAuthor(String author) throws SQLException {
        // Reference: Documentation 5.2 - 1.c

        List<Book> books = new ArrayList<Book>();

        // TODO

        return books;
    }

    public static List<Book> selectPopularBooks(int limit) throws SQLException {
        // Reference: Documentation 5.3 - 3

        List<Book> books = new ArrayList<Book>();

        // TODO: implement according to the reference
        // TODO: N in the documentation is the `limit`

        return books;
    }
}