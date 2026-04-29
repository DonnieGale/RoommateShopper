package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Fragment that displays a list of all roommates registered in the application.
 * It fetches user data from the Firebase database and dynamically adds them to the UI.
 */
public class RoommateFragment extends Fragment {

    private DatabaseReference mDatabase;

    LinearLayout roommatesList;

    /**
     * Default constructor for the RoommateFragment.
     */
    public RoommateFragment() {}

    /**
     * Static factory method to create a new instance of the RoommateFragment.
     *
     * @return A new instance of fragment RoommateFragment.
     */
    public static RoommateFragment newInstance() {
        RoommateFragment fragment = new RoommateFragment();
        return fragment;
    }

    /**
     * Called when the fragment is first created.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_roommate, container, false);
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes the Firebase database reference and sets up a listener to display roommates.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        roommatesList = view.findViewById(R.id.roommatesList);

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roommatesList.removeAllViews();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);

                    String displayLabel = (name != null && !name.isEmpty()) ? name : email;

                    if (displayLabel != null) {
                        addRoommateToUI(displayLabel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RoommateFragment", "Error fetching users: " + error.getMessage());
            }
        });
    }

    /**
     * Programmatically adds a roommate's name to the linear layout in the UI.
     *
     * @param name The name of the roommate to display.
     */
    private void addRoommateToUI(String name) {
        if (getContext() == null) return;
        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 16);
        roommatesList.addView(textView);
    }

}
