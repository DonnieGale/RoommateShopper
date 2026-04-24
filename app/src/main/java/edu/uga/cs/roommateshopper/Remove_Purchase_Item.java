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

import edu.uga.cs.roommateshopper.models.Purchase;
import edu.uga.cs.roommateshopper.models.ShoppingItem;


public class Remove_Purchase_Item extends DialogFragment {


    Button button;
    ShoppingItem item;
    public Remove_Purchase_Item(ShoppingItem item) {
        this.item = item;
    }


    public static Remove_Purchase_Item newInstance(ShoppingItem item) {
        Remove_Purchase_Item fragment = new Remove_Purchase_Item(item);
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
        button = view.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }

}
