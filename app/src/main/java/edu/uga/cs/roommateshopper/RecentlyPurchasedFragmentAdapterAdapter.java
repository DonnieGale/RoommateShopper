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

import edu.uga.cs.roommateshopper.models.Purchase;
import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class RecentlyPurchasedFragmentAdapterAdapter extends RecyclerView.Adapter<RecentlyPurchasedFragmentAdapterAdapter.RecentlyPurchasedHolderHolder> {

    List<ShoppingItem> items;

    public RecentlyPurchasedFragmentAdapterAdapter(List<ShoppingItem> items) {
        this.items = items;
    }

    class RecentlyPurchasedHolderHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public RecentlyPurchasedHolderHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ShoppingItem item = items.get(position);
                        DialogFragment Remove_Purchase_Item = new Remove_Purchase_Item(item);
                        Remove_Purchase_Item.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), null);
                    }
                }
            });
        }
    }
    @NonNull
    @Override
    public RecentlyPurchasedHolderHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.fragment_recently_purchased_item, parent, false );
        return new RecentlyPurchasedHolderHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentlyPurchasedHolderHolder holder, int position ) {
        ShoppingItem item = items.get(position);
        TextView itemID = holder.itemView.findViewById(R.id.itemID);
        TextView itemName = holder.itemView.findViewById(R.id.ItemName);
        TextView itemAddedBy = holder.itemView.findViewById(R.id.ItemAdedBy);
        TextView itemTime = holder.itemView.findViewById(R.id.ItemTime);
        TextView itemPrice = holder.itemView.findViewById(R.id.ItemPrice);
        TextView itemQuantity = holder.itemView.findViewById(R.id.ItemQuantity);


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
