package edu.uga.cs.roommateshopper.models;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a purchase made by a roommate.
 * A purchase consists of multiple shopping items, the total price,
 * the user who made the purchase, and the time it was completed.
 */
public class Purchase implements Serializable {
    public String id;
    public String purchasedBy;
    public String purchasedByName;
    public double totalPrice;
    public long timestamp;

    public Map<String, ShoppingItem> items;

    /**
     * Default constructor for the Purchase class.
     * Required for Firebase data mapping.
     */
    public Purchase() {}

    /**
     * Constructs a new Purchase with the specified details.
     *
     * @param id The unique identifier of the purchase.
     * @param purchasedBy The UID of the user who made the purchase.
     * @param purchasedByName The name of the user who made the purchase.
     * @param totalPrice The total cost of the purchase.
     * @param timestamp The time the purchase was made in milliseconds.
     * @param items A map of shopping items included in this purchase.
     */
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
