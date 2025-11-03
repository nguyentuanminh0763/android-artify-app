package com.example.artify.utils;

import android.content.Context;

public class Prefs {

    public static void saveRemember(Context c, String email, String pwd) {
        c.getSharedPreferences("auth", 0)
                .edit()
                .putString("email", email)
                .putString("pwd", pwd)
                .apply();
    }

    public static String getEmail(Context c) {
        return c.getSharedPreferences("auth", 0)
                .getString("email", "");
    }

    public static String getPwd(Context c) {
        return c.getSharedPreferences("auth", 0)
                .getString("pwd", "");
    }
}
