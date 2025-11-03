package com.example.artify.models;

public class Review {
    private long id;
    private int productId;
    private String userName;
    private float rating;
    private String comment;
    private long createdAt;

    public Review() {}
    public Review(int productId, String userName, float rating, String comment, long createdAt) {
        this.productId = productId; this.userName = userName;
        this.rating = rating; this.comment = comment; this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
