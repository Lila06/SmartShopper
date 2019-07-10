package com.example.smartshopper;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.data.ScannedProductRepository;
import com.example.smartshopper.data.database.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductCompareActivity extends AppCompatActivity implements
        ScannedProductRepository.GetMultipleProductCallback {

    public static final String EXTRA_EANS = "com.example.smartshopper.ProductCompareActivity.eans";

    private ScannedProductRepository scannedProductRepository;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerProductCompareApdater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_compare);

        long[] eans = getIntent().getLongArrayExtra(EXTRA_EANS);
        scannedProductRepository = new ScannedProductRepository(this);
        scannedProductRepository.getProducts(eans, this);

        recyclerView = findViewById(R.id.productCompareRecyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerProductCompareApdater(new ArrayList<Product>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetMultipleProducts(List<Product> products) {
        for (Product product : products) {
            Log.e("ProductCompareActivity", "Product: " + product.ean + " name: " + product.getDetailName());
        }

        adapter.setProducts(products);
        adapter.notifyDataSetChanged();
    }
}
