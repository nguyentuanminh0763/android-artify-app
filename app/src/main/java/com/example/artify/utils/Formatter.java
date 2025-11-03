package com.example.artify.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {
    private static final NumberFormat NF = NumberFormat.getInstance(new Locale("vi","VN"));
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi","VN"));

    public static String vnd(double v) {
        return NF.format(Math.round(v)) + " đ";
    }

    public static String dateTime(long millis) {
        return DF.format(new Date(millis));
    }
}
