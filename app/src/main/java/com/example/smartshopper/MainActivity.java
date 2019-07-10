package com.example.smartshopper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopper.data.ScannedProductRepository;
import com.example.smartshopper.data.database.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ScannedProductRepository.GetMultipleProductCallback,
        RecyclerProductAdapter.LongClickCallback {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private ScannedProductRepository scannedProductRepository;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerProductAdapter adapter;
    private List<Product> products = new ArrayList<>();
    private int selectedProductCounter = 0;
    private FloatingActionButton fabCompare;
    private Switch switchVegan;
    private Switch switchLaktose;
    private Switch switchGluten;
    private Switch switchNut;
    private String isVegan, isLaktose, isGluten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        switchVegan = findViewById(R.id.switchVegan);
        switchVegan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: set filter value for search results and future searches
                if(isChecked)
                {
                    isVegan = "vegan";
                    adapter.getFilter().filter(isVegan);
                }
                else
                {
                    isVegan = "";
                }
            }
        });

        switchLaktose = findViewById(R.id.switchLaktose);
        switchLaktose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: set filter value for search results and future searches
                if(isChecked)
                {
                    isLaktose = "laktosefrei";
                    adapter.getFilter().filter(isLaktose);
                }
                else
                {
                    isLaktose = "";
                }
            }
        });

        switchGluten = findViewById(R.id.switchGluten);
        switchGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: set filter value for search results and future searches
                if(isChecked)
                {
                    isGluten = "glutenfrei";
                    adapter.getFilter().filter(isGluten);
                }
                else
                {
                    isGluten = "";
                }
            }
        });

        switchNut = findViewById(R.id.switchNut);
        switchNut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: set filter value for search results and future searches
            }
        });

        FloatingActionButton fabCamera = findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanActivity();
            }
        });

        fabCompare = findViewById(R.id.fabCompare);
        fabCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProductCompareActivity();
            }
        });

        scannedProductRepository = new ScannedProductRepository(this);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerProductAdapter(products, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedProductCounter = 0;
        updateCompareFab();
        scannedProductRepository.getAllProducts(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (REQUEST_CAMERA_PERMISSION == requestCode &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanActivity();
        }
    }

    @Override
    public void onGetMultipleProducts(List<Product> products) {
        this.products = products;
        this.products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return (int) (o2.scanned - o1.scanned);
            }
        });

        adapter.setProducts(this.products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClickUpdate(boolean isSelected) {
        if (isSelected) {
            selectedProductCounter++;
        } else {
            selectedProductCounter--;
        }
        updateCompareFab();
    }

    private void updateCompareFab() {
        if (selectedProductCounter > 1) {
            fabCompare.show();
        } else {
            fabCompare.hide();
        }
    }

    private void startScanActivity() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void startProductCompareActivity() {
        List<Long> eansList = new ArrayList<>();

        for (Product product : products) {
            if (product.isSelected) {
                eansList.add(product.ean);
            }
        }

        long[] eans = new long[eansList.size()];
        for (int i = 0; i < eansList.size(); i++) {
            eans[i] = eansList.get(i);
        }

        Intent intent = new Intent(this, ProductCompareActivity.class);
        intent.putExtra(ProductCompareActivity.EXTRA_EANS, eans);
        startActivity(intent);
    }
}
