package edu.uga.cs.roommateshopper.models;

public class ShoppingItem {
    public String id; // itemId
    public String name;
    public String addedBy;
    public long timestamp;

    public ShoppingItem() {}

    public ShoppingItem(String id, String name, String addedBy, long timestamp) {
        this.id = id;
        this.name = name;
        this.addedBy = addedBy;
        this.timestamp = timestamp;
    }
}
