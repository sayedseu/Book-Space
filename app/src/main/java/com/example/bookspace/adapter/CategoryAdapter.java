package com.example.bookspace.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.model.model_class.local.ViewCategory;
import com.example.bookspace.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<ViewCategory> categoryList;
    private NavController navController;

    public CategoryAdapter(NavController navController) {
        this.navController = navController;
        categoryList = Utils.getViewCategoryList();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ViewCategory viewCategory = categoryList.get(position);
        holder.circleImageView.setImageResource(viewCategory.getImageId());
        holder.textView.setText(viewCategory.getName());
        holder.parent.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("category", viewCategory.getName());
            navController.navigate(R.id.action_categoryFragment_to_categoryDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView textView;
        private MaterialCardView parent;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.categoryName);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
