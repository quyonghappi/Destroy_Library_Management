package com.library.utils;

//convert a string to LocalDate.
//public class DateFormat {
//    DateFormat() {}
//    private static  String pattern = "yyyy-MM-dd";
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
//
//    public static LocalDate convert(String date){
//        try {
//            return LocalDate.parse(date, formatter);
//        } catch (DateTimeParseException e) {
//            System.out.println("Invalid date format. Please use " + pattern + ".");
//            return null;
//        }
//    }
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateFormat {


     //convert LocalDate to java.sql.Date
    public static Date toSqlDate(LocalDate localDate) {
        return (localDate != null) ? Date.valueOf(localDate) : null;
    }


     //convert java.sql.Date to LocalDate
    public static LocalDate toLocalDate(Date sqlDate) {
        return (sqlDate != null) ? sqlDate.toLocalDate() : null;
    }


     //convert LocalDateTime to java.sql.Timestamp
    public static Timestamp toSqlTimestamp(LocalDateTime localDateTime) {
        return (localDateTime != null) ? Timestamp.valueOf(localDateTime) : null;
    }


     //convert java.sql.Timestamp to LocalDate
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toLocalDateTime() : null;
    }
}
