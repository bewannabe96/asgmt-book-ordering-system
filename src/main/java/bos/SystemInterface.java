package bos;

import java.sql.*;
import java.io.FileNotFoundException;

import util.*;
import api.*;
import model.*;

public class SystemInterface {
    public static void run () {
        boolean running = true;
        while(running) {
            switch(printMenu()) {
                case 1:
                    SystemInterface.performCreateTable();
                    break;

                case 2:
                    SystemInterface.performDeleteTable();
                    break;

                case 3:
                    SystemInterface.performInsertData();
                    break;

                case 4:
                    SystemInterface.performSetSystemDate();
                    break;

                case 5:
                    running = false;
                    break;
            }
        };
    }

    public static void showSystemDate() {
        SystemInterface.performShowSystemDate();
    }

    private static int printMenu() {
        System.out.println("\n<System Interface>");
        System.out.println("------------------");
        System.out.println("1. Create Tables");
        System.out.println("2. Delete Tables");
        System.out.println("3. Insert Data");
        System.out.println("4. Set System Date");
        System.out.println("5. Back");
        return UserInput.getChoice(5);
    }

    private static void performCreateTable() {
        try {
            SchemaAPI.createTables();
            System.out.println("Successfully created tables");
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to create tables");
        }
    }

    private static void performDeleteTable() {
        try {
            SchemaAPI.deleteTables();
            System.out.println("Successfully deleted tables");
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to delete tables");
        }
    }

    private static void performInsertData() {
        String path = UserInput.getString("Folder Path: ");

        try {
            SchemaAPI.loadData(path);
            System.out.println("Successfully loaded data");
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to load data");
        } catch(FileNotFoundException e) {
            System.out.println("[Error]: File not found");
        } catch(Exception e) {
            System.out.println("[Error]: " + e.getMessage());
        }

    }

    private static void performSetSystemDate() {
        String date = UserInput.getString("Date(YYYYMMDD): ");

        if(!FormattedDate.validateDate(date)) {
            System.out.println("[Error]: Date format is invalid");
            return;
        }

        String latestDate = "";
        String today = "";

        // TODO: get latestdate and today's date

        System.out.println("Latest date in orders: " + latestDate);
        System.out.println("Today is " + today);
    }

    private static void performShowSystemDate() {
        try {
            String systemDate = SchemaAPI.selectSystemTime();
            System.out.println("System Date: " + systemDate);
        } catch(SQLException e) {
            System.out.println("[Error]: Failed to retrieve system date");
        }
    }
}