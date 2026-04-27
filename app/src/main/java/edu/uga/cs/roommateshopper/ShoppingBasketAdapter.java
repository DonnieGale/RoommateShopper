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

        holder.itemView.<TextView>findViewById(R.id.itemID)
                .setText(item.id);

        holder.itemView.<TextView>findViewById(R.id.ItemName)
                .setText(item.name);

        holder.itemView.<TextView>findViewById(R.id.ItemAdedBy)
                .setText(item.addedBy);

        holder.itemView.<TextView>findViewById(R.id.ItemTime)
                .setText(String.valueOf(item.timestamp));

        holder.itemView.<TextView>findViewById(R.id.Spent)
                .setText(String.valueOf(item.price));

        holder.itemView.<TextView>findViewById(R.id.Difference)
                .setText(String.valueOf(item.quantity));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}