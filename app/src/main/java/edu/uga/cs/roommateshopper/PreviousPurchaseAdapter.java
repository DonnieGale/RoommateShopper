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


public class PreviousPurchaseAdapter extends RecyclerView.Adapter<PreviousPurchaseAdapter.PreviousPurchasedHolder> {

    private List<Settlement> settlements;

    public PreviousPurchaseAdapter(List<Settlement> settlements) {
        this.settlements = settlements;
    }

    class PreviousPurchasedHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        RecyclerView recycler;
        TextView total;
        TextView average;

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

    @NonNull
    @Override
    public PreviousPurchasedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_previous_purchase_adapter, parent, false);
        return new PreviousPurchasedHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return settlements != null ? settlements.size() : 0;
    }
}
