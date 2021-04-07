package model;

import java.util.regex.Pattern;
import java.util.List;

public class Book {
    public String isbn;             // format of "X-XXXX-XXXX-X"
    public String title;            // non-empty string of max 100 characters
    public int price;               // non-negative integer
    public int availableCopies;     // non-negative integer
    public List<String> authors;    // list of authors

    public static boolean validateIsbn(String isbn) {
        return Pattern.matches("\\d-\\d\\d\\d\\d-\\d\\d\\d\\d-\\d", isbn);
    }

    public static void printHeader() {
        System.out.println(
            String.format(
                "%-15s%-30s%-10s%-10s%-12s", "ISBN", "Title", "Price", "No.Cp", "Authors"
            )
        );
    }

    public void printRow() {
        System.out.println(
            String.format(
                "%-15s%-30s%-10s%-10s%-12s",
                this.isbn, this.title, this.price, this.availableCopies, this.authors.get(0)
            )
        );
        for(int i=1; i<this.authors.size(); i++)
            System.out.println(
                String.format("%65s%-12s", "", this.authors.get(i))
            );
    }

    public void printDetail() {
        System.out.println("ISBN:\t\t" + this.isbn);
        System.out.println("Book Title:\t" + this.title);
        System.out.println("Unit Price:\t" + this.price);
        System.out.println("No. Copies:\t" + this.availableCopies);
        System.out.println("---------------------");
        System.out.println("Authors:");
        for(int j = 0; j < this.authors.size(); j++)
            System.out.println("" + (j+1) + ". " + this.authors.get(j));
    }
}