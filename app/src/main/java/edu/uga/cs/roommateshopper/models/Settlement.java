package edu.uga.cs.roommateshopper.models;

import java.util.Map;

/**
 * Represents a settlement of shopping expenses among roommates.
 * This class stores the summary of a settlement, including total cost, average cost per person,
 * and the individual totals for each user involved.
 */
public class Settlement {
    /**
     * The unique identifier for the settlement.
     */
    public String id;
    /**
     * The timestamp when the settlement was calculated.
     */
    public long timestamp;
    /**
     * The total cost of all purchases included in this settlement.
     */
    public double totalCost;
    /**
     * The average cost that each person should pay.
     */
    public double averagePerPerson;
    /**
     * The number of roommates participating in the settlement.
     */
    public int numRoommates;

    /**
     * A map of user IDs to their respective total spending in this settlement.
     */
    public Map<String, UserTotal> perUserTotals;

    /**
     * Default constructor for Firebase deserialization.
     */
    public Settlement() {}

    /**
     * Constructs a new Settlement with the specified details.
     * @param id The unique identifier for the settlement.
     * @param timestamp The timestamp of the settlement.
     * @param totalCost The total cost of the purchases.
     * @param averagePerPerson The average cost per person.
     * @param numRoommates The number of roommates.
     * @param perUserTotals A map containing individual user totals.
     */
    public Settlement(String id, long timestamp, double totalCost,
                      double averagePerPerson, int numRoommates,
                      Map<String, UserTotal> perUserTotals) {
        this.id = id;
        this.timestamp = timestamp;
        this.totalCost = totalCost;
        this.averagePerPerson = averagePerPerson;
        this.numRoommates = numRoommates;
        this.perUserTotals = perUserTotals;
    }
}
