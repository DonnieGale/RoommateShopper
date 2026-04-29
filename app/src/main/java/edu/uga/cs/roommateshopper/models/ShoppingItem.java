package edu.uga.cs.roommateshopper.models;

import java.io.Serializable;

public class ShoppingItem implements Serializable {
    public String id;
    public String name;
    public String addedBy;
    public long timestamp;
    public double price;
    public int quantity;

    public ShoppingItem() {}

    public ShoppingItem(String id, String name, String addedBy, long timestamp
            , double price, int quantity) {
        this.id = id;
        this.name = name;
        this.addedBy = addedBy;
        this.timestamp = timestamp;
        this.price = price;
        this.quantity = quantity;
    }
}
