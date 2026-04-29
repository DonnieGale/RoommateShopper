package edu.uga.cs.roommateshopper.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 * This class holds the error messages for the username and password fields and a flag indicating if the data is valid.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    /**
     * Constructs a LoginFormState with specific errors for username and password.
     * @param usernameError The resource ID of the username error message, or null if no error.
     * @param passwordError The resource ID of the password error message, or null if no error.
     */
    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    /**
     * Constructs a LoginFormState indicating whether the overall form data is valid.
     * @param isDataValid True if the form data is valid, false otherwise.
     */
    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    /**
     * Returns the error message resource ID for the username field.
     * @return The error message ID, or null if there is no error.
     */
    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    /**
     * Returns the error message resource ID for the password field.
     * @return The error message ID, or null if there is no error.
     */
    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    /**
     * Returns whether the form data is currently valid.
     * @return True if the form data is valid, false otherwise.
     */
    boolean isDataValid() {
        return isDataValid;
    }
}