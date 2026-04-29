package edu.uga.cs.roommateshopper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * Adapter class for the Shopping Basket RecyclerView.
 * It binds the user's shopping basket items to the view items in the basket list.
 */
public class ShoppingBasketAdapter extends RecyclerView.Adapter<ShoppingBasketAdapter.ShoppingBasketHolder> {

    private List<ShoppingItem> items;

    /**
     * Constructs a new ShoppingBasketAdapter with the specified list of items.
     *
     * @param items The list of shopping items in the basket to display.
     */
    public ShoppingBasketAdapter(List<ShoppingItem> items) {
        this.items = items;
        for (ShoppingItem item : items) {
            Log.d("ShoppingBasketAdapter", "Item: " + item.name);
        }
    }

    /**
     * ViewHolder class for shopping basket items.
     * It holds the view for each individual item and handles click events to move items back to the main list.
     */
    class ShoppingBasketHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        /**
         * Constructs a new ShoppingBasketHolder.
         *
         * @param itemView The view of the individual shopping basket item.
         */
        public ShoppingBasketHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    ShoppingItem item = items.get(position);

                    DialogFragment moveBackFragment =
                            MoveBackFragment.newInstance(item);

                    moveBackFragment.show(
                            ((AppCompatActivity) v.getContext()).getSupportFragmentManager(),
                            "move_back"
                    );

                    Log.d("ShoppingBasketAdapter", "Item clicked: " + item.name);
                }
            });
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ShoppingBasketHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ShoppingBasketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shopping_basket_list, parent, false);
        return new ShoppingBasketHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ShoppingBasketHolder holder, int position) {
        ShoppingItem item = items.get(position);


        holder.itemView.<android.widget.TextView>findViewById(R.id.ItemName)
                .setText(item.name);

        holder.itemView.<android.widget.TextView>findViewById(R.id.ItemAdedBy)
                .setText("Added By: " + item.addedBy);

        holder.itemView.<android.widget.TextView>findViewById(R.id.Cost)
                .setText("Cost: $" + String.format("%.2f", item.price));

        holder.itemView.<android.widget.TextView>findViewById(R.id.Quantity)
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
