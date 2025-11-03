package com.example.artify.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artify.models.CartItem;
import com.example.artify.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private final SQLiteDatabase db;

    public CartDao(SQLiteDatabase db) { this.db = db; }

    /** Thêm 1 sản phẩm (qty +1). Dùng upsert nếu có, fallback nếu không. */
    public void add(Product p) {

        try {
            db.execSQL(
                    "INSERT INTO cart_items(product_id, name, price, image_url, quantity) " +
                            "VALUES (?, ?, ?, ?, 1) " +
                            "ON CONFLICT(product_id) DO UPDATE SET quantity = quantity + 1",
                    new Object[]{p.getId(), p.getName(), p.getPrice(), p.getImageUrl()}
            );
            return;
        } catch (Exception ignored) { /* rơi về cách B */ }

        // Cách B: insert or update (tương thích rộng)
        db.beginTransaction();
        try (Cursor c = db.rawQuery(
                "SELECT id, quantity FROM cart_items WHERE product_id=?",
                new String[]{String.valueOf(p.getId())})) {
            if (c.moveToFirst()) {
                int id = c.getInt(0);
                int newQty = c.getInt(1) + 1;
                ContentValues v = new ContentValues();
                v.put("quantity", newQty);
                db.update("cart_items", v, "id=?", new String[]{String.valueOf(id)});
            } else {
                ContentValues v = new ContentValues();
                v.put("product_id", p.getId());
                v.put("name", p.getName());
                v.put("price", p.getPrice());
                v.put("image_url", p.getImageUrl());
                v.put("quantity", 1);
                db.insertWithOnConflict("cart_items", null, v, SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /** Lấy toàn bộ items trong giỏ. */
    public List<CartItem> getAll() {
        List<CartItem> list = new ArrayList<>();
        try (Cursor c = db.rawQuery(
                "SELECT id, product_id, name, price, image_url, quantity FROM cart_items ORDER BY id DESC",
                null)) {
            while (c.moveToNext()) {
                list.add(new CartItem(
                        c.getInt(0),     // id
                        c.getInt(1),     // productId
                        c.getInt(5),     // quantity
                        c.getString(2),  // name
                        c.getDouble(3),  // price
                        c.getString(4)   // imageUrl
                ));
            }
        }
        return list;
    }

    /** Tăng số lượng 1 item theo id. */
    public void increase(int id) {
        db.execSQL("UPDATE cart_items SET quantity = quantity + 1 WHERE id=?", new Object[]{id});
    }

    /** Giảm 1: nếu còn >1 thì -1; nếu =1 thì xoá. Không cần đọc rồi viết. */
    public void decreaseOrRemove(int id) {
        db.execSQL("UPDATE cart_items SET quantity = quantity - 1 WHERE id=? AND quantity > 1", new Object[]{id});
        // Nếu sau update không đổi (tức quantity không >1), xoá luôn:
        db.delete("cart_items", "id=? AND quantity <= 1", new String[]{String.valueOf(id)});
    }

    /** Xoá 1 item theo id. */
    public void remove(int id) {
        db.delete("cart_items", "id=?", new String[]{String.valueOf(id)});
    }

    /** Xoá theo productId (dùng khi xoá sản phẩm khỏi catalog). */
    public int removeByProductId(int productId) {
        return db.delete("cart_items", "product_id=?", new String[]{String.valueOf(productId)});
    }

    /** Tổng tiền giỏ. */
    public double getTotal() {
        try (Cursor c = db.rawQuery("SELECT IFNULL(SUM(price * quantity),0) FROM cart_items", null)) {
            return c.moveToFirst() ? c.getDouble(0) : 0d;
        }
    }

    /** Tổng số lượng item (để hiển thị badge). */
    public int getItemCount() {
        try (Cursor c = db.rawQuery("SELECT IFNULL(SUM(quantity),0) FROM cart_items", null)) {
            return c.moveToFirst() ? c.getInt(0) : 0;
        }
    }

    /** Xoá toàn bộ giỏ. */
    public void clear() {
        db.delete("cart_items", null, null);
    }
}
