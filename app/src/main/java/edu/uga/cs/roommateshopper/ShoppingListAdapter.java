package edu.uga.cs.roommateshopper;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

        public ShoppingListHolder(View itemView) {
            super(itemView);
        }

    }

    @NonNull
    @Override
    public ShoppingListHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.shopping_list_item, parent, false );
        return new ShoppingListHolder( view );
    }


    @Override
    public void onBindViewHolder( ShoppingListHolder holder, int position ) {
        ShoppingItem item = items.get( position );
        TextView itemID = holder.itemView.findViewById( R.id.itemID );
        TextView itemName = holder.itemView.findViewById( R.id.ItemName );
        TextView itemAddedBy = holder.itemView.findViewById( R.id.ItemAdedBy );
        TextView itemTime = holder.itemView.findViewById( R.id.ItemTime );

        String id = item.id;
        String name = item.name;
        String addedBy = item.addedBy;
        long timestamp = item.timestamp;

        itemID.setText( id );
        itemName.setText( name );
        itemAddedBy.setText( addedBy );
        itemTime.setText(String.valueOf( timestamp));


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
