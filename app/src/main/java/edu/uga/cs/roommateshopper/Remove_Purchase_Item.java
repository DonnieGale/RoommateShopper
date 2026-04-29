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

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * DialogFragment that allows users to remove a specific item from a purchase.
 * When an item is removed, it is typically added back to the main shopping list.
 */
public class Remove_Purchase_Item extends DialogFragment {

    public static final String TAG = "Remove_Purchase_Item";

    private static final String ARG_ITEM = "item";
    private static final String ARG_PURCHASE_ID = "purchase_id";

    Button removeFromPurchaseButton;

    ShoppingItem item;
    String purchaseId;

    /**
     * Default constructor for the Remove_Purchase_Item fragment.
     */
    public Remove_Purchase_Item() {}

    /**
     * Static factory method to create a new instance of the Remove_Purchase_Item fragment.
     *
     * @param item The shopping item to be removed from the purchase.
     * @param purchaseId The ID of the purchase from which the item is to be removed.
     * @return A new instance of fragment Remove_Purchase_Item.
     */
    public static Remove_Purchase_Item newInstance(ShoppingItem item, String purchaseId) {
        Remove_Purchase_Item fragment = new Remove_Purchase_Item();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        args.putString(ARG_PURCHASE_ID, purchaseId);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is first created.
     * Retrieves the item and purchaseId from the fragment arguments.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = (ShoppingItem) getArguments().getSerializable(ARG_ITEM);
            purchaseId = getArguments().getString(ARG_PURCHASE_ID);
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
        return inflater.inflate(R.layout.fragment_remove__purchase__item, container, false);
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes the UI components and sets up the click listener for the remove button.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        removeFromPurchaseButton = view.findViewById(R.id.removeFromPurchaseButton);

        if (item == null || purchaseId == null) {
            Log.e(TAG, "Item or purchaseId is null after rotation");
            dismiss();
            return;
        }

        removeFromPurchaseButton.setOnClickListener(v -> {
            FirebaseDBHelper.getInstance()
                    .removeItemFromPurchaseAndUpdate(purchaseId, item);

            Log.d(TAG, "Item removed from purchase and added back to list");
            dismiss();
        });
    }
}
