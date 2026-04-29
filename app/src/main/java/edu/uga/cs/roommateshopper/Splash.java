package edu.uga.cs.roommateshopper;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigationrail.NavigationRailView;


/**
 * Main application container fragment that manages navigation between different features.
 * It uses a BottomNavigationView for portrait orientation and a NavigationRailView
 * for landscape orientation to host various fragments like ShoppingList, ShoppingBasket,
 * RecentlyPurchased, and more.
 */
public class Splash extends Fragment {

    BottomNavigationView bottomNavigationView;
    NavigationRailView navigationRail;

    private int selectedNavItem = R.id.action_shoppingList;

    /**
     * Default constructor for the Splash fragment.
     */
    public Splash() {}

    /**
     * Static factory method to create a new instance of the Splash fragment.
     *
     * @return A new instance of fragment Splash.
     */
    public static Splash newInstance() {
        Splash fragment = new Splash();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Called when the fragment is first created.
     * Restores the selected navigation item if available from savedInstanceState.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedNavItem = savedInstanceState.getInt("selected_nav", R.id.action_shoppingList);
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

        return inflater.inflate(R.layout.fragment_splash, container, false);

    }

    /**
     * Called immediately after onCreateView has returned.
     * Sets up the navigation view (BottomNavigationView or NavigationRailView) based on the current orientation.
     *
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        FragmentManager manager = getChildFragmentManager();

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            navigationRail = view.findViewById(R.id.navigationRail);
            navigationRail.setOnItemSelectedListener(item -> {
                selectedNavItem = item.getItemId();
                if (selectedNavItem == R.id.action_shoppingList) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new ShoppingList()).commit();
                } else if (selectedNavItem == R.id.action_shoppingBasket) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new ShoppingBasketFragment()).commit();
                } else if (selectedNavItem == R.id.action_recentlyPurchased) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new RecentlyPurchasedFragment()).commit();
                } else if (selectedNavItem == R.id.action_prevPurchases) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new PrevPurchasesFragment()).commit();
                } else if (selectedNavItem == R.id.action_roommates) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new RoommateFragment()).commit();
                } else if (selectedNavItem == R.id.action_profile) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new ProfileFragment()).commit();
                }
                return true;
            });

            navigationRail.setSelectedItemId(selectedNavItem);
        } else if ( orientation == Configuration.ORIENTATION_PORTRAIT){
            bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                selectedNavItem = item.getItemId();
                if (selectedNavItem == R.id.action_shoppingList) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new ShoppingList()).commit();
                } else if (selectedNavItem == R.id.action_shoppingBasket) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new ShoppingBasketFragment()).commit();
                } else if (selectedNavItem == R.id.action_recentlyPurchased) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new RecentlyPurchasedFragment()).commit();
                } else if (selectedNavItem == R.id.action_prevPurchases) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new PrevPurchasesFragment()).commit();
                } else if (selectedNavItem == R.id.action_roommates) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new RoommateFragment()).commit();
                } else if (selectedNavItem == R.id.action_profile) {
                    manager.beginTransaction().replace(R.id.fragmentContainerView2, new ProfileFragment()).commit();
                }
                return true;
            });

            bottomNavigationView.setSelectedItemId(selectedNavItem);
        }

    }

    /**
     * Called to retrieve per-instance state from a fragment before being killed so that
     * the state can be restored in onCreate.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected_nav", selectedNavItem);
    }

}
