package edu.uga.cs.roommateshopper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import edu.uga.cs.roommateshopper.models.*;

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

    public void addUser(User user) {
        db.child("users")
                .child(user.id)
                .setValue(user);
    }


    public DatabaseReference getUsersRef() {
        return db.child("users");
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

    public void editPrice(ShoppingItem item, double newPrice){
        db.child("shopping_list")
                .child(item.id)
                .child("price")
                .setValue(newPrice);
    }

    public void editName(ShoppingItem item, String newName){
        db.child("shopping_list")
                .child(item.id)
                .child("name")
                .setValue(newName);
    }

    public void editQuantity(ShoppingItem item, int newQuantity){
        db.child("shopping_list")
                .child(item.id)
                .child("quantity")
                .setValue(newQuantity);
    }

    public DatabaseReference getShoppingListRef() {
        return db.child("shopping_list");
    }


    public void addItemToBasket(String uid, ShoppingItem item) {
        db.child("shopping_basket")
                .child(uid)
                .child(item.id)
                .setValue(item);
    }

    public void removeItemFromBasket(String uid, String itemId) {
        db.child("shopping_basket")
                .child(uid)
                .child(itemId)
                .removeValue();
    }

    public DatabaseReference getBasketRef(String uid) {
        return db.child("shopping_basket").child(uid);
    }


    public void moveItemToBasket(String uid, ShoppingItem item) {
        addItemToBasket(uid, item);
        deleteShoppingItem(item.id);
    }


    public void moveItemToShoppingList(String uid, ShoppingItem item) {
        removeItemFromBasket(uid, item.id);
        addShoppingItem(item);

    }



    public void addPurchase(Purchase purchase) {
        String key = db.child("purchases").push().getKey();
        purchase.id = key;

        db.child("purchases")
                .child(key)
                .setValue(purchase);
    }

    public DatabaseReference getPurchasesRef() {
        return db.child("purchases");
    }



    public void checkoutBasket(String uid,
                               String userName,
                               Map<String, ShoppingItem> items,
                               double totalPrice,
                               long timestamp) {

        String purchaseId = db.child("purchases").push().getKey();

        Purchase purchase = new Purchase();
        purchase.id = purchaseId;
        purchase.purchasedBy = uid;
        purchase.purchasedByName = userName;
        purchase.totalPrice = totalPrice;
        purchase.timestamp = timestamp;
        purchase.items = items;

        db.child("purchases")
                .child(purchaseId)
                .setValue(purchase);

        db.child("shopping_basket")
                .child(uid)
                .removeValue();
    }



    public void removeItemFromPurchase(String purchaseId,
                                       String itemId,
                                       ShoppingItem item) {

        db.child("purchases")
                .child(purchaseId)
                .child("items")
                .child(itemId)
                .removeValue();

        addShoppingItem(item);
    }


    public void updatePurchasePrice(String purchaseId, double newPrice) {
        db.child("purchases")
                .child(purchaseId)
                .child("totalPrice")
                .setValue(newPrice);
    }


    public void clearPurchases() {
        db.child("purchases").removeValue();
    }



    public void addSettlement(Settlement settlement) {
        String key = db.child("settlements").push().getKey();
        settlement.id = key;

        db.child("settlements")
                .child(key)
                .setValue(settlement);
    }



    public void removeItemFromPurchaseAndUpdate(String purchaseId, ShoppingItem item) {

        DatabaseReference purchaseRef = db.child("purchases").child(purchaseId);

        purchaseRef.child("items").child(item.id).removeValue();

        purchaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Purchase purchase = snapshot.getValue(Purchase.class);

                if (purchase == null) return;

                if (purchase.items == null || purchase.items.isEmpty()) {

                    purchaseRef.removeValue();

                    Log.d("FirebaseDBHelper", "Purchase deleted (empty)");

                } else {

                    double newTotal = 0;

                    for (ShoppingItem i : purchase.items.values()) {
                        newTotal += i.price;
                    }

                    newTotal = newTotal * 1.08;

                    purchaseRef.child("totalPrice").setValue(newTotal);

                    Log.d("FirebaseDBHelper", "Purchase updated. New total: " + newTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDBHelper", error.getMessage());
            }
        });

        db.child("shopping_list").child(item.id).setValue(item);
    }

    public DatabaseReference getSettlementsRef() {
        return db.child("settlements");
    }
}