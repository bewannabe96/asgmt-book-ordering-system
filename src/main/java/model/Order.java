package model;

import java.util.Map;

public class Order {
    public String oid;                      // format of "XXXXXXXX"
    public String cid;                      // non-empty string of maximum 10 chars
    public String date;                     // fomrat of "YYYY-MM-DD"
    public int quantity;                    // non-negative integer
    public int charge;                      // non-negative integer
    public char status;                     // one of <"Y", "N">
    public Map<String, Integer> orders;     // key in format of "X-XXXX-XXXX-X" and value the quantity

    public static void printHeader() {
        System.out.println(
            String.format(
                "%-10s%-11s%-10s%-8s%-9s%-12s%-15s%-4s",
                "ID", "Customer", "Shipped", "Books", "Charge", "Date", "ISBN", "Qty."
            )
        );
    }

    public void printRow() {
        System.out.println(
            String.format(
                "%-10s%-11s%-10s%-8s%-9s%-10s",
                this.oid, this.cid, this.status, this.quantity, this.charge, this.date
            )
        );
        for (Map.Entry<String,Integer> entry : this.orders.entrySet())
            System.out.println(
                String.format("%60s%-15s%4d", "", entry.getKey(), entry.getValue())
            );
    }

    public void printDetail() {
        System.out.println("Order ID:\t" + this.oid);
        System.out.println("Customer ID:\t" + this.cid);
        System.out.println("Date:\t\t" + this.date);
        System.out.println("Charge:\t\t" + this.charge);
        System.out.println("Total Books:\t" + this.quantity);
        System.out.println("Shipped:\t" + this.status);
        System.out.println("--------------------------");
        System.out.println("Books:");
        System.out.println("ISBN\t\tQty.");
            for (Map.Entry<String,Integer> entry : this.orders.entrySet())
                System.out.println(entry.getKey()+"\t"+entry.getValue());
    }
}