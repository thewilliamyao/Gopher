package com.example.gavi.gopher;

/**
 * Created by grawson2 on 5/4/16.
 */
public class Meal {

    private String title;
    private double price;
    private String description;
    private String address;
    private String id;
    private String sellerID;

    public Meal(String t, double p, String d, String a, String i, String sellerID) {
        this.title = t;
        this.description = d;
        this.price = p;
        this.address = a;
        this.id = i;
        this.sellerID = sellerID;
    }

    public Meal() { }

    public String getId() {
        return id;
    }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public double getPrice() { return this.price; }

    public String getSellerID() {
        return sellerID;
    }

    public String getAddress() {
        return address;
    }
}
