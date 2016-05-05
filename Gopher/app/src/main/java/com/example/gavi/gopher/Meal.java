package com.example.gavi.gopher;

/**
 * Created by grawson2 on 5/4/16.
 */
public class Meal {

    private String title;
    private double price;
    private String description;
    private double xCoordinate;
    private double yCoordinate;

    public Meal(String t, double p, String d, double x, double y) {
        this.title = t;
        this.description = d;
        this.price = p;
        this.yCoordinate = y;
        this.xCoordinate = x;
    }

    public Meal() { }

    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public double getPrice() { return this.price; }
    public double getxCoordinate() { return this.xCoordinate; }
    public double getyCoordinate() { return yCoordinate; }
}
