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

public class RoommateFragment extends Fragment {

    private DatabaseReference mDatabase;

    LinearLayout roommatesList;

    public RoommateFragment() {}

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

        return inflater.inflate(R.layout.fragment_roommate, container, false);
    }

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


    private void addRoommateToUI(String name) {
        if (getContext() == null) return;
        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setTextSize(18);
        textView.setPadding(16, 16, 16, 16);
        roommatesList.addView(textView);
    }

}