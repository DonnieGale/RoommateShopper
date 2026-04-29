package edu.uga.cs.roommateshopper.ui.login;

import androidx.annotation.Nullable;

/**
 * Result of the login process, containing either the successful user details or an error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    /**
     * Constructs a LoginResult with an error message.
     * @param error The resource ID of the error message.
     */
    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    /**
     * Constructs a LoginResult with successful user details.
     * @param success The view model representing the logged-in user.
     */
    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    /**
     * Returns the successful login details.
     * @return The LoggedInUserView if successful, otherwise null.
     */
    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    /**
     * Returns the error message resource ID.
     * @return The error ID if login failed, otherwise null.
     */
    @Nullable
    Integer getError() {
        return error;
    }
}