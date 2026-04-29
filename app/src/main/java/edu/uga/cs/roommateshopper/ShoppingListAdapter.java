package edu.uga.cs.roommateshopper;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * Adapter class for the ShoppingList RecyclerView.
 * It binds the list of ShoppingItem objects to the view items in the shopping list.
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder> {

    private List<ShoppingItem> items;

    /**
     * Constructs a new ShoppingListAdapter with the specified list of items.
     *
     * @param items The list of shopping items to display.
     */
    public ShoppingListAdapter(List<ShoppingItem> items) {
        this.items = items;
        for (ShoppingItem item : items) {
            Log.d("ShoppingListAdapter", "Item: " + item.name);
        }
    }

    /**
     * ViewHolder class for shopping list items.
     * It holds the view for each individual item and handles click events.
     */
    class ShoppingListHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        /**
         * Constructs a new ShoppingListHolder.
         *
         * @param itemView The view of the individual shopping list item.
         */
        public ShoppingListHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    ShoppingItem item = items.get(position);

                    DialogFragment editShoppingItemFragment =
                            EditShoppingItemFragment.newInstance(item);

                    editShoppingItemFragment.show(
                            ((AppCompatActivity) v.getContext()).getSupportFragmentManager(),
                            "edit_item"
                    );

                    Log.d("ShoppingListAdapter", "Item clicked: " + item.name);
                }
            });
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new ShoppingListHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ShoppingListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingListHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ShoppingListHolder holder, int position) {
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
