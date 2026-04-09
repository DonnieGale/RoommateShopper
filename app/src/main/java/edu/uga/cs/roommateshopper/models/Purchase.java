package edu.uga.cs.roommateshopper.models;

import java.util.Map;

public class Purchase {
    public String id; // purchaseId
    public String purchasedBy;
    public String purchasedByName;
    public double totalPrice;
    public long timestamp;

    public Map<String, PurchasedItem> items;

    public Purchase() {}

    public Purchase(String id, String purchasedBy, String purchasedByName,
                    double totalPrice, long timestamp,
                    Map<String, PurchasedItem> items) {
        this.id = id;
        this.purchasedBy = purchasedBy;
        this.purchasedByName = purchasedByName;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.items = items;
    }
}