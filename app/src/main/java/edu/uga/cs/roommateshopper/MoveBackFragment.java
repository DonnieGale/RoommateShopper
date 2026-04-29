package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * DialogFragment that allows users to move an item from their shopping basket back to the main shopping list.
 * This is useful if a user decides not to purchase an item they previously selected.
 */
public class MoveBackFragment extends DialogFragment {

    Button button;
    ShoppingItem item;

    /**
     * Default constructor for the MoveBackFragment.
     */
    public MoveBackFragment() {}

    /**
     * Static factory method to create a new instance of the MoveBackFragment with a specific shopping item.
     *
     * @param item The shopping item to be moved back to the main list.
     * @return A new instance of fragment MoveBackFragment.
     */
    public static MoveBackFragment newInstance(ShoppingItem item) {
        MoveBackFragment fragment = new MoveBackFragment();

        Bundle args = new Bundle();
        args.putSerializable("item", item);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is first created.
     * Retrieves the shopping item from the fragment arguments.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            item = (ShoppingItem) getArguments().getSerializable("item");
        }
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_move_back, container, false);
    }

    /**
     * Called immediately after onCreateView has returned.
     * Initializes the UI components and sets up the click listener for the move back button.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.button3);

        if (item == null) {
            Log.e("MoveBackFragment", "Item is null after recreation");
            dismiss();
            return;
        }

        button.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDBHelper.getInstance()
                    .moveItemToShoppingList(uid, item);

            dismiss();
        });
    }
}
