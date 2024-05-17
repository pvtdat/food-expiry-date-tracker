package com.application.freshfoodapp.adapter;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.R;
import com.application.freshfoodapp.databinding.ProductItemCardBinding;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.ui.kitchen.productcommand.RemoteControl;
import com.application.freshfoodapp.ui.productdetail.ProductDetailFragment;
import com.application.freshfoodapp.viewholder.KitchenViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class KitchenTypeAdapter extends RecyclerView.Adapter<KitchenViewHolder> {
    private List<Product> data = new ArrayList<>();
    RemoteControl remoteControl = new RemoteControl();
    FirebaseFirestore db = null;

    @NonNull
    @Override
    public KitchenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemCardBinding binding = ProductItemCardBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new KitchenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull KitchenViewHolder holder, int position) {
        int curPosition = holder.getAdapterPosition();
        Product curProduct = data.get(curPosition);
        holder.getProductNameTextView().setText(curProduct.getTitle());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, uuuu");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(curProduct.getExpiryDate());
        LocalDate expiryDate = convertToLocalDateViaInstant(calendar.getTime());
        holder.getExpiryDateTextView().setText(expiryDate.format(formatter));
        long duration = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

        if ((duration <= 3 && expiryDate.isAfter(LocalDate.now())) ||
                (expiryDate.isBefore(LocalDate.now()))) {
            int color = ContextCompat.getColor(holder.getItemView().getContext(), R.color.md_theme_light_error);
            holder.getExpiryDateTextView().setTextColor(color);
            holder.getProductNameTextView().setTextColor(color);
        }


        holder.getShowMoreBtn().setOnClickListener(v ->  {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.more_option_product, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.deleteProduct) {
                    int productIndex = data.indexOf(curProduct);
                    remoteControl.deleteBtnWasPushed(curProduct, data, this, productIndex);
                    Snackbar sbUndo = Snackbar.make(v, "Successfully deleted " + curProduct.getTitle(), Snackbar.LENGTH_LONG);
                    sbUndo.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Product product = remoteControl.undoBtnWasPushed();
                            if(!data.contains(product)) {
                                data.add(productIndex, product);
                                notifyItemInserted(productIndex);
                            }
                        }
                    }).show();
                } else if (item.getItemId() == R.id.cloneableProduct) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(android.widget.DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    Calendar selectedDate = Calendar.getInstance();
                                    selectedDate.set(year, monthOfYear, dayOfMonth);

                                    // Create a clone product
                                    Product productCloned = (Product) curProduct.Clone();

                                    // Reset Id for a new product
                                    productCloned.setProductId(null);

                                    // Set new value in clone product (expiry date)
                                    productCloned.setExpiryDate(selectedDate.getTimeInMillis());

                                    db = FirebaseFirestore.getInstance();
                                    db.collection("products")
                                            .add(productCloned)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        // Get the clone product after completely adding
                                                        DocumentReference documentReference = task.getResult();

                                                        // Get index added of clone product
                                                        int indexLast = data.size();

                                                        // Add Id (firebase) to clone product to add on list<Product>
                                                        String newId = documentReference.getId();
                                                        productCloned.setProductId(newId);
                                                        data.add(productCloned);
                                                        notifyItemInserted(indexLast);
                                                    }
                                                }
                                            });
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                    datePickerDialog.show();
                }
                return true;
            });
            popup.show();
        });

        holder.getItemView().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString(ProductDetailFragment.ARG_PRODUCT_DETAIL, curProduct.getProductId());
            MainActivity.getNavController().navigate(R.id.nav_product_detail, args);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateProductList(List<Product> data) {
        this.data = data;
        notifyItemRangeChanged(0, data.size());
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}