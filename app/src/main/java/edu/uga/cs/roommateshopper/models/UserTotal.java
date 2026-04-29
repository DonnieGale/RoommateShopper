package edu.uga.cs.roommateshopper.models;

/**
 * Represents the total spending and settlement status of an individual roommate.
 * This class is used during the settlement process to track how much a roommate
 * has spent and their current balance relative to the average expenditure.
 */
public class UserTotal {
    public String id;
    public String name;
    public double totalSpent;
    public double differenceFromAverage;

    /**
     * Default constructor for the UserTotal class.
     * Required for Firebase data mapping.
     */
    public UserTotal() {}

    /**
     * Constructs a new UserTotal with the specified details.
     *
     * @param id The unique identifier of the user.
     * @param name The name of the roommate.
     * @param totalSpent The total amount spent by this roommate.
     * @param differenceFromAverage The difference between the roommate's spending and the group average.
     */
    public UserTotal(String id, String name, double totalSpent, double differenceFromAverage) {
        this.id = id;
        this.name = name;
        this.totalSpent = totalSpent;
        this.differenceFromAverage = differenceFromAverage;
    }
}
