package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.uga.cs.roommateshopper.models.Purchase;

/**
 * DialogFragment that allows users to edit the price of a specific purchase.
 * It provides an interface to input a new price and updates the record in the Firebase database.
 */
public class EditPrice extends DialogFragment {

    Purchase purchase;
    EditText editText;
    Button button;

    /**
     * Default constructor for the EditPrice fragment.
     */
    public EditPrice() {}

    /**
     * Static factory method to create a new instance of the EditPrice fragment with a specific purchase.
     *
     * @param purchase The purchase object whose price is to be edited.
     * @return A new instance of fragment EditPrice.
     */
    public static EditPrice newInstance(Purchase purchase) {
        EditPrice fragment = new EditPrice();
        Bundle args = new Bundle();
        args.putSerializable("purchase", purchase);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is first created.
     * Retrieves the purchase object from the fragment arguments.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            purchase = (Purchase) getArguments().getSerializable("purchase");
        }

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
        return inflater.inflate(R.layout.fragment_edit_price, container, false);
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes the UI components and sets up the click listener for the update button.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editTextText);
        button = view.findViewById(R.id.button4);
        button.setOnClickListener(v -> {
            try {
                double newPrice = Double.parseDouble(editText.getText().toString());
                FirebaseDBHelper.getInstance().updatePurchasePrice(purchase.id, newPrice);
                dismiss();
            } catch (NumberFormatException e) {
                editText.setError("Invalid price format");
            }
        });
    }
}
