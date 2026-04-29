package edu.uga.cs.roommateshopper;

import static edu.uga.cs.roommateshopper.MainActivity.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class EditShoppingItemFragment extends DialogFragment {

    EditText price;
    TextView textView;
    EditText name;
    EditText quantity;

    Button save;
    Button delete;
    Button addToCart;

    ShoppingItem item;

    public EditShoppingItemFragment() {

    }

    public static EditShoppingItemFragment newInstance(ShoppingItem item) {
        EditShoppingItemFragment fragment = new EditShoppingItemFragment();

        Bundle args = new Bundle();
        args.putSerializable("item", item);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = (ShoppingItem) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit_shopping_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.EditName);
        price = view.findViewById(R.id.EditPrice);
        quantity = view.findViewById(R.id.editQuantity);
        save = view.findViewById(R.id.Save);
        delete = view.findViewById(R.id.DeleteButton);
        textView = view.findViewById(R.id.textView2);
        addToCart = view.findViewById(R.id.AddToCart);
        name.setText(item.name);
        price.setText(String.valueOf(item.price));
        quantity.setText(String.valueOf(item.quantity));

        if (item == null) {
            Log.e(TAG, "Item is null in EditShoppingItemFragment");
            dismiss();
            return;
        }

        textView.setText("Edit: " + item.name);

        delete.setOnClickListener(v -> {
            FirebaseDBHelper.getInstance().deleteShoppingItem(item.id);
            Log.d(TAG, "Deleted item with ID: " + item.id);
            dismiss();
        });

        save.setOnClickListener(v -> {
            String nameString = name.getText().toString();
            String priceString = price.getText().toString();
            String quantityString = quantity.getText().toString();

            if (nameString.isEmpty()) {
                name.setError("Name required");
                return;
            }
            if (priceString.isEmpty()) {
                price.setError("Price Required");
                return;
            }
            if (quantityString.isEmpty()) {
                quantity.setError("Quantity Required");
                return;
            }

            FirebaseDBHelper.getInstance().editName(item, nameString);
            FirebaseDBHelper.getInstance().editPrice(item, Double.parseDouble(priceString));
            FirebaseDBHelper.getInstance().editQuantity(item, Integer.parseInt(quantityString));

            dismiss();
        });

        addToCart.setOnClickListener(v -> {
            if (item.id == null) {
                Log.e(TAG, "Item ID is null");
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDBHelper.getInstance().moveItemToBasket(uid, item);

            Log.d(TAG, "Moved item to basket: " + item.name);

            dismiss();
        });
    }
}