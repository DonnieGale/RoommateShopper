package edu.uga.cs.roommateshopper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uga.cs.roommateshopper.models.Purchase;
import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class RecentlyPurchasedFragmentAdapter extends RecyclerView.Adapter<RecentlyPurchasedFragmentAdapter.RecentlyPurchasedHolder> {
    private List<Purchase> purchases;

    public RecentlyPurchasedFragmentAdapter(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    class RecentlyPurchasedHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        RecyclerView recycler;
        TextView textView2;
        TextView dateTextView; // 👈 NEW

        public RecentlyPurchasedHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView5);
            recycler = itemView.findViewById(R.id.recycler);
            textView2 = itemView.findViewById(R.id.TotalPrice);
            dateTextView = itemView.findViewById(R.id.PurchaseDate); // 👈 make sure this exists in XML

            if (recycler != null) {
                recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            }

            if (cardView != null) {
                cardView.setOnClickListener(v -> {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Purchase purchase = purchases.get(position);
                        DialogFragment editPriceFragment = EditPrice.newInstance(purchase);
                        editPriceFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "edit_price");
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public RecentlyPurchasedHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recently__purchased__items, parent, false);
        return new RecentlyPurchasedHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentlyPurchasedHolder holder, int position ) {
        Purchase purchase = purchases.get(position);


        holder.textView.setText("Purchased by: " + purchase.purchasedByName);
        holder.textView2.setText("Total Price: $" + String.format("%.2f", purchase.totalPrice));

        // ✅ FORMAT DATE/TIME
        if (purchase.timestamp != 0) {
            Date date = new Date(purchase.timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());
            String formattedDate = sdf.format(date);

            holder.dateTextView.setText("Date: " + formattedDate);
        } else {
            holder.dateTextView.setText("Date: N/A");
        }

        List<ShoppingItem> items = new ArrayList<>();
        if (purchase.items != null) {
            items.addAll(purchase.items.values());
        }

        RecentlyPurchasedFragmentAdapterAdapter adapter =
                new RecentlyPurchasedFragmentAdapterAdapter(items, purchase.id);

        holder.recycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return purchases != null ? purchases.size() : 0;
    }
}

