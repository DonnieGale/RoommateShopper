package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.roommateshopper.models.Settlement;

/**
 * Fragment that displays a list of previous settlements (purchases that have been settled).
 * It fetches settlement data from the Firebase database and presents it in a RecyclerView.
 */
public class PrevPurchasesFragment extends Fragment {

    RecyclerView recycler;

    List<Settlement> items;

    private static final String TAG = "PreviousPurchaseFragment";

    /**
     * Default constructor for the PrevPurchasesFragment.
     */
    public PrevPurchasesFragment() {}

    /**
     * Static factory method to create a new instance of the PrevPurchasesFragment.
     *
     * @return A new instance of fragment PrevPurchasesFragment.
     */
    public static PrevPurchasesFragment newInstance() {
        PrevPurchasesFragment fragment = new PrevPurchasesFragment();
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

        return inflater.inflate(R.layout.fragment_prev_purchases, container, false);
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes the RecyclerView and its adapter, and starts listening for settlement data.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<Settlement>();

        recycler = view.findViewById(R.id.recycler2);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        PreviousPurchaseAdapter adapter = new PreviousPurchaseAdapter(items);
        recycler.setAdapter(adapter);

        listenForPurchaseItems();

    }

    /**
     * Sets up a listener for the settlements collection in the Firebase database.
     * Updates the local list of settlements and notifies the adapter when data changes.
     */
    private void listenForPurchaseItems() {
        FirebaseDBHelper.getInstance()
                .getSettlementsRef()
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        items.clear();

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                            Settlement item = itemSnapshot.getValue(Settlement.class);

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
