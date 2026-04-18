package edu.uga.cs.roommateshopper.models;

public class PurchasedItem {
    public String id; // itemId (optional but useful)
    public String name;
    public double price;

    public PurchasedItem() {}

    public PurchasedItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
