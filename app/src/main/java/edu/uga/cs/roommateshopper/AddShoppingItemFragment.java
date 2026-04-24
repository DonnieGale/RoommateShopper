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
            // Set the width to Match Parent and height to Wrap Content
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shopping_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemName = view.findViewById(R.id.editText1);
        price = view.findViewById(R.id.editText2);
        quantity = view.findViewById(R.id.editText3);
        Button saveButton = view.findViewById(R.id.button);

        saveButton.setOnClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String nameString = itemName.getText().toString();
            String priceString = price.getText().toString();
            String QuantityVal = quantity.getText().toString();

            if (nameString.isEmpty()) {
                itemName.setError("Name required");
                return;
            }

            double priceValue = 0;
            try {
                if (!priceString.isEmpty()) {
                    priceValue = Double.parseDouble(priceString);
                }
            } catch (NumberFormatException e) {
                price.setError("Invalid price");
                return;
            }
            int quantityNum = 0;
            try {
                if (!QuantityVal.isEmpty()) {
                    quantityNum = Integer.parseInt(QuantityVal);
                }
            } catch (NumberFormatException e) {
                price.setError("Invalid Quantity");
                return;
            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            // Add a new element to the list in Firebase.
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("shopping_list");
            String addedBy = currentUser.getEmail();

            final ShoppingItem item = new ShoppingItem(null, nameString, addedBy, System.currentTimeMillis(), priceValue, quantityNum);


            myRef.push().setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Show a quick confirmation
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Item added: " + item.name,
                                        Toast.LENGTH_SHORT).show();
                            }

                            // Clear the EditTexts for next use.
                            itemName.setText("");
                            price.setText("");
                            quantity.setText("");

                            // Close the dialog
                            dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Failed to add item: " + item.name,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
