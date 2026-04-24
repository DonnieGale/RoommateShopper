package edu.uga.cs.roommateshopper;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder> {

    private List<ShoppingItem> items;

    public ShoppingListAdapter(List<ShoppingItem> items) {
        this.items = items;
        for (ShoppingItem item : items) {
            Log.d("ShoppingListAdapter", "Item: " + item.name);
        }

    }

    class ShoppingListHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public ShoppingListHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ShoppingItem item = items.get(position);
                        DialogFragment editShoppingItemFragment = new EditShoppingItemFragment(item);
                        editShoppingItemFragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), null);
                        Log.d("ShoppingListAdapter", "Item clicked: " + item.name);
                    }
                }

            });
        }

    }

    @NonNull
    @Override
    public ShoppingListHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.shopping_list_item, parent, false );
        return new ShoppingListHolder( view);
    }


    @Override
    public void onBindViewHolder( ShoppingListHolder holder, int position ) {
        ShoppingItem item = items.get(position);
        TextView itemID = holder.itemView.findViewById(R.id.itemID);
        TextView itemName = holder.itemView.findViewById(R.id.ItemName);
        TextView itemAddedBy = holder.itemView.findViewById(R.id.ItemAdedBy);
        TextView itemTime = holder.itemView.findViewById(R.id.ItemTime);
        TextView itemPrice = holder.itemView.findViewById(R.id.Spent);
        TextView itemQuantity = holder.itemView.findViewById(R.id.Difference);


        String id = item.id;
        String name = item.name;
        String addedBy = item.addedBy;
        long timestamp = item.timestamp;
        Double price = item.price;
        int number = item.quantity;

        itemID.setText(id);
        itemName.setText(name);
        itemAddedBy.setText(addedBy);
        itemTime.setText(String.valueOf(timestamp));
        itemPrice.setText(String.valueOf(price));
        itemQuantity.setText(String.valueOf(number));

    }







    @Override
    public int getItemCount() {
        if( items != null ) {
            return items.size();
        } else {
            return 0;
        }
    }
}
