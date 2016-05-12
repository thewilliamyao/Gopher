package com.example.gavi.gopher;

/**
 * Created by wyao on 16-04-27.
 */
public class ListItem {
    private String title;
    private Double price;
    private String chefName;
    private String address;
//    private Double rating;
    private String distance;
    private String id;
//    private boolean isDairyFree;
//    private boolean isGlutenFree;
//    private boolean isNutFree;

    ListItem(String title, Double price, String chefName, String address, String distance, String id) {
        this.title = title;
        this.price = price;
        this.chefName = chefName;
        this.address = address;
//        this.rating = rating;
        this.distance = distance;
        this.id = id;
//        this.isDairyFree = isDairyFree;
//        this.isGlutenFree = isGlutenFree;
//        this.isNutFree = isNutFree;
    }

    public String getTitle() { return title; }
    public Double getPrice() { return price; }
    public String getChefName() { return chefName; }
    public String getAddress() { return address; }
//    public Double getRating() { return rating; }
    public String getDistance() { return distance; }
    public String getId() { return id; }
//    public boolean getDairy() { return isDairyFree; }
//    public boolean getGluten() { return isGlutenFree; }
//    public boolean getNut() { return isNutFree; }
}
