package edu.uga.cs.roommateshopper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    // =========================
    // 👤 USERS
    // =========================
    public void addUser(User user) {
        db.child("users")
                .child(user.id)
                .setValue(user);
    }


    public DatabaseReference getUsersRef() {
        return db.child("users");
    }

    // =========================
    // 🛒 SHOPPING LIST
    // =========================
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

    public void editPrice(ShoppingItem item, int newPrice){
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

    // =========================
    // 🧺 SHOPPING BASKET (per user)
    // =========================
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

    // 🔄 Move item: shopping_list → basket
    public void moveItemToBasket(String uid, ShoppingItem item) {
        addItemToBasket(uid, item);
        deleteShoppingItem(item.id);
    }

    // 🔄 Move item: basket → shopping_list
    public void moveItemToShoppingList(String uid, ShoppingItem item) {
        removeItemFromBasket(uid, item.id);
        addShoppingItem(item);

    }

    // =========================
    // 🧾 PURCHASES
    // =========================
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



    // 🔄 Checkout: basket → purchase
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

        // Save purchase
        db.child("purchases")
                .child(purchaseId)
                .setValue(purchase);

        // Clear basket
        db.child("shopping_basket")
                .child(uid)
                .removeValue();
    }

    // ❌ Remove item from purchase → back to shopping list
    public void removeItemFromPurchase(String purchaseId,
                                       String itemId,
                                       ShoppingItem item) {

        // Remove from purchase
        db.child("purchases")
                .child(purchaseId)
                .child("items")
                .child(itemId)
                .removeValue();

        // Add back to shopping list
        addShoppingItem(item);
    }

    // ✏️ Update purchase price
    public void updatePurchasePrice(String purchaseId, double newPrice) {
        db.child("purchases")
                .child(purchaseId)
                .child("totalPrice")
                .setValue(newPrice);
    }

    // 🧹 Clear all purchases (after settlement)
    public void clearPurchases() {
        db.child("purchases").removeValue();
    }

    // =========================
    // 💰 SETTLEMENTS
    // =========================
    public void addSettlement(Settlement settlement) {
        String key = db.child("settlements").push().getKey();
        settlement.id = key;

        db.child("settlements")
                .child(key)
                .setValue(settlement);
    }

    public DatabaseReference getSettlementsRef() {
        return db.child("settlements");
    }
}