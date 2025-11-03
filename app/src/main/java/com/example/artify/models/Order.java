package com.example.artify.models;

/** Đơn hàng */
public class Order {
    private long id;
    private String customerName; // orders.customer_name
    private String phone;        // orders.phone
    private String address;      // orders.address
    private String note;         // orders.note
    private double total;        // orders.total
    private long createdAt;      // orders.created_at (millis)

    public Order() {}

    public Order(long id, String customerName, String phone, String address,
                 String note, double total, long createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.note = note;
        this.total = total;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    @Override public String toString() {
        return "Order{id=" + id + ", total=" + total + ", at=" + createdAt + "}";
    }
}
