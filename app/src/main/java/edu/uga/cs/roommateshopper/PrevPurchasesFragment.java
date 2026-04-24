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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.roommateshopper.models.Purchase;
import edu.uga.cs.roommateshopper.models.Settlement;
import edu.uga.cs.roommateshopper.models.ShoppingItem;


public class PrevPurchasesFragment extends Fragment {

    TextView total;
    TextView average;
    RecyclerView recycler;
    Double OverallTotal;

    List<Settlement> items;

    private static final String TAG = "PreviousPurchaseFragment";


    public PrevPurchasesFragment() {
        // Required empty public constructor
    }



    public static PrevPurchasesFragment newInstance() {
        PrevPurchasesFragment fragment = new PrevPurchasesFragment();
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
        return inflater.inflate(R.layout.fragment_prev_purchases, container, false);
    }
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
