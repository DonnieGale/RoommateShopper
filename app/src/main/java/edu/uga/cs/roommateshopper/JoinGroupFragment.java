package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class JoinGroupFragment extends DialogFragment {

    EditText groupID;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    Button join;

    public JoinGroupFragment() {
        // Required empty public constructor
    }


    public static JoinGroupFragment newInstance() {
        JoinGroupFragment fragment = new JoinGroupFragment();
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
        return inflater.inflate(R.layout.fragment_join_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        join = view.findViewById(R.id.button3);
        groupID = view.findViewById(R.id.editTextText);

        join.setOnClickListener(v -> {
            String groupId = groupID.getText().toString().trim();
            if (!groupId.isEmpty()) {
                joinGroup(groupId);
            }
            dismiss();
        });


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