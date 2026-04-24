package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uga.cs.roommateshopper.models.ShoppingItem;


public class ShoppingBasketFragment extends Fragment {

    List<ShoppingItem> items;

    RecyclerView recycler;
    private static final String TAG = "ShoppingBasketFragment";


    public ShoppingBasketFragment() {
    }

    public static ShoppingBasketFragment newInstance() {
        ShoppingBasketFragment fragment = new ShoppingBasketFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_basket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<ShoppingItem>();

        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ShoppingBasketAdapter adapter = new ShoppingBasketAdapter(items);
        recycler.setAdapter(adapter);

        //addTestItem(); // adds a test item
        listenForBasketItems(); // retrieves items and deletes a test item

        FloatingActionButton checkoutButton = view.findViewById(R.id.checkoutButton);

        checkoutButton.setOnClickListener(v -> {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Log.e(TAG, "User not logged in");
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            DatabaseReference basketRef =
                    FirebaseDBHelper.getInstance().getBasketRef(uid);

            basketRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (!snapshot.exists()) {
                        Log.d(TAG, "Basket is empty");
                        return;
                    }

                    Map<String, ShoppingItem> items = new HashMap<>();
                    double totalPrice = 0;

                    for (DataSnapshot itemSnap : snapshot.getChildren()) {

                        ShoppingItem item = itemSnap.getValue(ShoppingItem.class);

                        if (item != null) {
                            item.id = itemSnap.getKey();
                            items.put(item.id, item);

                            totalPrice += item.price;
                        }
                    }

                    long timestamp = System.currentTimeMillis();

                    FirebaseDBHelper.getInstance()
                            .checkoutBasket(uid, userName, items, totalPrice, timestamp);

                    Log.d(TAG, "Checkout complete. Total: " + totalPrice);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, error.getMessage());
                }
            });
        });


    }

    private void listenForBasketItems() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDBHelper.getInstance()
                .getBasketRef(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.d(TAG, "----- CURRENT SHOPPING LIST -----");
                        items.clear();

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                            ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);

                            if (item != null) {
                                item.id = itemSnapshot.getKey();
                                items.add(item);

                                Log.d(TAG,
                                        "ID: " + item.id +
                                                ", Name: " + item.name +
                                                ", AddedBy: " + item.addedBy +
                                                ", Time: " + item.timestamp);


                            }
                        }
                        if (recycler.getAdapter() != null) {
                            recycler.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                    }
                });
    }
}