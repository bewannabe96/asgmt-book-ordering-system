package api;

import java.sql.*;
import java.util.*;

import util.*;
import model.*;

public class BookAPI {
    public static Book selectBookByISBN(String isbn) throws SQLException {
        Book book = null;

        // Reference: Documentation 5.2 - 1.a
        // TODO

        return book;
    }

    public static List<Book> selectBooksByTitle(String title) throws SQLException {
        List<Book> books = new ArrayList<Book>();

        // Reference: Documentation 5.2 - 1.b
        // TODO

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