package com.example.smartshopper;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartshopper.data.ScannedProductRepository;
import com.example.smartshopper.data.database.Product;

public class ScanDetailsActivity extends AppCompatActivity
        implements ScannedProductRepository.GetProductCallback {

    public static final String EXTRA_EAN = "com.example.smartshopper.ScanDetailsActivity.ean";
    private static final String TAG = "ScanDetailsActivity";
    private ScannedProductRepository scannedProductRepository;
    private TextView scanDetailsNameValue;
    private TextView scanResultDescriptionValue;
    private TextView scanDetailsAllergenValue;
    private TextView scanDetailsPackagingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

        scannedProductRepository = new ScannedProductRepository(this);

        long ean = getIntent().getLongExtra(EXTRA_EAN, -1L);

        scanDetailsNameValue = findViewById(R.id.scanDetailsNameValue);
        scanResultDescriptionValue = findViewById(R.id.scanResultDescriptionValue);
        scanDetailsAllergenValue = findViewById(R.id.scanDetailsAllergenValue);
        scanDetailsPackagingValue = findViewById(R.id.scanDetailsPackagingValue);

        scannedProductRepository.getProduct(ean, this);
    }

    @Override
    public void onGetProduct(Product product) {
        Log.d(TAG, "show product ean: " + product.ean + "\ndata: " + product.data + "\nscanned: " + product.scanned);

        scanDetailsNameValue.setText(product.getDetailName());
        scanResultDescriptionValue.setText(product.getDescription());

        StringBuilder allergenDescription = new StringBuilder();
        for (String description : product.getAllergen()) {
            allergenDescription.append("\u25CF ");
            allergenDescription.append(description);
            allergenDescription.append("\n");
        }
        scanDetailsAllergenValue.setText(allergenDescription.toString().trim());

        StringBuilder packagingDescription = new StringBuilder();
        for (String description : product.getPackaging()) {
            packagingDescription.append("\u25CF ");
            packagingDescription.append(description);
            packagingDescription.append("\n");
        }
        scanDetailsPackagingValue.setText(packagingDescription.toString().trim());
    }
}
