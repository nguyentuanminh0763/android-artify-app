package com.example.artify.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
    private final SQLiteDatabase db;

    public UserDao(SQLiteDatabase db) {
        this.db = db;
    }

    public long insert(String fullName, String email, String passwordHash) {
        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("email", email.toLowerCase());
        values.put("password_hash", passwordHash);
        values.put("created_at", System.currentTimeMillis());
        return db.insertOrThrow("users", null, values);
    }

    public Cursor findByEmail(String email) {
        return db.query(
                "users",
                new String[]{"id", "password_hash"},
                "email=?",
                new String[]{email.toLowerCase()},
                null,
                null,
                null
        );
    }
}
