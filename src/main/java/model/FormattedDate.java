package model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FormattedDate {
    public static boolean validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean validateMonth(String month) {
        try {
            LocalDate.parse(date + "-01");
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
}