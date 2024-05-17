package com.application.freshfoodapp.ui.sharing.kitchenproxy;

import android.content.Context;
import android.view.View;

import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public interface KitchenService {
    void accessKitchen(Context context, String kitchenId);
    void removeProductItem(
            Product product,
            List<Product> data,
            KitchenTypeAdapter adapter,
            int deletePosition);
}
