package com.example.artify.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artify.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private final SQLiteDatabase db;
    public ReviewDao(SQLiteDatabase db) { this.db = db; }

    public long add(Review r) {
        ContentValues cv = new ContentValues();
        cv.put("product_id", r.getProductId());
        cv.put("user_name", r.getUserName());
        cv.put("rating", r.getRating());
        cv.put("comment", r.getComment());
        cv.put("created_at", r.getCreatedAt());
        return db.insert("reviews", null, cv);
    }

    public List<Review> listByProduct(int productId) {
        List<Review> out = new ArrayList<>();
        Cursor c = db.rawQuery(
                "SELECT id, user_name, rating, comment, created_at FROM reviews " +
                        "WHERE product_id=? ORDER BY created_at DESC",
                new String[]{String.valueOf(productId)});
        try {
            while (c.moveToNext()) {
                Review r = new Review();
                r.setId(c.getLong(0));
                r.setProductId(productId);
                r.setUserName(c.getString(1));
                r.setRating(c.getFloat(2));
                r.setComment(c.getString(3));
                r.setCreatedAt(c.getLong(4));
                out.add(r);
            }
        } finally { c.close(); }
        return out;
    }

    public float averageRating(int productId) {
        Cursor c = db.rawQuery("SELECT AVG(rating) FROM reviews WHERE product_id=?",
                new String[]{String.valueOf(productId)});
        try { return c.moveToFirst() ? c.getFloat(0) : 0f; }
        finally { c.close(); }
    }

    public int count(int productId) {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM reviews WHERE product_id=?",
                new String[]{String.valueOf(productId)});
        try { return c.moveToFirst() ? c.getInt(0) : 0; }
        finally { c.close(); }
    }

    /** Seed vài review ảo khi sản phẩm chưa có đánh giá */
    public void seedIfEmpty(int productId) {
        if (count(productId) > 0) return;
        long now = System.currentTimeMillis();
        add(new Review(productId, "Lan Anh", 4.5f, "Bút mịn, dễ điều khiển.", now - 86_400_000L));
        add(new Review(productId, "Minh Quân", 5f,   "Đúng mô tả, giao nhanh.", now - 60_000_000L));
        add(new Review(productId, "Hoàng",    4f,   "Ổn trong tầm giá.",       now - 30_000_000L));
    }
}
