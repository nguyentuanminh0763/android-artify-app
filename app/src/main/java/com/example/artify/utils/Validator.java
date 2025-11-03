package com.example.artify.utils;

import android.util.Patterns;

public class Validator {

    public static boolean isEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean notEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
