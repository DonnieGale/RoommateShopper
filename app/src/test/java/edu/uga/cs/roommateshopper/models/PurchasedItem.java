package edu.uga.cs.roommateshopper.models;

public class PurchasedItem {
    public String id; // itemId (optional but useful)
    public String name;

    public PurchasedItem() {}

    public PurchasedItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
