package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateGroupFragment extends DialogFragment {


    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private TextView groupID;

    public CreateGroupFragment() {
        // Required empty public constructor
    }


    public static CreateGroupFragment newInstance() {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupID = view.findViewById(R.id.GroupIDTEXT);
        createNewGroup();

    }

    private void createNewGroup() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        String newGroupId = mDatabase.child("groups").push().getKey();

        if (newGroupId != null) {
            // Shorten the ID to make it easier to share
            String shortId = newGroupId.substring(0, 6).toUpperCase();

            // 2. Join this newly created group automatically
            joinGroup(shortId);

            // 3. Display the ID
            groupID.setText(shortId);

            Toast.makeText(getContext(),
                    "Group Created! Share this ID: " + shortId,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void joinGroup(String groupIdInput) {
        if (mUser == null) return;
        String uid = mUser.getUid();

        // 1. Assign the group ID to the user
        mDatabase.child("users").child(uid).child("groupId").setValue(groupIdInput);

        // 2. Add the user to the group's members list
        mDatabase.child("groups").child(groupIdInput).child("members").child(uid).setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Joined group: " + groupIdInput, Toast.LENGTH_SHORT).show();
                });
    }
}