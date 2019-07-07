package com.example.smartshopper;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartshopper.data.ScannedProductRepository;
import com.example.smartshopper.data.database.Product;

import java.util.List;

public class ProductCompareActivity extends AppCompatActivity implements
        ScannedProductRepository.GetMultipleProductCallback {

    public static final String EXTRA_EANS = "com.example.smartshopper.ProductCompareActivity.eans";

    private ScannedProductRepository scannedProductRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_compare);

        long[] eans = getIntent().getLongArrayExtra(EXTRA_EANS);
        scannedProductRepository = new ScannedProductRepository(this);
        scannedProductRepository.getProducts(eans, this);

    }

    @Override
    public void onGetMultipleProducts(List<Product> products) {
        for (Product product : products) {
            //TODO remove only for test
            Log.e("Debug", "Product: " + product.ean + " name: " + product.getDetailName());
        }

        //TODO show products
    }
}
