package edu.uga.cs.roommateshopper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * Nested adapter class for displaying individual shopping items within a purchase in the recently purchased list.
 * It binds a list of ShoppingItem objects to the view items in the nested purchase details list.
 */
public class RecentlyPurchasedFragmentAdapterAdapter
        extends RecyclerView.Adapter<RecentlyPurchasedFragmentAdapterAdapter.RecentlyPurchasedHolderHolder> {

    List<ShoppingItem> items;
    private String purchaseId;

    /**
     * Constructs a new RecentlyPurchasedFragmentAdapterAdapter with the specified list of items and purchase ID.
     *
     * @param items The list of shopping items in the purchase.
     * @param purchaseId The ID of the purchase these items belong to.
     */
    public RecentlyPurchasedFragmentAdapterAdapter(List<ShoppingItem> items, String purchaseId) {
        this.items = items;
        this.purchaseId = purchaseId;
    }

    /**
     * ViewHolder class for individual shopping items within a purchase.
     * It holds the view for each item and handles click events to remove the item from the purchase.
     */
    class RecentlyPurchasedHolderHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        /**
         * Constructs a new RecentlyPurchasedHolderHolder.
         *
         * @param itemView The view of the individual shopping item within a purchase.
         */
        public RecentlyPurchasedHolderHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    ShoppingItem item = items.get(position);

                    DialogFragment fragment =
                            Remove_Purchase_Item.newInstance(item, purchaseId);

                    fragment.show(
                            ((AppCompatActivity) v.getContext())
                                    .getSupportFragmentManager(),
                            "remove_purchase"
                    );
                }
            });
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new RecentlyPurchasedHolderHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public RecentlyPurchasedHolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recently_purchased_item, parent, false);

        return new RecentlyPurchasedHolderHolder(view);
    }

    /**
     * Called by RecyclerView to display the shopping item data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecentlyPurchasedHolderHolder holder, int position) {
        ShoppingItem item = items.get(position);

        ((TextView) holder.itemView.findViewById(R.id.ItemName))
                .setText(item.name);

        ((TextView) holder.itemView.findViewById(R.id.ItemAdedBy))
                .setText("Added By: " + item.addedBy);

        ((TextView) holder.itemView.findViewById(R.id.Cost))
                .setText("Cost: $" + String.format("%.2f", item.price));

        ((TextView) holder.itemView.findViewById(R.id.Quantity))
                .setText("Quantity: " + String.valueOf(item.quantity));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
