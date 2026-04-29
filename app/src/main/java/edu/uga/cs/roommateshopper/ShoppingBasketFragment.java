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

/**
 * Fragment that displays the user's shopping basket.
 * It manages the items that the user intends to purchase and provides
 * functionality to check out the basket.
 */
public class ShoppingBasketFragment extends Fragment {

    List<ShoppingItem> items;

    RecyclerView recycler;
    private static final String TAG = "ShoppingBasketFragment";

    /**
     * Default constructor for the ShoppingBasketFragment.
     */
    public ShoppingBasketFragment() {
    }

    /**
     * Static factory method to create a new instance of the ShoppingBasketFragment.
     *
     * @return A new instance of fragment ShoppingBasketFragment.
     */
    public static ShoppingBasketFragment newInstance() {
        ShoppingBasketFragment fragment = new ShoppingBasketFragment();
        return fragment;
    }

    /**
     * Called when the fragment is first created.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        return inflater.inflate(R.layout.fragment_shopping_basket, container, false);
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes the RecyclerView, the adapter, and the checkout button.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<ShoppingItem>();

        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ShoppingBasketAdapter adapter = new ShoppingBasketAdapter(items);
        recycler.setAdapter(adapter);

        listenForBasketItems();

        FloatingActionButton checkoutButton = view.findViewById(R.id.checkoutButton);

        checkoutButton.setOnClickListener(v -> {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Log.e(TAG, "User not logged in");
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String userName1 = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            if (userName1 == null || userName1.isEmpty()) {
                userName1 = userEmail.substring(0, userEmail.indexOf('@'));
            }
            String userName = userName1;

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

                            totalPrice += (item.price * 1.08);
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

    /**
     * Sets up a listener for the user's shopping basket in the Firebase database.
     * Updates the local list of items and notifies the adapter when data changes.
     */
    private void listenForBasketItems() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDBHelper.getInstance()
                .getBasketRef(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        items.clear();

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                            ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);

                            if (item != null) {
                                item.id = itemSnapshot.getKey();
                                items.add(item);
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
