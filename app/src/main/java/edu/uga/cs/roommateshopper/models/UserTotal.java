package edu.uga.cs.roommateshopper.models;

public class UserTotal {
    public String id;
    public String name;
    public double totalSpent;
    public double differenceFromAverage;

    public UserTotal() {}

    public UserTotal(String id, String name, double totalSpent, double differenceFromAverage) {
        this.id = id;
        this.name = name;
        this.totalSpent = totalSpent;
        this.differenceFromAverage = differenceFromAverage;
    }
}
