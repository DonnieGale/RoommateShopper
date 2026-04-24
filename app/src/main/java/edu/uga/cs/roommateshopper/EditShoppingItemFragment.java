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

    Button save;
    Button delete;

    ShoppingItem item;
    EditText quantity;


    Button addToCart;


    public EditShoppingItemFragment(ShoppingItem item) {
        this.item = item;
        // Required empty public constructor
    }

    public static EditShoppingItemFragment newInstance(ShoppingItem item) {
        EditShoppingItemFragment fragment = new EditShoppingItemFragment(item);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        textView.setText("Edit: " + item.name);


        addToCart = view.findViewById(R.id.AddToCart);




        delete.setOnClickListener(new View.OnClickListener() {

            boolean hasDeleted = false; // prevent multiple deletes
            @Override
            public void onClick(View v) {
                if (!hasDeleted) {
                    hasDeleted = true;
                    FirebaseDBHelper.getInstance()
                            .deleteShoppingItem(item.id);

                    Log.d(TAG, "Deleted item with ID: " + item.id);
                }
                dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                String priceString = price.getText().toString();
                String quantityString = quantity.getText().toString();

                if (nameString.isEmpty()) {
                    name.setError("Name required");
                    return;
                }
                if(priceString.isEmpty()){
                    price.setError("Price Required");
                    return;
                }
                if(quantityString.isEmpty()){
                    quantity.setError("Quantity Required");
                    return;
                }
                FirebaseDBHelper.getInstance().editName(item, nameString);
                FirebaseDBHelper.getInstance().editPrice(item, Integer.parseInt(priceString));
                FirebaseDBHelper.getInstance().editQuantity(item, Integer.parseInt(quantityString));

                dismiss();


            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item == null || item.id == null) {
                    Log.e(TAG, "Item or item ID is null");
                    return;
                }

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDBHelper.getInstance()
                        .moveItemToBasket(uid, item);

                Log.d(TAG, "Moved item to basket: " + item.name);

                dismiss();
            }
        });
    }
}