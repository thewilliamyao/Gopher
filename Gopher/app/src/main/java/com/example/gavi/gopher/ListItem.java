package com.example.gavi.gopher;

/**
 * Created by wyao on 16-04-27.
 */
public class ListItem {
    private String title;
    private Double price;
    private String chefName;
    private String address;
    private Double rating;

    ListItem(String title, Double price, String chefName, String address, Double rating) {
        this.title = title;
        this.price = price;
        this.chefName = chefName;
        this.address = address;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public Double getPrice() { return price; }
    public String getChefName() { return chefName; }
    public String getAddress() { return address; }
    public Double getRating() { return rating; }
}
