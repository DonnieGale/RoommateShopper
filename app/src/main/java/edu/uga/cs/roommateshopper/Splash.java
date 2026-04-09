package edu.uga.cs.roommateshopper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Splash#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Splash extends Fragment {

    BottomNavigationView bottomNavigationView;

    public Splash() {
        // Required empty public constructor
    }


    public static Splash newInstance() {
        Splash fragment = new Splash();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_splash, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        FragmentManager manager = getActivity().getSupportFragmentManager();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_shoppingList) {
                manager.beginTransaction().replace(R.id.fragmentContainerView2, new ShoppingList()).commit();
            } else if (itemId == R.id.action_recentlyPurchased) {
                //manager.beginTransaction().replace(R.id.fragmentContainerView2, new RecentlyPurchased()).commit();
            } else if (itemId == R.id.action_prevPurchases) {
                //manager.beginTransaction().replace(R.id.fragmentContainerView2, new PrevPurchases()).commit();
            } else if (itemId == R.id.action_roommates) {
                //manager.beginTransaction().replace(R.id.fragmentContainerView2, new Roommates()).commit();
            } else if (itemId == R.id.action_profile) {
                //manager.beginTransaction().replace(R.id.fragmentContainerView2, new ProfileFragment()).commit();
            }
            return true;
        });

    }
}