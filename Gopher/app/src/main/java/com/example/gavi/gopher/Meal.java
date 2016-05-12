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
    private String buyerID;
    private boolean ready;
    private boolean bought;

    public Meal(String t, double p, String d, String a, String i, String sellerID, String buyerID) {
        this.title = t;
        this.description = d;
        this.price = p;
        this.address = a;
        this.id = i;
        this.sellerID = sellerID;
        this.bought = false;
        this.ready = false;
        this.buyerID = buyerID;
    }

    public Meal() { }

    public String getBuyerID() {
        return buyerID;
    }

    public String getId() {
        return id;
    }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public double getPrice() { return this.price; }
    public String getSellerID() {
        return sellerID;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isBought() {
        return bought;
    }

    public String getAddress() {
        return address;
    }
}
