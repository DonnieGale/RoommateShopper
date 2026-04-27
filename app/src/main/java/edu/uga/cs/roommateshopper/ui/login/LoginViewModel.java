package edu.uga.cs.roommateshopper.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.roommateshopper.FirebaseDBHelper;
import edu.uga.cs.roommateshopper.R;
import edu.uga.cs.roommateshopper.models.User;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseDBHelper db = FirebaseDBHelper.getInstance();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

    private String name;
    LoginViewModel() {}

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

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
                        // Create new user if login fails
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



    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username != null && Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}