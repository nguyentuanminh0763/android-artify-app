package com.example.artify.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artify.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private final SQLiteDatabase db;
    public ProductDao(SQLiteDatabase db) { this.db = db; }

    public long add(Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("description", p.getDescription());
        v.put("price", p.getPrice());
        v.put("image_url", p.getImageUrl());
        v.put("created_at", System.currentTimeMillis());
        return db.insertOrThrow("products", null, v);
    }

    public List<Product> list(String keyword, boolean sortDesc) {
        String where = null; String[] args = null;
        if (keyword != null && keyword.trim().length() > 0) {
            where = "name LIKE ?";
            args = new String[]{"%"+keyword.trim()+"%"};
        }
        String order = "price " + (sortDesc ? "DESC" : "ASC");
        Cursor c = db.query("products", null, where, args, null, null, order);
        try {
            List<Product> out = new ArrayList<>();
            while (c.moveToNext()) {
                Product p = new Product();
                p.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                p.setName(c.getString(c.getColumnIndexOrThrow("name")));
                p.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
                p.setPrice(c.getDouble(c.getColumnIndexOrThrow("price")));
                p.setImageUrl(c.getString(c.getColumnIndexOrThrow("image_url")));
                p.setCreatedAt(c.getLong(c.getColumnIndexOrThrow("created_at")));
                out.add(p);
            }
            return out;
        } finally { c.close(); }
    }

    public Product getById(int id) {
        Cursor c = db.rawQuery(
                "SELECT id,name,description,price,image_url FROM products WHERE id=?",
                new String[]{ String.valueOf(id) });
        try {
            if (c.moveToFirst()) {
                Product p = new Product();
                p.setId(c.getInt(0));
                p.setName(c.getString(1));
                p.setDescription(c.getString(2));
                p.setPrice(c.getDouble(3));
                p.setImageUrl(c.getString(4));
                return p;
            }
            return null;
        } finally {
            c.close();
        }
    }


    public void seedIfEmpty() {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM products", null);
        try {
            if (c.moveToFirst() && c.getInt(0) == 0) {
                // Seed vài sản phẩm dụng cụ vẽ
                add(make("Cọ vẽ ArtMaster S20",
                        "Bộ 10 cây cọ đa kích cỡ.",
                        120000,
                        "https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcRLMkjLywixyiCo8cKoIidfYfRvW20QgnKpApC4TR6SvsUJfYf1GfRs6gKHzQgFqr5Z_mcHMQzzwD2yc7OerRVL_NXADbCcSGGS4vFvPFdW31j8aCtRcPb37w&usqp=CAc"));

                add(make("Bút chì SketchPro 12",
                        "12 cây HB-8B, phác thảo mượt.",
                        85000,
                        "https://marco.com.vn/wp-content/uploads/2024/09/2-11.jpg"));

                add(make("Giấy WaterArt 300gsm",
                        "A4, 50 tờ, chuyên màu nước.",
                        150000,
                        "https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-md8f2lrkd31pa7_tn.webp"));

                add(make("Màu nước Sakura Koi 24",
                        "Tông ấm, dễ hòa trộn.",
                        320000,
                        "https://sakuracolorvn.vn/Data/Sites/1/Product/64/koi_water_color_sketch1.jpg"));

                add(make("Canvas 40x50cm",
                        "Canvas căng khung gỗ thông.",
                        95000,
                        "https://myartstudio.sg/wp-content/uploads/2022/02/16_x_20_Stretched_Canvas_front.jpg.png"));

            }
        } finally { c.close(); }
    }

    /** Cập nhật thông tin sản phẩm theo id (trong đối tượng p). Trả về số dòng bị ảnh hưởng. */
    public int update(Product p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("description", p.getDescription());
        v.put("price", p.getPrice());
        v.put("image_url", p.getImageUrl());
        return db.update("products", v, "id=?", new String[]{ String.valueOf(p.getId()) });
    }

    /** Xoá sản phẩm theo id. Trả về số dòng bị xoá (0 nếu không thấy). */
    public int remove(int id) {
        return db.delete("products", "id=?", new String[]{ String.valueOf(id) });
    }

    private Product make(String n, String d, double price, String url){
        Product p = new Product();
        p.setName(n); p.setDescription(d); p.setPrice(price); p.setImageUrl(url);
        return p;
    }
}
