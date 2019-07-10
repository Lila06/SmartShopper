package com.example.smartshopper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.data.database.Product;

import java.util.ArrayList;
import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.ProductViewHolder> implements Filterable {

    private List<Product> products;
    private List<Product> filteredProducts;
    private LongClickCallback longClickCallback;

    RecyclerProductAdapter(List<Product> products, LongClickCallback longClickCallback) {
        this.products = products;
        this.filteredProducts = new ArrayList<>(this.products);
        this.longClickCallback = longClickCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.reycler_product_view, parent, false);
        return new ProductViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.productTitle.setText(product.getDetailName());
        holder.productEan.setText(Long.toString(product.ean));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProductDetailActivity(product.ean, v.getContext());
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                product.isSelected = !product.isSelected;
                if (product.isSelected) {
                    holder.productSelectedIndicator.setVisibility(View.VISIBLE);
                } else {
                    holder.productSelectedIndicator.setVisibility(View.INVISIBLE);
                }
                longClickCallback.onLongClickUpdate(product.isSelected);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        final Product product = products.get(position);
        if (product.isSelected) {
            holder.productSelectedIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.productSelectedIndicator.setVisibility(View.INVISIBLE);
        }

    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        View layout;
        TextView productTitle;
        TextView productEan;
        View productSelectedIndicator;

        ProductViewHolder(@NonNull View view) {
            super(view);
            layout = view;
            productTitle = view.findViewById(R.id.productTitleValue);
            productEan = view.findViewById(R.id.productEanValue);
            productSelectedIndicator = view.findViewById(R.id.productSelectedIndicator);
            productSelectedIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void startProductDetailActivity(long ean, Context context) {
        Intent intent = new Intent(context, ScanDetailsActivity.class);
        intent.putExtra(ScanDetailsActivity.EXTRA_EAN, ean);
        context.startActivity(intent);
    }

    public interface LongClickCallback {
        void onLongClickUpdate(boolean isSelected);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Product> filteredList = new ArrayList<>();
                if(constraint == null || constraint.length() == 0)
                {
                    filteredList.addAll(products);
                }
                else
                {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for(Product item : products){
                        for(String allergen : item.getAllergen()){
                            if(allergen.toLowerCase().contains(filterPattern)){
                                filteredList.add(item);
                            }
                        }
                    }
                }
               FilterResults results = new FilterResults();
               results.values = filteredList;
               return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                products.clear();
                products.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
