package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * Fragment that displays the shopping list of items requested by roommates.
 * It connects to the Firebase database to provide real-time updates and allows
 * users to add new items to the list via a FloatingActionButton.
 */
public class ShoppingList extends Fragment {

    List<ShoppingItem> items;

    RecyclerView recycler;
    private static final String TAG = "FIREBASE_TEST";

    /**
     * Default constructor for the ShoppingList fragment.
     */
    public ShoppingList() {}

    /**
     * Static factory method to create a new instance of the ShoppingList fragment.
     *
     * @return A new instance of fragment ShoppingList.
     */
    public static ShoppingList newInstance() {
        ShoppingList fragment = new ShoppingList();
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

        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view.
     * Initializes the RecyclerView and the FloatingActionButton.
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
        ShoppingListAdapter adapter = new ShoppingListAdapter(items);
        recycler.setAdapter(adapter);

        listenForShoppingItems();

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddShoppingItemFragment();
                newFragment.show( getParentFragmentManager(), null);
            }
        });

    }

    /**
     * Sets up a listener for the Firebase database to track changes in the shopping list.
     * When data changes, it updates the local items list and notifies the adapter.
     */
    private void listenForShoppingItems() {
        FirebaseDBHelper.getInstance()
                .getShoppingListRef()
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
