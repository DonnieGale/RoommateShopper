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


public class ShoppingList extends Fragment {

    List<ShoppingItem> items;
    private static final String TAG = "FIREBASE_TEST";

    public ShoppingList() {
        // Required empty public constructor
    }


    public static ShoppingList newInstance(String param1, String param2) {
        ShoppingList fragment = new ShoppingList();
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

        items = new ArrayList<ShoppingItem>();

        // ADD / READ / DELETE shopping item test
        addTestItem(); // adds a test item
        listenForShoppingItems(); // retrieves items and deletes a test item
        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ShoppingListAdapter adapter = new ShoppingListAdapter(items);
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddShoppingItemFragment();
                newFragment.show( getSupportFragmentManager(), null);
            }
        });






    }

    // FOR TESTING DATABASE OPERATIONS:

    private void addTestItem() {
        ShoppingItem item = new ShoppingItem();
        item.name = "DeleteMe";
        item.addedBy = "testUser123";
        item.timestamp = System.currentTimeMillis();

        FirebaseDBHelper.getInstance().addShoppingItem(item);
        items.add(item);

        Log.d(TAG, "Item added to Firebase");
    }



    private void listenForShoppingItems() {
        FirebaseDBHelper.getInstance()
                .getShoppingListRef()
                .addValueEventListener(new ValueEventListener() {

                    boolean hasDeleted = false; // prevent multiple deletes

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.d(TAG, "----- CURRENT SHOPPING LIST -----");

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                            ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);

                            if (item != null) {
                                item.id = itemSnapshot.getKey();

                                Log.d(TAG,
                                        "ID: " + item.id +
                                                ", Name: " + item.name +
                                                ", AddedBy: " + item.addedBy +
                                                ", Time: " + item.timestamp);

                                // 🔴 DELETE TEST: delete item named "DeleteMe"
                                /*if (!hasDeleted && "DeleteMe".equals(item.name)) {
                                    hasDeleted = true;

                                    FirebaseDBHelper.getInstance()
                                            .deleteShoppingItem(item.id);

                                    Log.d(TAG, "Deleted item with ID: " + item.id);
                                }*/
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                    }
                });
    }
}