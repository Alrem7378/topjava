package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
    public static<T extends Comparable<T>> boolean isBetween(T ldt, T start, T end) {
        //return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
        return ldt.compareTo(start) >= 0 && ldt.compareTo(end) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

