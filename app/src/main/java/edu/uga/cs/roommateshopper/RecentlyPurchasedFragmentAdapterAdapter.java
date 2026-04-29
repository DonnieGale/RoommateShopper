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

public class RecentlyPurchasedFragmentAdapterAdapter
        extends RecyclerView.Adapter<RecentlyPurchasedFragmentAdapterAdapter.RecentlyPurchasedHolderHolder> {

    List<ShoppingItem> items;
    private String purchaseId;

    public RecentlyPurchasedFragmentAdapterAdapter(List<ShoppingItem> items, String purchaseId) {
        this.items = items;
        this.purchaseId = purchaseId;
    }

    class RecentlyPurchasedHolderHolder extends RecyclerView.ViewHolder {

        CardView cardView;

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

    @NonNull
    @Override
    public RecentlyPurchasedHolderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recently_purchased_item, parent, false);

        return new RecentlyPurchasedHolderHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
