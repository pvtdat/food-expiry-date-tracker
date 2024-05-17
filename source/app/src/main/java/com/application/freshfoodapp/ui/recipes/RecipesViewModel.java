package com.application.freshfoodapp.ui.recipes;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.api.EdamamRecipeService;
import com.application.freshfoodapp.api.RecipeServiceProvider;
import com.application.freshfoodapp.api.SpoonacularServiceAdapter;
import com.application.freshfoodapp.model.Product;
import com.application.freshfoodapp.model.Restriction;
import com.application.freshfoodapp.model.RootObjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RecipesViewModel extends ViewModel {
    FirebaseFirestore db;
    private MutableLiveData<List<RootObjectModel>> mRecipes;
    private RecipeServiceProvider recipeServiceProvider;
    private List<String> restrictions;
    private static FirebaseUser curUser;
    private String query = "";
    private String healthy = "";
    private String[] healthyLabels;
    public RecipesViewModel() {
        this.mRecipes = new MutableLiveData<>();
        prepareData();
    }

    public MutableLiveData<List<RootObjectModel>> getRecipes() {
        return mRecipes;
    }
    public void updateRecipes(List<RootObjectModel> newRecipes) {
        mRecipes.setValue(newRecipes);
    }

    private void prepareData() {
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        List<Product> products = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("products")
            .whereEqualTo("kitchenId", MainActivity.getCurKitchenId())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Product product;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        product = document.toObject(Product.class);
                        product.setProductId(document.getId());
                        products.add(product);

                        // Create a query for search recipes api
                        List<String> ingredients = product.getProductCategorizes();
                        for(int i=0; i<ingredients.size(); i++) {
                            query += ingredients.get(i) + ",";
                        }
                    }

                    // Call api recipes from some ingredients in ProductCategorizes
                    if(!products.isEmpty()) {
                          if(products.size() > 3) {
                              query = deleteDuplicates(query.substring(0, query.length()-1));
                          } else {
                              query = query.substring(0, query.length()-1);
                          }
                    }
                restrictions = new ArrayList<>();
                db.collection("restrictions")
                    .whereEqualTo("ownerId", curUser.getUid())
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Restriction restriction;
                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                restriction = document.toObject(Restriction.class);
                                restriction.setOwnerId(document.getId());
                                restrictions = restriction.getRestrictions();
                            }

                            if(!restrictions.isEmpty()) {
                                for(int i=0; i<restrictions.size(); i++) {
                                    healthy += restrictions.get(i) + ",";
                                }
                                // Call api recipes from some healthyLabels in Restrictions
                                healthyLabels = new String[restrictions.size()];
                                healthyLabels = healthy.split(",");
                            }

                            if(!query.isEmpty()) {
                                // Existing API service
                                recipeServiceProvider = new EdamamRecipeService();
                                recipeServiceProvider.fetchRecipes(this, query, healthyLabels);

                                // New API service with different JSON format
                                recipeServiceProvider = new SpoonacularServiceAdapter();
                                recipeServiceProvider.fetchRecipes(this, query);

                            } else {
                                mRecipes.postValue(null);
                            }
                        }
                    });
                }
            });
    }
    private String deleteDuplicates(String inputString) {
        Random rand = new Random();
        String[] values = inputString.split(",");
        randomizeArray(values);

        String[] valuesDeclineNumberIngredients = new String[3];
        for (int i = 0; i < valuesDeclineNumberIngredients.length; i++) {
            int index = rand.nextInt(values.length - 1);
            valuesDeclineNumberIngredients[i] = values[index];
        }

        HashSet<String> seen = new HashSet<>();
        StringBuilder result = new StringBuilder();

        for (String value : valuesDeclineNumberIngredients) {
            if (!seen.contains(value)) {
                seen.add(value);
                if (result.length() > 0) {
                    result.append(",");
                }
                result.append(value);
            }
        }

        return result.toString();
    }

    private void randomizeArray(String[] array) {
        Random rand = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);

            // Swap elements at i and index
            String temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }
}