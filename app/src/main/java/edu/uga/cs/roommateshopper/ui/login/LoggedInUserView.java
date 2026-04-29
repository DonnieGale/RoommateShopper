package edu.uga.cs.roommateshopper.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 * This class is used to store the user's display name for presentation in the login interface.
 */
class LoggedInUserView {
    private String displayName;

    /**
     * Constructs a new LoggedInUserView with the given display name.
     * @param displayName The name to be displayed in the UI.
     */
    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the user's display name.
     * @return The display name string.
     */
    String getDisplayName() {
        return displayName;
    }
}