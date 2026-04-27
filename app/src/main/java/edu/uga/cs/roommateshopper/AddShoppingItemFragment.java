package edu.uga.cs.roommateshopper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class AddShoppingItemFragment extends DialogFragment {

    private EditText itemName;
    private EditText price;
    private EditText quantity;

    public AddShoppingItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_shopping_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemName = view.findViewById(R.id.editText1);
        price = view.findViewById(R.id.editText2);
        quantity = view.findViewById(R.id.editText3);
        Button saveButton = view.findViewById(R.id.button);

        saveButton.setOnClickListener(v -> addItem());
    }

    private void addItem() {
        String nameString = itemName.getText().toString();
        String priceString = price.getText().toString();
        String quantityString = quantity.getText().toString();

        if (nameString.isEmpty()) {
            itemName.setError("Name required");
            return;
        }

        double priceValue;
        int quantityNum;

        try {
            priceValue = priceString.isEmpty() ? 0 : Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            price.setError("Invalid price");
            return;
        }

        try {
            quantityNum = quantityString.isEmpty() ? 0 : Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            quantity.setError("Invalid quantity");
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        DatabaseReference userRef =
                FirebaseDatabase.getInstance().getReference("users").child(uid);

        DatabaseReference shoppingRef =
                FirebaseDatabase.getInstance().getReference("shopping_list");

        // 🔥 Fetch user's display name from database
        userRef.get().addOnSuccessListener(snapshot -> {

            String addedBy = snapshot.child("name").getValue(String.class);

            // fallback safety
            if (addedBy == null || addedBy.isEmpty()) {
                addedBy = currentUser.getEmail();
            }

            ShoppingItem item = new ShoppingItem(
                    null,
                    nameString,
                    addedBy,
                    System.currentTimeMillis(),
                    priceValue,
                    quantityNum
            );

            shoppingRef.push().setValue(item)
                    .addOnSuccessListener(unused -> {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(),
                                    "Item added: " + item.name,
                                    Toast.LENGTH_SHORT).show();
                        }

                        itemName.setText("");
                        price.setText("");
                        quantity.setText("");

                        dismiss();
                    })
                    .addOnFailureListener(e -> {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(),
                                    "Failed to add item",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }).addOnFailureListener(e -> {
            if (getActivity() != null) {
                Toast.makeText(getActivity(),
                        "Failed to load user info",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
