package edu.uga.cs.roommateshopper.data;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uga.cs.roommateshopper.data.Result;
import edu.uga.cs.roommateshopper.data.model.LoggedInUser;

public class LoginRepository {

    private static volatile LoginRepository instance;
    private final FirebaseAuth mAuth;
    private LoggedInUser user = null;

    private LoginRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            synchronized (LoginRepository.class) {
                if (instance == null) {
                    instance = new LoginRepository();
                }
            }
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void logout() {
        user = null;
        mAuth.signOut();
    }

    public void login(String email, String password, final LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String name = firebaseUser.getDisplayName() != null
                                    ? firebaseUser.getDisplayName()
                                    : firebaseUser.getEmail();
                            user = new LoggedInUser(firebaseUser.getUid(), name);
                            callback.onSuccess(new Result.Success<>(user));
                        } else {
                            callback.onError(new Exception(
                                    task.getException() != null
                                            ? task.getException().getMessage()
                                            : "Authentication failed."
                            ));
                        }
                    }
                });
    }

    public LoggedInUser getLoggedInUser() {
        return user;
    }

    public interface LoginCallback {
        void onSuccess(Result.Success<LoggedInUser> result);
        void onError(Exception e);
    }
}