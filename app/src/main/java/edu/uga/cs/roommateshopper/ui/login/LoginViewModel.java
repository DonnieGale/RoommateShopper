package edu.uga.cs.roommateshopper.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uga.cs.roommateshopper.FirebaseDBHelper;
import edu.uga.cs.roommateshopper.R;
import edu.uga.cs.roommateshopper.models.User;

/**
 * ViewModel for the login screen.
 * Handles the authentication logic using Firebase and manages the state of the login form and result.
 */
public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    /**
     * Default constructor for LoginViewModel.
     */
    LoginViewModel() {}

    /**
     * Returns the LiveData object for the login form state.
     * @return LiveData containing LoginFormState.
     */
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    /**
     * Returns the LiveData object for the login result.
     * @return LiveData containing LoginResult.
     */
    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    /**
     * Attempts to log in a user with the provided credentials.
     * If the login fails, it attempts to create a new user account.
     * @param username The user's email address.
     * @param password The user's password.
     */
    public void login(String username, String password) {

        fAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = fAuth.getCurrentUser();

                        if (user != null) {
                            String email = user.getEmail();
                            String name = user.getDisplayName();
                            loginResult.setValue(new LoginResult(new LoggedInUserView(email)));
                            FirebaseDBHelper.getInstance().addUser(new User(user.getUid(), name, email));
                        }

                    } else {

                        fAuth.createUserWithEmailAndPassword(username, password)
                                .addOnCompleteListener(createTask -> {
                                    if (createTask.isSuccessful()) {

                                        FirebaseUser user = fAuth.getCurrentUser();

                                        if (user != null) {
                                            String uid = user.getUid();
                                            String email = user.getEmail();

                                            User newUser = new User();
                                            newUser.id = uid;
                                            newUser.name = email.substring(0, email.indexOf('@'));
                                            newUser.email = email;

                                            FirebaseDBHelper.getInstance().addUser(newUser);

                                            loginResult.setValue(new LoginResult(new LoggedInUserView(email)));
                                        }

                                    } else {
                                        loginResult.setValue(new LoginResult(R.string.login_failed));
                                    }
                                });
                    }
                });
    }

    /**
     * Updates the login form state based on the current input.
     * @param username The current username input.
     * @param password The current password input.
     */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /**
     * Validates if the provided username is a valid email address.
     * @param username The username to validate.
     * @return True if valid, false otherwise.
     */
    private boolean isUserNameValid(String username) {
        return username != null && Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    /**
     * Validates if the provided password meets the minimum length requirement.
     * @param password The password to validate.
     * @return True if valid, false otherwise.
     */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}