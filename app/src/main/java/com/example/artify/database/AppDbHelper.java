package com.example.artify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "artify.db";
    private static final int DB_VERSION = 9; // ⬅️ bump version

    public AppDbHelper(Context c) { super(c, DB_NAME, null, DB_VERSION); }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Bật ràng buộc khoá ngoại cho SQLite trên Android
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT," +
                "email TEXT UNIQUE," +
                "password_hash TEXT," +
                "created_at INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS products(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL CHECK(price>=0)," +
                "image_url TEXT," +
                "created_at INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS cart_items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "image_url TEXT," +
                "quantity INTEGER NOT NULL CHECK(quantity>0)," +
                "UNIQUE(product_id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS orders(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_name TEXT NOT NULL," +
                "phone TEXT," +
                "address TEXT," +
                "note TEXT," +
                "total REAL NOT NULL," +
                "created_at INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS order_items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS reviews(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id INTEGER NOT NULL," +
                "user_name TEXT NOT NULL," +
                "rating REAL NOT NULL CHECK(rating>=0 AND rating<=5)," +
                "comment TEXT," +
                "created_at INTEGER NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Chiến lược dev: drop & recreate để tránh xung đột schema cũ
        db.beginTransaction();
        try {
            db.execSQL("DROP TABLE IF EXISTS order_items");
            db.execSQL("DROP TABLE IF EXISTS orders");
            db.execSQL("DROP TABLE IF EXISTS cart_items");
            db.execSQL("DROP TABLE IF EXISTS products");
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS reviews");   // ⬅️ thêm
            onCreate(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
