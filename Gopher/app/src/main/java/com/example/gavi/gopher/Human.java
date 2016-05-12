package com.example.gavi.gopher;

/**
 * Created by Suyi on 4/28/16.
 */
public class Human {
    private String name;
    private String address;
    private String email;
    private String distance;
    private String id;
//    private boolean isDairyFree;
//    private boolean isGlutenFree;
//    private boolean isNutFree;

    Human(String name, String address, String rating, String distance, String id) {
        this.name = name;
        this.address = address;
        this.email = rating;
        this.distance = distance;
        this.id = id;
//        this.isDairyFree = isDairyFree;
//        this.isGlutenFree = isGlutenFree;
//        this.isNutFree = isNutFree;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getDistance() { return distance; }
    public String getId() { return id; }
//    public boolean getDairy() { return isDairyFree; }
//    public boolean getGluten() { return isGlutenFree; }
//    public boolean getNut() { return isNutFree; }
}
