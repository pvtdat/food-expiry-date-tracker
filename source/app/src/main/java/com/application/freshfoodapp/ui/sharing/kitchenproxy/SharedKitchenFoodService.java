package com.application.freshfoodapp.ui.sharing.kitchenproxy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.ui.sharing.SharedKitchen.SharedKitchenFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SharedKitchenFoodService implements KitchenService {
    FirebaseFirestore db;


    public SharedKitchenFoodService() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void accessKitchen(Context context, String kitchenId) {
        Bundle args = new Bundle();
        args.putString(SharedKitchenFragment.ARG_SHARED_KITCHEN, kitchenId);
        MainActivity.getNavController().navigate(R.id.nav_shared_kitchen, args);
    }

    @Override
    public void removeProductItem(
            Product product,
            List<Product> data,
            KitchenTypeAdapter adapter,
            int deletePosition) {
        db.collection("products")
                .document(product.getProductId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    data.remove(product);
                    adapter.notifyItemRemoved(deletePosition);

                })
                .addOnFailureListener(e -> {
                    Log.w("ERROR", "Error deleting document", e);
                });
    }
}
