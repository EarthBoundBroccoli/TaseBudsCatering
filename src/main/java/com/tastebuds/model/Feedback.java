package com.tastebuds.model;

public class Feedback {
    public int orderId;
    public int rating;
    public String comments;

    public Feedback(int orderId, int rating, String comments) {
        this.orderId = orderId;
        this.rating = rating;
        this.comments = comments;
    }
}