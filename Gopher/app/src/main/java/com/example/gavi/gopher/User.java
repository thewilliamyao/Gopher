package com.example.gavi.gopher;

/**
 * Created by grawson2 on 5/1/16.
 */
public class User {

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public User(String id, String fn, String ln, String email) {
        this.id = id;
        this.firstName = fn;
        this.lastName = ln;
        this.email = email;
    }

    public User(){ }

    public String getID() { return this.id; }
    public String getLastName() { return this.lastName; }
    public String getFirstName() { return this.firstName; }
    public String getEmail() { return this.email; }

}
