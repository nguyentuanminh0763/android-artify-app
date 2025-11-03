package com.example.artify.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.example.artify.dao.UserDao;

public class AuthService {
    private final UserDao userDao;

    public AuthService(SQLiteDatabase db) {
        userDao = new UserDao(db);
    }

    public long register(String name, String email, String password) {
        return userDao.insert(name, email, sha256(password));
    }

    public boolean login(String email, String password) {
        Cursor cursor = userDao.findByEmail(email);
        try {
            if (cursor.moveToFirst()) {
                String hash = cursor.getString(1);
                return hash.equals(sha256(password));
            }
            return false;
        } finally {
            cursor.close();
        }
    }

    private static String sha256(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
