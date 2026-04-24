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

    Button removeFromPurchaseButton;
    ShoppingItem item;
    String purchaseId;
    public Remove_Purchase_Item(ShoppingItem item, String purchaseId) {
        this.item = item;
        this.purchaseId = purchaseId;
    }


    public static Remove_Purchase_Item newInstance(ShoppingItem item, String purchaseId) {
        Remove_Purchase_Item fragment = new Remove_Purchase_Item(item,purchaseId);
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
        return inflater.inflate(R.layout.fragment_remove__purchase__item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        removeFromPurchaseButton = view.findViewById(R.id.removeFromPurchaseButton);
        removeFromPurchaseButton.setOnClickListener(v -> {
                //FirebaseDBHelper.getInstance().(item);
            FirebaseDBHelper.getInstance()
                    .removeItemFromPurchaseAndUpdate(purchaseId, item);

            Log.d(TAG, "Item removed from purchase and added back to list");
        });
    }

}
