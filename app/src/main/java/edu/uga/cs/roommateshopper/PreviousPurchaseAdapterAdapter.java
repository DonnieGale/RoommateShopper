package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import edu.uga.cs.roommateshopper.models.UserTotal;

/**
 * Nested adapter class for displaying individual user totals within a settlement.
 * It binds a list of UserTotal objects to the view items in the nested settlement details list.
 */
public class PreviousPurchaseAdapterAdapter extends RecyclerView.Adapter<PreviousPurchaseAdapterAdapter.UserTotalHolder> {

    private List<UserTotal> items;

    /**
     * Constructs a new PreviousPurchaseAdapterAdapter with the specified list of user totals.
     *
     * @param items The list of user totals to display.
     */
    public PreviousPurchaseAdapterAdapter(List<UserTotal> items) {
        this.items = items;
    }

    /**
     * ViewHolder class for individual user total items within a settlement.
     * It holds the view for each individual user's spending and difference from the average.
     */
    class UserTotalHolder extends RecyclerView.ViewHolder {
        TextView person;
        TextView spent;
        TextView difference;

        /**
         * Constructs a new UserTotalHolder.
         *
         * @param itemView The view of the individual user total item.
         */
        public UserTotalHolder(View itemView) {
            super(itemView);
            person = itemView.findViewById(R.id.Person);
            spent = itemView.findViewById(R.id.Cost);
            difference = itemView.findViewById(R.id.Quantity);
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new UserTotalHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public UserTotalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_previous_purchase_adapter_adapter, parent, false);
        return new UserTotalHolder(view);
    }

    /**
     * Called by RecyclerView to display the user total data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(UserTotalHolder holder, int position) {
        UserTotal item = items.get(position);
        
        holder.person.setText(item.name != null ? item.name : "Unknown Roommate");
        holder.spent.setText(String.format(Locale.getDefault(), "Spent: $%.2f", item.totalSpent));
        holder.difference.setText(String.format(Locale.getDefault(), "$%.2f", item.differenceFromAverage));
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
