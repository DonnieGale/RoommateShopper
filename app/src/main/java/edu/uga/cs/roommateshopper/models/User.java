package edu.uga.cs.roommateshopper.models;

/**
 * Represents a user in the system.
 * This class holds the user's unique identifier, name, and email address.
 */
public class User {
    /**
     * The unique identifier for the user.
     */
    public String id;
    /**
     * The name of the user.
     */
    public String name;
    /**
     * The email address of the user.
     */
    public String email;

    /**
     * Default constructor for Firebase deserialization.
     */
    public User() {}

    /**
     * Constructs a new User with the specified details.
     * @param id The unique identifier for the user.
     * @param name The name of the user.
     * @param email The email address of the user.
     */
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the user's unique identifier.
     * @return The user ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the user.
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the email address of the user.
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }
}
