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

import edu.uga.cs.roommateshopper.models.Purchase;


public class EditPrice extends DialogFragment {


    Purchase purchase;
    EditText editText;
    Button button;

    public EditPrice() {
        // Required empty public constructor
    }


    public static EditPrice newInstance(Purchase purchase) {
        EditPrice fragment = new EditPrice();
        Bundle args = new Bundle();
        args.putSerializable("purchase", purchase);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            purchase = (Purchase) getArguments().getSerializable("purchase");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_price, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editTextText);
        button = view.findViewById(R.id.button4);
        button.setOnClickListener(v -> {
            double newPrice = Double.parseDouble(editText.getText().toString());
            FirebaseDBHelper.getInstance().updatePurchasePrice(purchase.id, newPrice);
            dismiss();
        });
    }
}