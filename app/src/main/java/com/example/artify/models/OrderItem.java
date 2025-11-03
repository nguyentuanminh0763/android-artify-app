package com.example.artify.models;

/** Dòng sản phẩm thuộc 1 đơn hàng */
public class OrderItem {
    private long id;         // order_items.id
    private long orderId;    // order_items.order_id
    private int productId;   // order_items.product_id
    private String name;     // snapshot tên lúc mua
    private double price;    // snapshot giá lúc mua
    private int quantity;    // số lượng

    public OrderItem() {}

    public OrderItem(long id, long orderId, int productId,
                     String name, double price, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters & Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getLineTotal() { return price * quantity; }

    @Override public String toString() {
        return "OrderItem{productId=" + productId + ", qty=" + quantity + "}";
    }
}
