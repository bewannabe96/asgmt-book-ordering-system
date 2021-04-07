package api;

import java.sql.*;
import java.io.*;

import util.*;
import model.*;

import java.io.File;
import java.util.Scanner;

public class SystemTimeAPI {
    private static String st = null;

    public static void initialize() throws Exception {
        try {
            SystemTimeAPI.st = OrderAPI.selectLatestOrderTime();
        } catch (SQLException e) {
            throw new Exception("Could not initialize system time");
        }
    }

    public static String get() throws Exception {
        if (SystemTimeAPI.st == null)
            throw new Exception("System time has not been initialized");

        return SystemTimeAPI.st;
    }

    public static void set(String time) throws Exception {
        if (SystemTimeAPI.st == null) {
            SystemTimeAPI.st = time;
            return;
        }

        try {
            String latestDate = OrderAPI.selectLatestOrderTime();
            System.out.println("Latest order date: " + latestDate);

            if (FormattedDate.isBefore(time, latestDate)) {
                throw new Exception("New system date must be later than or equal to the latest order date");
            }
    
            SystemTimeAPI.st = time;
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve latest order date");
        }
    }
}