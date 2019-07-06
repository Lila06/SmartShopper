package com.example.smartshopper;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartshopper.data.ScannedProductRepository;
import com.example.smartshopper.data.database.Product;

public class ScanDetailsActivity extends AppCompatActivity implements ScannedProductRepository.GetProductCallback {

    public static String EXTRA_EAN = "com.example.smartshopper.ScanDetailsActivity.ean";
    private ScannedProductRepository scannedProductRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

        scannedProductRepository = new ScannedProductRepository(this);

        long ean = getIntent().getLongExtra(EXTRA_EAN, -1L);

        scannedProductRepository.getProduct(ean, this);

    }

    @Override
    public void onGetProduct(Product product) {
        Log.e("Debug", "show product ean: " + product.ean + "\ndata: " + product.data + "\nscanned: " + product.scanned);
    }
}
