package edu.uga.cs.roommateshopper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import edu.uga.cs.roommateshopper.models.UserTotal;


public class PreviousPurchaseAdapterAdapter extends RecyclerView.Adapter<PreviousPurchaseAdapterAdapter.UserTotalHolder> {

    private List<UserTotal> items;

    public PreviousPurchaseAdapterAdapter(List<UserTotal> items) {
        this.items = items;
    }

    class UserTotalHolder extends RecyclerView.ViewHolder {
        TextView person;
        TextView spent;
        TextView difference;

        public UserTotalHolder(View itemView) {
            super(itemView);
            person = itemView.findViewById(R.id.Person);
            spent = itemView.findViewById(R.id.Spent);
            difference = itemView.findViewById(R.id.Difference);
        }
    }

    @NonNull
    @Override
    public UserTotalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_previous_purchase_adapter_adapter, parent, false);
        return new UserTotalHolder(view);
    }

    @Override
    public void onBindViewHolder(UserTotalHolder holder, int position) {
        UserTotal item = items.get(position);
        
        holder.person.setText(item.name != null ? item.name : "Unknown Roommate");
        holder.spent.setText(String.format(Locale.getDefault(), "Spent: $%.2f", item.totalSpent));
        holder.difference.setText(String.format(Locale.getDefault(), "Difference: $%.2f", item.differenceFromAverage));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
