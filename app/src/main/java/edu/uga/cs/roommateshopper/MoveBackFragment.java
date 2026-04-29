package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class MoveBackFragment extends DialogFragment {

    Button button;
    ShoppingItem item;


    public MoveBackFragment() {}

    public static MoveBackFragment newInstance(ShoppingItem item) {
        MoveBackFragment fragment = new MoveBackFragment();

        Bundle args = new Bundle();
        args.putSerializable("item", item);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = (ShoppingItem) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_move_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.button3);

        if (item == null) {
            Log.e("MoveBackFragment", "Item is null after recreation");
            dismiss();
            return;
        }

        button.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDBHelper.getInstance()
                    .moveItemToShoppingList(uid, item);

            dismiss();
        });
    }
}