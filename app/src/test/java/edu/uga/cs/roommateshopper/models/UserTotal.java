package edu.uga.cs.roommateshopper.models;

public class UserTotal {
    public String id; // uid (optional but helpful)
    public String name;
    public double totalSpent;

    public UserTotal() {}

    public UserTotal(String id, String name, double totalSpent) {
        this.id = id;
        this.name = name;
        this.totalSpent = totalSpent;
    }
}
