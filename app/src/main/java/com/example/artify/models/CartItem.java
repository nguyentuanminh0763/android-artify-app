package com.example.artify.models;

/**
 * CartItem dùng cho màn hình giỏ hàng (đã JOIN với bảng products).
 * Chứa cả tên/giá/ảnh để hiển thị nhanh.
 */
public class CartItem {
    private int id;          // cart_items.id
    private int productId;   // products.id
    private int quantity;
    private String name;     // products.name
    private double price;    // products.price
    private String imageUrl; // products.image_url

    public CartItem() { }

    public CartItem(int id, int productId, int quantity, String name, double price, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getLineTotal() { return price * quantity; }

    @Override public String toString() {
        return "CartItem{productId=" + productId + ", qty=" + quantity + ", name='" + name + "'}";
    }
}
