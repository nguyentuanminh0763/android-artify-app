package com.example.artify.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Fmt {
    public static String vnd(double v) {
        return NumberFormat.getCurrencyInstance(new Locale("vi","VN")).format(v);
    }
}
