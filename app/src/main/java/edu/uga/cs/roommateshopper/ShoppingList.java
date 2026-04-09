package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingList extends Fragment {

    List<ShoppingItem> items;

    public ShoppingList() {
        // Required empty public constructor
    }


    public static ShoppingList newInstance(String param1, String param2) {
        ShoppingList fragment = new ShoppingList();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<>();




    }
}