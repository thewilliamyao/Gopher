package com.example.gavi.gopher;

/**
 * Created by grawson2 on 5/4/16.
 */
public class Meal {

    private String title;
    private double price;
    private String description;
    private String address;

    public Meal(String t, double p, String d, String a) {
        this.title = t;
        this.description = d;
        this.price = p;
        this.address = a;
    }

    public Meal() { }

    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public double getPrice() { return this.price; }

    public String getAddress() {
        return address;
    }
}
