package edu.uga.cs.roommateshopper;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import edu.uga.cs.roommateshopper.models.ShoppingItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Splash#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Splash extends Fragment {


    //private int selectedNavItem = R.id.home;

    BottomNavigationView bottomNavigationView;
    NavigationRailView navigationRail;

    private int selectedNavItem = R.id.action_shoppingList;

    public Splash() {
        // Required empty public constructor
    }


    public static Splash newInstance() {
        Splash fragment = new Splash();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            selectedNavItem = savedInstanceState.getInt("selected_nav", R.id.action_shoppingList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);

    }

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

            // 🔑 Restore the selected tab (this triggers the correct fragment load)
            bottomNavigationView.setSelectedItemId(selectedNavItem);
        }











    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // 🔑 Save selected tab before rotation
        outState.putInt("selected_nav", selectedNavItem);
    }








}