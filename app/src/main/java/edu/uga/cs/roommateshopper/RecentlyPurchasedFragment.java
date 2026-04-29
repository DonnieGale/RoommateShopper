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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uga.cs.roommateshopper.models.Purchase;
import edu.uga.cs.roommateshopper.models.Settlement;
import edu.uga.cs.roommateshopper.models.User;
import edu.uga.cs.roommateshopper.models.UserTotal;


public class RecentlyPurchasedFragment extends Fragment {


    public static final String TAG = "RecentlyPurchasedFragment";
    private FloatingActionButton settleButton;
    private RecyclerView recyclerView;
    private RecentlyPurchasedFragmentAdapter adapter;
    private List<Purchase> purchases;

    public RecentlyPurchasedFragment() {}


    public static RecentlyPurchasedFragment newInstance() {
        RecentlyPurchasedFragment fragment = new RecentlyPurchasedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recently_purchased, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.RecyclerView2);
        purchases = new ArrayList<>();
        adapter = new RecentlyPurchasedFragmentAdapter(purchases);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        listenForPurchaseItems();

        settleButton = view.findViewById(R.id.settleButton);

        settleButton.setOnClickListener(v -> {

            DatabaseReference purchasesRef =
                    FirebaseDBHelper.getInstance().getPurchasesRef();

            DatabaseReference usersRef =
                    FirebaseDBHelper.getInstance().getUsersRef();


            purchasesRef.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot purchaseSnapshot) {

                    double totalCost = 0;

                    Map<String, Double> userSpentMap = new HashMap<>();

                    for (DataSnapshot purchaseSnap : purchaseSnapshot.getChildren()) {

                        Purchase purchase = purchaseSnap.getValue(Purchase.class);

                        if (purchase != null) {

                            totalCost += purchase.totalPrice;

                            double current = userSpentMap.getOrDefault(purchase.purchasedBy, 0.0);

                            userSpentMap.put(
                                    purchase.purchasedBy,
                                    current + purchase.totalPrice
                            );
                        }
                    }

                    double finalTotalCost = totalCost;
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {

                            int numUsers = (int) userSnapshot.getChildrenCount();

                            if (numUsers == 0) return;

                            double average = finalTotalCost / numUsers;

                            Map<String, UserTotal> perUserTotals = new HashMap<>();

                            for (DataSnapshot userSnap : userSnapshot.getChildren()) {

                                String uid = userSnap.getKey();
                                User user = userSnap.getValue(User.class);

                                double spent = userSpentMap.getOrDefault(uid, 0.0);

                                double difference = spent - average;

                                perUserTotals.put(uid, new UserTotal(
                                        uid,
                                        user != null ? user.name : "Unknown",
                                        spent,
                                        difference
                                ));
                            }

                            Settlement settlement = new Settlement(
                                    null,
                                    System.currentTimeMillis(),
                                    finalTotalCost,
                                    average,
                                    numUsers,
                                    perUserTotals
                            );

                            FirebaseDBHelper.getInstance()
                                    .addSettlement(settlement);

                            Log.d(TAG, "Settlement saved successfully");

                            FirebaseDBHelper.getInstance()
                                    .clearPurchases();

                            Log.d(TAG, "Purchases cleared after settlement");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "User fetch failed: " + error.getMessage());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Purchase fetch failed: " + error.getMessage());
                }
            });
        });

    }

    private void listenForPurchaseItems() {
        FirebaseDBHelper.getInstance()
                .getPurchasesRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        purchases.clear();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            Purchase purchase = itemSnapshot.getValue(Purchase.class);
                            if (purchase != null) {
                                purchase.id = itemSnapshot.getKey();
                                purchases.add(purchase);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                    }
                });
    }
}