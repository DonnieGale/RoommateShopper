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

/**
 * Adapter class for the Recently Purchased items RecyclerView.
 * It binds a list of Purchase objects to the view items in the recently purchased list.
 * Each purchase item also contains a nested RecyclerView to show individual shopping items within that purchase.
 */
public class RecentlyPurchasedFragmentAdapter extends RecyclerView.Adapter<RecentlyPurchasedFragmentAdapter.RecentlyPurchasedHolder> {
    private List<Purchase> purchases;

    /**
     * Constructs a new RecentlyPurchasedFragmentAdapter with the specified list of purchases.
     *
     * @param purchases The list of purchases to display.
     */
    public RecentlyPurchasedFragmentAdapter(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    /**
     * ViewHolder class for recently purchased items.
     * It holds the view for each individual purchase and manages a nested RecyclerView for item details.
     * It also handles click events to edit the price of the purchase.
     */
    class RecentlyPurchasedHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        RecyclerView recycler;
        TextView textView2;
        TextView dateTextView;

        /**
         * Constructs a new RecentlyPurchasedHolder.
         *
         * @param itemView The view of the individual purchase item.
         */
        public RecentlyPurchasedHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView5);
            recycler = itemView.findViewById(R.id.recycler);
            textView2 = itemView.findViewById(R.id.TotalPrice);
            dateTextView = itemView.findViewById(R.id.PurchaseDate);

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

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new RecentlyPurchasedHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public RecentlyPurchasedHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recently__purchased__items, parent, false);
        return new RecentlyPurchasedHolder(view);
    }

    /**
     * Called by RecyclerView to display the purchase data at the specified position.
     * It also sets up the adapter for the nested RecyclerView.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecentlyPurchasedHolder holder, int position ) {
        Purchase purchase = purchases.get(position);


        holder.textView.setText("Purchased by: " + purchase.purchasedByName);
        holder.textView2.setText("Total Price: $" + String.format("%.2f", purchase.totalPrice));

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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return purchases != null ? purchases.size() : 0;
    }

}
