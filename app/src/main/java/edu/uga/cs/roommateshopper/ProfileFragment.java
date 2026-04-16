package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    String user;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
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
            String UserName = currentUserObject.getName();
            String UserEmail = currentUserObject.getEmail();

            TextView welcome = view.findViewById(R.id.Welcome);
            welcome.setText("Welcome " + UserName + "!");
            TextView UID = view.findViewById(R.id.UID);
            UID.setText("User ID: " + Uid);
            TextView Email = view.findViewById(R.id.Email);
            Email.setText("Email: " + UserEmail);
            EditText Name = view.findViewById(R.id.Name);
            Name.setText("Name: " + UserName);
            Button button = view.findViewById(R.id.button2);

            button.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();

                //getParentFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getParentFragmentManager().beginTransaction();
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, new LoginFragment()).commit();

            });


            // Focus Listener
            Name.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    saveName(Name.getText().toString(), Uid, welcome);
                }
            });

            // Enter Key Listener
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
    private void saveName(String fullName, String Uid, TextView welcomeLabel) {
        // Safety check: if user deleted the "Name: " prefix
        String newName = fullName;
        if (fullName.startsWith("Name: ")) {
            newName = fullName.substring(6).trim();
        }

        if (newName.isEmpty()) return;

        final String finalnewName = newName;

        // Update Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(Uid).child("name").setValue(newName)
                .addOnSuccessListener(aVoid -> {
                    welcomeLabel.setText("Welcome " + finalnewName + "!");
                    android.util.Log.d("ProfileFragment", "Update successful");
                });

        // Also update Firebase Auth Profile so it stays in sync
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