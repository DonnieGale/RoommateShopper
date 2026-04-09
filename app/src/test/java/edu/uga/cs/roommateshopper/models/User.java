package edu.uga.cs.roommateshopper.models;

public class User {
    public String id; // Firebase key (uid)
    public String name;
    public String email;

    public User() {}

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
