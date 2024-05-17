package com.application.freshfoodapp.ui.kitchen.kitchentype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.freshfoodapp.MainActivity;
import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.databinding.FragmentKitchenTypeBinding;
import com.application.freshfoodapp.utils.GridSpacingItemDecoration;
import com.google.android.material.tabs.TabLayout;

public class TabKitchenTypeFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    private FragmentKitchenTypeBinding binding;
    private TabKitchenTypeViewModel mViewModel;

    KitchenTypeAdapter adapter;
    TabLayout tabLayout;
    Bundle args;

    public static TabKitchenTypeFragment newInstance() {
        return new TabKitchenTypeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentKitchenTypeBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(TabKitchenTypeViewModel.class);
        args = getArguments();
        mViewModel
                .fetchProductsByKitchenType(args.getString(ARG_OBJECT), MainActivity.getCurKitchenId());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new KitchenTypeAdapter();
        mViewModel
                .getProductsByKitchenType()
                .observe(getViewLifecycleOwner(), adapter::updateProductList);
        RecyclerView recyclerView = binding.productRecyclerviewTransform;
        int spanCount = 2;
        int spacing = 32;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}