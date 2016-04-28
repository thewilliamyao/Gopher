package com.example.gavi.gopher;

/**
 * Created by Suyi on 4/28/16.
 */
public class Human {
    private String name;
    private String address;
    private Double rating;
    private Double distance;
    private boolean isDairyFree;
    private boolean isGlutenFree;
    private boolean isNutFree;

    Human(String name, String address, Double rating, Double distance, boolean isDairyFree, boolean isGlutenFree, boolean isNutFree) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.distance = distance;
        this.isDairyFree = isDairyFree;
        this.isGlutenFree = isGlutenFree;
        this.isNutFree = isNutFree;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public Double getRating() { return rating; }
    public Double getDistance() { return distance; }
    public boolean getDairy() { return isDairyFree; }
    public boolean getGluten() { return isGlutenFree; }
    public boolean getNut() { return isNutFree; }
}
