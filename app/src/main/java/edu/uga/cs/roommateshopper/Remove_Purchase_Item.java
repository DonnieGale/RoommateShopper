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

public class Remove_Purchase_Item extends DialogFragment {

    public static final String TAG = "Remove_Purchase_Item";

    private static final String ARG_ITEM = "item";
    private static final String ARG_PURCHASE_ID = "purchase_id";

    Button removeFromPurchaseButton;

    ShoppingItem item;
    String purchaseId;

    public Remove_Purchase_Item() {
        // Required empty public constructor
    }

    public static Remove_Purchase_Item newInstance(ShoppingItem item, String purchaseId) {
        Remove_Purchase_Item fragment = new Remove_Purchase_Item();

        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        args.putString(ARG_PURCHASE_ID, purchaseId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = (ShoppingItem) getArguments().getSerializable(ARG_ITEM);
            purchaseId = getArguments().getString(ARG_PURCHASE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_remove__purchase__item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        removeFromPurchaseButton = view.findViewById(R.id.removeFromPurchaseButton);

        // 🛡️ Safety check (prevents Firebase crash)
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
