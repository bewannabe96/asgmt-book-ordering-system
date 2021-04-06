package model;

import java.util.List;

public class Book {
    public String isbn;             // format of "X-XXXX-XXXX-X"
    public String title;            // non-empty string of max 100 characters
    public int price;               // non-negative integer
    public int availableCopies;     // non-negative integer
    public List<String> authors;    // list of authors

    public static boolean validateIsbn(String isbn) {
        // TODO: validate ISBN
        if (isbn.length()>13 || isbn.length()<13) 
            return false;
        else
            return true;
    }
}