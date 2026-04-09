package edu.uga.cs.roommateshopper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.roommateshopper.models.Purchase;
import edu.uga.cs.roommateshopper.models.Settlement;
import edu.uga.cs.roommateshopper.models.ShoppingItem;

public class FirebaseDBHelper {

    private static FirebaseDBHelper instance;
    private final DatabaseReference db;

    private FirebaseDBHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseDBHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDBHelper();
        }
        return instance;
    }



    public void addShoppingItem(ShoppingItem item) {
        String key = db.child("shopping_list").push().getKey();
        item.id = key;

        db.child("shopping_list")
                .child(key)
                .setValue(item);
    }


    public void deleteShoppingItem(String itemId) {
        db.child("shopping_list")
                .child(itemId)
                .removeValue();
    }


    public void addPurchase(Purchase purchase) {
        String key = db.child("purchases").push().getKey();
        purchase.id = key;

        db.child("purchases")
                .child(key)
                .setValue(purchase);
    }


    public void addSettlement(Settlement settlement) {
        String key = db.child("settlements").push().getKey();
        settlement.id = key;

        db.child("settlements")
                .child(key)
                .setValue(settlement);
    }


    public void clearPurchases() {
        db.child("purchases").removeValue();
    }






    public DatabaseReference getShoppingListRef() {
        return db.child("shopping_list");
    }



    
}
