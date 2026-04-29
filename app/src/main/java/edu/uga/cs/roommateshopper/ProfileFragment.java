package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.roommateshopper.models.User;
import edu.uga.cs.roommateshopper.ui.login.LoginFragment;

/**
 * Fragment that displays and manages the user's profile information.
 * This includes displaying the user's name, email, and UID, and allowing the user to update their name.
 */
public class ProfileFragment extends Fragment {

    /**
     * The ID of the current user.
     */
    String user;

    /**
     * Default constructor for ProfileFragment.
     */
    public ProfileFragment() {}

    /**
     * Factory method to create a new instance of ProfileFragment.
     * @return A new instance of ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    /**
     * Called to do initial creation of the fragment.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            String Uid = firebaseUser.getUid();
            String FBEmail = firebaseUser.getEmail();


            String currentName = firebaseUser.getDisplayName() != null ?
                    firebaseUser.getDisplayName() : FBEmail;


            User currentUserObject = new User(Uid, currentName, FBEmail);

            this.user = currentUserObject.getId();

            String UserEmail = currentUserObject.getEmail();

            TextView welcome = view.findViewById(R.id.Welcome);
            EditText Name = view.findViewById(R.id.Name);

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(Uid);
            userRef.child("name").get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String dbName = snapshot.getValue(String.class);
                    welcome.setText("Welcome " + dbName + "!");
                    Name.setText("Name: " + dbName);
                }
            });

            TextView UID = view.findViewById(R.id.UID);
            UID.setText("User ID: " + Uid);
            TextView Email = view.findViewById(R.id.Email);
            Email.setText("Email: " + UserEmail);
            Button button = view.findViewById(R.id.button2);

            button.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();

                getParentFragmentManager().beginTransaction();
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, new LoginFragment()).commit();

            });


            Name.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    saveName(Name.getText().toString(), Uid, welcome);
                    Name.clearFocus();
                }
            });


            Name.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
                    Name.clearFocus();
                    return true;
                }
                return false;
            });
        }

    }

    /**
     * Saves the updated username to Firebase database and authentication profile.
     * @param fullName The full text from the name input field.
     * @param Uid The user's unique identifier.
     * @param welcomeLabel The TextView that displays the welcome message.
     */
    private void saveName(String fullName, String Uid, TextView welcomeLabel) {

        String newName = fullName;
        if (fullName.startsWith("Name: ")) {
            newName = fullName.substring(6).trim();
        }

        if (newName.isEmpty()) return;

        final String finalnewName = newName;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(Uid).child("name").setValue(newName)
                .addOnSuccessListener(aVoid -> {
                    welcomeLabel.setText("Welcome " + finalnewName + "!");
                    android.util.Log.d("ProfileFragment", "Update successful");
                });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            com.google.firebase.auth.UserProfileChangeRequest profileUpdates =
                    new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build();
            user.updateProfile(profileUpdates);
        }
    }
}