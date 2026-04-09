package edu.uga.cs.roommateshopper.models;

import java.util.Map;

public class Settlement {
    public String id; // settlementId
    public long timestamp;
    public double totalCost;
    public double averagePerPerson;
    public int numRoommates;

    public Map<String, UserTotal> perUserTotals;

    public Settlement() {}

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
