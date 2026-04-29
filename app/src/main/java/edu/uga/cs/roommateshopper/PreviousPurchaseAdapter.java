package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.uga.cs.roommateshopper.models.Settlement;
import edu.uga.cs.roommateshopper.models.UserTotal;


/**
 * Adapter class for the Previous Purchases RecyclerView.
 * It binds a list of Settlement objects to the view items in the previous purchases list.
 * Each settlement item also contains a nested RecyclerView to show individual user totals.
 */
public class PreviousPurchaseAdapter extends RecyclerView.Adapter<PreviousPurchaseAdapter.PreviousPurchasedHolder> {

    private List<Settlement> settlements;

    /**
     * Constructs a new PreviousPurchaseAdapter with the specified list of settlements.
     *
     * @param settlements The list of settlements to display.
     */
    public PreviousPurchaseAdapter(List<Settlement> settlements) {
        this.settlements = settlements;
    }

    /**
     * ViewHolder class for previous settlement items.
     * It holds the view for each individual settlement and manages a nested RecyclerView for user details.
     */
    class PreviousPurchasedHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        RecyclerView recycler;
        TextView total;
        TextView average;

        /**
         * Constructs a new PreviousPurchasedHolder.
         *
         * @param itemView The view of the individual settlement item.
         */
        public PreviousPurchasedHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            recycler = itemView.findViewById(R.id.recycler);
            total = itemView.findViewById(R.id.textView9);
            average = itemView.findViewById(R.id.textView10);

            if (recycler != null) {
                recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new PreviousPurchasedHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public PreviousPurchasedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_previous_purchase_adapter, parent, false);
        return new PreviousPurchasedHolder(view);
    }

    /**
     * Called by RecyclerView to display the settlement data at the specified position.
     * It also sets up the adapter for the nested RecyclerView.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PreviousPurchasedHolder holder, int position) {
        Settlement settlement = settlements.get(position);

        if (holder.total != null) {
            holder.total.setText(String.format(Locale.getDefault(), "Total: $%.2f", settlement.totalCost));
        }
        if (holder.average != null) {
            holder.average.setText(String.format(Locale.getDefault(), "Average per Person: $%.2f", settlement.averagePerPerson));
        }

        List<UserTotal> items = new ArrayList<>();
        if (settlement.perUserTotals != null) {
            items.addAll(settlement.perUserTotals.values());
        }

        PreviousPurchaseAdapterAdapter adapter = new PreviousPurchaseAdapterAdapter(items);
        if (holder.recycler != null) {
            holder.recycler.setAdapter(adapter);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return settlements != null ? settlements.size() : 0;
    }
}
