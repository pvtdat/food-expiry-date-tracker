package com.application.freshfoodapp.ui.kitchen.productcommand;

import android.util.Log;

import androidx.annotation.NonNull;

import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.ui.sharing.kitchenproxy.KitchenService;
import com.application.freshfoodapp.ui.sharing.kitchenproxy.SharedKitchenProxy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductCommand extends CommandBase {
    Product product;
    String newId;
    private KitchenService kitchenService = new SharedKitchenProxy();
    public ProductCommand(Product product) {
        this.product = product;
    }
    @Override
    protected void executeDelete(Product product,
                                 List<Product> data,
                                 KitchenTypeAdapter adapter,
                                 int deletePosition) {
        kitchenService.removeProductItem(product, data, adapter, deletePosition);
    }
    @Override
    protected void executeAdd() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String productId = product.getProductId();
        if(productId != null && !productId.isEmpty()) {
            db.collection("products")
                    .document(product.getProductId())
                    .set(product);
        } else {
            db.collection("products").add(product);
        }
    }
}