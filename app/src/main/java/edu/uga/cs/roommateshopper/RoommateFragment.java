package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RoommateFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    LinearLayout roommatesList;

    public RoommateFragment() {
        // Required empty public constructor
    }

    public static RoommateFragment newInstance() {
        RoommateFragment fragment = new RoommateFragment();
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
        return inflater.inflate(R.layout.fragment_roommate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        roommatesList = view.findViewById(R.id.roommatesList);

        // Read roommates from Firebase
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


    private void addRoommateToUI(String name) {
        if (getContext() == null) return;
        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 16);
        roommatesList.addView(textView);
    }

}