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

/**
 * Helper class for Firebase Database operations.
 * Provides methods to interact with users, shopping items, baskets, purchases, and settlements.
 */
public class FirebaseDBHelper {

    private static FirebaseDBHelper instance;
    private final DatabaseReference db;

    /**
     * Private constructor for singleton pattern.
     */
    private FirebaseDBHelper() {
        db = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Returns the singleton instance of FirebaseDBHelper.
     * @return The singleton instance.
     */
    public static FirebaseDBHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDBHelper();
        }
        return instance;
    }

    /**
     * Adds a user to the database.
     * @param user The user object to add.
     */
    public void addUser(User user) {
        db.child("users")
                .child(user.id)
                .setValue(user);
    }

    /**
     * Gets the reference to the users node.
     * @return DatabaseReference for users.
     */
    public DatabaseReference getUsersRef() {
        return db.child("users");
    }

    /**
     * Adds a shopping item to the global shopping list.
     * @param item The item to add.
     */
    public void addShoppingItem(ShoppingItem item) {
        String key = db.child("shopping_list").push().getKey();
        item.id = key;

        db.child("shopping_list")
                .child(key)
                .setValue(item);
    }

    /**
     * Deletes a shopping item from the global shopping list.
     * @param itemId The ID of the item to delete.
     */
    public void deleteShoppingItem(String itemId) {
        db.child("shopping_list")
                .child(itemId)
                .removeValue();
    }

    /**
     * Updates the price of a shopping item.
     * @param item The item to update.
     * @param newPrice The new price value.
     */
    public void editPrice(ShoppingItem item, double newPrice){
        db.child("shopping_list")
                .child(item.id)
                .child("price")
                .setValue(newPrice);
    }

    /**
     * Updates the name of a shopping item.
     * @param item The item to update.
     * @param newName The new name string.
     */
    public void editName(ShoppingItem item, String newName){
        db.child("shopping_list")
                .child(item.id)
                .child("name")
                .setValue(newName);
    }

    /**
     * Updates the quantity of a shopping item.
     * @param item The item to update.
     * @param newQuantity The new quantity value.
     */
    public void editQuantity(ShoppingItem item, int newQuantity){
        db.child("shopping_list")
                .child(item.id)
                .child("quantity")
                .setValue(newQuantity);
    }

    /**
     * Gets the reference to the global shopping list node.
     * @return DatabaseReference for shopping list.
     */
    public DatabaseReference getShoppingListRef() {
        return db.child("shopping_list");
    }

    /**
     * Adds an item to a user's shopping basket.
     * @param uid The unique identifier of the user.
     * @param item The item to add.
     */
    public void addItemToBasket(String uid, ShoppingItem item) {
        db.child("shopping_basket")
                .child(uid)
                .child(item.id)
                .setValue(item);
    }

    /**
     * Removes an item from a user's shopping basket.
     * @param uid The unique identifier of the user.
     * @param itemId The ID of the item to remove.
     */
    public void removeItemFromBasket(String uid, String itemId) {
        db.child("shopping_basket")
                .child(uid)
                .child(itemId)
                .removeValue();
    }

    /**
     * Gets the reference to a user's shopping basket node.
     * @param uid The unique identifier of the user.
     * @return DatabaseReference for the user's basket.
     */
    public DatabaseReference getBasketRef(String uid) {
        return db.child("shopping_basket").child(uid);
    }

    /**
     * Moves an item from the global shopping list to a user's basket.
     * @param uid The unique identifier of the user.
     * @param item The item to move.
     */
    public void moveItemToBasket(String uid, ShoppingItem item) {
        addItemToBasket(uid, item);
        deleteShoppingItem(item.id);
    }

    /**
     * Moves an item from a user's basket back to the global shopping list.
     * @param uid The unique identifier of the user.
     * @param item The item to move.
     */
    public void moveItemToShoppingList(String uid, ShoppingItem item) {
        removeItemFromBasket(uid, item.id);
        addShoppingItem(item);

    }

    /**
     * Gets the reference to the purchases node.
     * @return DatabaseReference for purchases.
     */
    public DatabaseReference getPurchasesRef() {
        return db.child("purchases");
    }

    /**
     * Records a purchase by moving items from a user's basket to the purchases node and clearing the basket.
     * @param uid The unique identifier of the user who made the purchase.
     * @param userName The name of the user who made the purchase.
     * @param items A map of items included in the purchase.
     * @param totalPrice The total price of the purchase.
     * @param timestamp The time the purchase was made.
     */
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

    /**
     * Updates the total price of a purchase.
     * @param purchaseId The unique identifier of the purchase.
     * @param newPrice The new total price.
     */
    public void updatePurchasePrice(String purchaseId, double newPrice) {
        db.child("purchases")
                .child(purchaseId)
                .child("totalPrice")
                .setValue(newPrice);
    }

    /**
     * Clears all purchase records from the database.
     */
    public void clearPurchases() {
        db.child("purchases").removeValue();
    }

    /**
     * Adds a settlement record to the database.
     * @param settlement The settlement object to add.
     */
    public void addSettlement(Settlement settlement) {
        String key = db.child("settlements").push().getKey();
        settlement.id = key;

        db.child("settlements")
                .child(key)
                .setValue(settlement);
    }

    /**
     * Removes an item from a purchase record, updates the purchase total, and moves the item back to the shopping list.
     * @param purchaseId The unique identifier of the purchase.
     * @param item The item to remove from the purchase.
     */
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

    /**
     * Gets the reference to the settlements node.
     * @return DatabaseReference for settlements.
     */
    public DatabaseReference getSettlementsRef() {
        return db.child("settlements");
    }
}