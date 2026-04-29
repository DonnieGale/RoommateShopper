package edu.uga.cs.roommateshopper.models;

import java.io.Serializable;
import java.util.Map;

public class Purchase implements Serializable {
    public String id;
    public String purchasedBy;
    public String purchasedByName;
    public double totalPrice;
    public long timestamp;

    public Map<String, ShoppingItem> items;

    public Purchase() {}

    public Purchase(String id, String purchasedBy, String purchasedByName,
                    double totalPrice, long timestamp,
                    Map<String, ShoppingItem> items) {
        this.id = id;
        this.purchasedBy = purchasedBy;
        this.purchasedByName = purchasedByName;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.items = items;
    }
}