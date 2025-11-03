package com.example.artify.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artify.models.CartItem;
import com.example.artify.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    private final SQLiteDatabase db;
    public OrderDao(SQLiteDatabase db) { this.db = db; }

    /** Tạo order từ cart_items, trả về orderId; đồng thời clear giỏ. */
    public long placeOrder(String name, String phone, String address, String note, List<CartItem> cart) {
        if (cart == null || cart.isEmpty()) return -1;

        double total = 0;
        for (CartItem it : cart) total += it.getLineTotal();

        db.beginTransaction();
        try {
            // insert order
            ContentValues o = new ContentValues();
            o.put("customer_name", name);
            o.put("phone", phone);
            o.put("address", address);
            o.put("note", note);
            o.put("total", total);
            o.put("created_at", System.currentTimeMillis());
            long orderId = db.insertOrThrow("orders", null, o);

            // insert order_items
            for (CartItem it : cart) {
                ContentValues oi = new ContentValues();
                oi.put("order_id", orderId);
                oi.put("product_id", it.getProductId());
                oi.put("name", it.getName());
                oi.put("price", it.getPrice());
                oi.put("quantity", it.getQuantity());
                db.insertOrThrow("order_items", null, oi);
            }

            // clear cart
            db.delete("cart_items", null, null);

            db.setTransactionSuccessful();
            return orderId;
        } finally {
            db.endTransaction();
        }
    }

    /** Tổng doanh thu (all time) */
    public double getTotalRevenue() {
        Cursor c = db.rawQuery("SELECT IFNULL(SUM(total),0) FROM orders", null);
        double sum = 0;
        if (c.moveToFirst()) sum = c.getDouble(0);
        c.close();
        return sum;
    }

    /** Lịch sử đơn hàng (newest first) kèm count items */
    public List<OrderRow> listOrders() {
        String sql = "SELECT o.id, o.customer_name, o.total, o.created_at, " +
                "(SELECT IFNULL(SUM(quantity),0) FROM order_items oi WHERE oi.order_id=o.id) AS item_count " +
                "FROM orders o ORDER BY o.created_at DESC";
        Cursor c = db.rawQuery(sql, null);
        List<OrderRow> list = new ArrayList<>();
        while (c.moveToNext()) {
            OrderRow r = new OrderRow();
            r.id = c.getInt(0);
            r.customer = c.getString(1);
            r.total = c.getDouble(2);
            r.createdAt = c.getLong(3);
            r.itemCount = c.getInt(4);
            list.add(r);
        }
        c.close();
        return list;
    }

    /** Row đơn giản cho màn lịch sử */
    public static class OrderRow {
        public int id;
        public String customer;
        public double total;
        public long createdAt;
        public int itemCount;
    }
}
