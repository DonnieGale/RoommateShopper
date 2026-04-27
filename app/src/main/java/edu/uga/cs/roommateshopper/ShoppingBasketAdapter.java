package edu.uga.cs.roommateshopper;

import android.util.Log;
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

public class ShoppingBasketAdapter extends RecyclerView.Adapter<ShoppingBasketAdapter.ShoppingBasketHolder> {

    private List<ShoppingItem> items;

    public ShoppingBasketAdapter(List<ShoppingItem> items) {
        this.items = items;
        for (ShoppingItem item : items) {
            Log.d("ShoppingBasketAdapter", "Item: " + item.name);
        }
    }

    class ShoppingBasketHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ShoppingBasketHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    ShoppingItem item = items.get(position);

                    // 🔑 FIX: use newInstance()
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

    @NonNull
    @Override
    public ShoppingBasketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingBasketHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingBasketHolder holder, int position) {
        ShoppingItem item = items.get(position);

        holder.itemView.<android.widget.TextView>findViewById(R.id.itemID)
                .setText("ITEM:");

        holder.itemView.<android.widget.TextView>findViewById(R.id.ItemName)
                .setText(item.name);

        holder.itemView.<android.widget.TextView>findViewById(R.id.ItemAdedBy)
                .setText("Added By: " + item.addedBy);

        /*
        holder.itemView.<android.widget.TextView>findViewById(R.id.ItemTime)
                .setText("Time Added: " + String.valueOf(item.timestamp));
        */
        holder.itemView.<android.widget.TextView>findViewById(R.id.Spent)
                .setText("Cost: $" + String.format("%.2f", item.price));

        holder.itemView.<android.widget.TextView>findViewById(R.id.Difference)
                .setText("Quantity: " + String.valueOf(item.quantity));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}