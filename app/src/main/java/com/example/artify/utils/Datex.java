package com.example.artify.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Datex {
    public static long startOfDay(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long endOfDay(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startOfDay(millis));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTimeInMillis();
    }

    public static long startOfMonth(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return startOfDay(cal.getTimeInMillis());
    }

    public static long endOfMonth(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startOfMonth(millis));
        cal.add(Calendar.MONTH, 1);
        return cal.getTimeInMillis();
    }

    public static long startOfYear(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return startOfDay(cal.getTimeInMillis());
    }

    public static long endOfYear(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startOfYear(millis));
        cal.add(Calendar.YEAR, 1);
        return cal.getTimeInMillis();
    }

    public static String fmtDateTime(long millis) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(millis));
    }
}
