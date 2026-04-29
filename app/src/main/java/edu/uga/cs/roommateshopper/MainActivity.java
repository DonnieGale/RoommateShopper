package edu.uga.cs.roommateshopper;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uga.cs.roommateshopper.ui.login.LoginFragment;

/**
 * The main activity of the application that handles the initial screen navigation.
 * It determines whether to show the login screen or the splash screen based on the user's authentication state.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Tag used for logging purposes.
     */
    public static final String TAG = "RoommateShopper";
    
    private FirebaseAuth mAuth;

    /**
     * Called when the activity is starting. Initializes the UI and checks the authentication state.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (savedInstanceState == null) {

            if (currentUser == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, new LoginFragment())
                        .commit();
            } else {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, new Splash())
                        .commit();
            }


        }
    }

}