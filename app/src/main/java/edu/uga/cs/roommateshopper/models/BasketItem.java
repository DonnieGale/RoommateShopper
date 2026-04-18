package edu.uga.cs.roommateshopper.models;

public class BasketItem {
    public String id;
    public String name;
    public long timestamp;
    public double price;

    public BasketItem() {}

    public BasketItem(String id, String name, long timestamp, double price) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.price = price;
    }
}
