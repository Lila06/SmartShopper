package com.example.smartshopper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.data.database.Product;

import java.util.List;

public class RecyclerProductCompareApdater extends RecyclerView.Adapter<RecyclerProductCompareApdater.ProductCompareViewHolder>{

    private List<Product> products;

    RecyclerProductCompareApdater(List<Product> products) {
        this.products = products;
    }


    @NonNull
    @Override
    public ProductCompareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_product_compare_view, parent, false);
        return new ProductCompareViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCompareViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.productTitle.setText(product.getDetailName());

        StringBuilder allergenDescription = new StringBuilder();
        for (String description : product.getAllergen()) {
            allergenDescription.append("\u25CF ");
            allergenDescription.append(description);
            allergenDescription.append("\n");
        }
        holder.productAllergen.setText(allergenDescription.toString().trim());

        StringBuilder packagingDescription = new StringBuilder();
        for (String description : product.getPackaging()) {
            packagingDescription.append("\u25CF ");
            packagingDescription.append(description);
            packagingDescription.append("\n");
        }
        holder.productPackaging.setText(packagingDescription.toString().trim());

        if (position % 2 == 0) {
            holder.productBackground.setVisibility(View.INVISIBLE);
        } else {
            holder.productBackground.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    static class ProductCompareViewHolder extends RecyclerView.ViewHolder {

        View layout;
        TextView productTitle;
        TextView productAllergen;
        TextView productPackaging;
        View productBackground;

        public ProductCompareViewHolder(@NonNull View view) {
            super(view);
            layout = view;
            productTitle = view.findViewById(R.id.productCompareName);
            productAllergen = view.findViewById(R.id.productCompareAllergenValue);
            productPackaging = view.findViewById(R.id.productComparePackagingValue);
            productBackground = view.findViewById(R.id.productCompareBackground);
        }


    }
}
