package edu.uga.cs.roommateshopper.models;

import java.io.Serializable;

/**
 * Represents an item in the shopping list.
 * This class contains details about the item such as its name, the user who added it,
 * the time it was added, its price, and the quantity.
 */
public class ShoppingItem implements Serializable {
    /**
     * The unique identifier for the shopping item.
     */
    public String id;
    /**
     * The name of the shopping item.
     */
    public String name;
    /**
     * The ID of the user who added the item.
     */
    public String addedBy;
    /**
     * The timestamp when the item was added.
     */
    public long timestamp;
    /**
     * The price of the shopping item.
     */
    public double price;
    /**
     * The quantity of the shopping item.
     */
    public int quantity;

    /**
     * Default constructor for Firebase deserialization.
     */
    public ShoppingItem() {}

    /**
     * Constructs a new ShoppingItem with the specified details.
     * @param id The unique identifier for the item.
     * @param name The name of the item.
     * @param addedBy The ID of the user who added the item.
     * @param timestamp The timestamp when the item was added.
     * @param price The price of the item.
     * @param quantity The quantity of the item.
     */
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
