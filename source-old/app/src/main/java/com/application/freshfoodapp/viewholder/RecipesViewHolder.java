package com.application.freshfoodapp.viewholder;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.databinding.RecipesItemCardBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.carousel.MaskableFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;

public class RecipesViewHolder extends RecyclerView.ViewHolder {
    private ImageView recipesImage;
    private TextView dishName, calories, totalTime;
    private MaskableFrameLayout itemView;

    public RecipesViewHolder(RecipesItemCardBinding binding) {
        super(binding.getRoot());
        recipesImage = binding.dishImageView;
        dishName = binding.dishNameTextView;
        calories = binding.caloriesTextView;
        totalTime = binding.totalTimeTextView;
        itemView = binding.card;
    }

    public MaskableFrameLayout getItemView() {
        return itemView;
    }

    public void setItemView(MaskableFrameLayout itemView) {
        this.itemView = itemView;
    }

    public ImageView getRecipesImage() {
        return recipesImage;
    }

    public void setRecipesImage(ImageView recipesImage) {
        this.recipesImage = recipesImage;
    }

    public TextView getDishName() {
        return dishName;
    }

    public void setDishName(TextView dishName) {
        this.dishName = dishName;
    }

    public TextView getCalories() {
        return calories;
    }

    public void setCalories(TextView calories) {
        this.calories = calories;
    }

    public TextView getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TextView totalTime) {
        this.totalTime = totalTime;
    }
}
