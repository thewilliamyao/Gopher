package com.example.gavi.gopher;

/**
 * Created by grawson2 on 5/1/16.
 */
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String mealBuyingID;
    private String mealSellingID;
    private String profilePic;

    public User(String id, String fn, String ln, String email, String a,  String bid, String sid, String p) {
        this.id = id;
        this.firstName = fn;
        this.lastName = ln;
        this.email = email;
        this.address = a;
        this.profilePic = p;
        this.mealBuyingID = bid;
        this.mealSellingID = sid;
    }

    public User(){ }

    public String getProfilePic() {
        return profilePic;
    }

    public String getId() {
        return id;
    }
    public String getLastName() { return this.lastName; }
    public String getFirstName() { return this.firstName; }
    public String getEmail() { return this.email; }
    public String getAddress() { return address; }

    public String getMealBuyingID() {
        return mealBuyingID;
    }

    public String getMealSellingID() {
        return mealSellingID;
    }
}
