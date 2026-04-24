package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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


    }

    private void getRoommates(String groupId) {
        mDatabase.child("groups").child(groupId).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roommatesList.removeAllViews(); // Clear current list
                for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                    String memberUid = memberSnapshot.getKey();
                    if (memberUid != null) {
                        fetchMemberName(memberUid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchMemberName(String uid) {
        mDatabase.child("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.getValue(String.class);
                    addRoommateToUI(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void addRoommateToUI(String name) {
        if (getContext() == null) return;
        TextView textView = new TextView(getContext());
        textView.setText(name);
        textView.setTextSize(16);
        textView.setPadding(0, 8, 0, 8);
        roommatesList.addView(textView);
    }
}