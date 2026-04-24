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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

        public RecentlyPurchasedHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textView = itemView.findViewById(R.id.textView5);
            recycler = itemView.findViewById(R.id.recycler);

            if (recycler != null) {
                recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            }

            if (cardView != null) {
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Purchase purchase = purchases.get(position);
                            // on click
                        }
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public RecentlyPurchasedHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.fragment_recently__purchased__items, parent, false );
        return new RecentlyPurchasedHolder(view);
    }


    @Override
    public void onBindViewHolder(RecentlyPurchasedHolder holder, int position ) {
        Purchase purchase = purchases.get(position);
        holder.textView.setText("Purchased by: " + purchase.purchasedByName + " Total: $" + String.format("%.2f", purchase.totalPrice));
        
        List<ShoppingItem> items = new ArrayList<>();
        if (purchase.items != null) {
            items.addAll(purchase.items.values());
        }
        
        RecentlyPurchasedFragmentAdapterAdapter adapter = new RecentlyPurchasedFragmentAdapterAdapter(items,purchase.id);
        holder.recycler.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        if( purchases != null ) {
            return purchases.size();
        } else {
            return 0;
        }
    }
}

