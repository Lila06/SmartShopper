package com.example.smartshopper;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

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
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String VEGAN_STATUS = "veganStatus";
    private static final String GLUTEN_STATUS = "glutenStatus";
    private static final String LAKTOSE_STATUS = "laktoseStatus";
    private static final String FRUKTOSE_STATUS = "fruktoseStatus";
    private boolean vStatus, gStatus, lStatus, fStatus;

    private ScannedProductRepository scannedProductRepository;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerProductAdapter adapter;
    private List<Product> products = new ArrayList<>();
    private List<Product> allProducts = new ArrayList<>();
    private int selectedProductCounter = 0;
    private FloatingActionButton fabCompare;
    private Switch switchVegan;
    private Switch switchLaktose;
    private Switch switchGluten;
    private Switch switchFruktose;

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
                updateProductList();
                saveData();
            }
        });

        switchLaktose = findViewById(R.id.switchLaktose);
        switchLaktose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProductList();
                saveData();
            }
        });

        switchGluten = findViewById(R.id.switchGluten);
        switchGluten.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProductList();
                saveData();
            }
        });

        switchFruktose = findViewById(R.id.switchFruktose);
        switchFruktose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProductList();
                saveData();
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
                if (selectedProductCounter > 1) {
                    startProductCompareActivity();
                } else {
                    Toast.makeText(MainActivity.this, "Nicht genügend Produkte zum Vergleich ausgewählt", Toast.LENGTH_LONG).show();
                }
            }
        });

        scannedProductRepository = new ScannedProductRepository(this);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerProductAdapter(products, this);
        recyclerView.setAdapter(adapter);

        loadData();
        updateViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedProductCounter = 0;
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
        this.allProducts = products;
        this.allProducts.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return (int) (o2.scanned - o1.scanned);
            }
        });

        updateProductList();
    }

    @Override
    public void onLongClickUpdate(boolean isSelected) {
        if (isSelected) {
            selectedProductCounter++;
        } else {
            selectedProductCounter--;
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

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(VEGAN_STATUS, switchVegan.isChecked());
        editor.putBoolean(GLUTEN_STATUS, switchGluten.isChecked());
        editor.putBoolean(LAKTOSE_STATUS, switchLaktose.isChecked());
        editor.putBoolean(FRUKTOSE_STATUS, switchFruktose.isChecked());

        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        vStatus = sharedPreferences.getBoolean(VEGAN_STATUS, false);
        gStatus = sharedPreferences.getBoolean(GLUTEN_STATUS, false);
        lStatus = sharedPreferences.getBoolean(LAKTOSE_STATUS, false);
        fStatus = sharedPreferences.getBoolean(FRUKTOSE_STATUS, false);
    }

    private void updateViews() {
        switchVegan.setChecked(vStatus);
        switchGluten.setChecked(gStatus);
        switchLaktose.setChecked(lStatus);
        switchFruktose.setChecked(fStatus);
    }

    private void updateProductList() {
        List<String> filerKeyWords = new ArrayList<>();
        if (switchVegan.isChecked()) {
            filerKeyWords.add("vegan");
        }
        if (switchLaktose.isChecked()) {
            filerKeyWords.add("laktosefrei");
        }
        if (switchGluten.isChecked()) {
            filerKeyWords.add("glutenfrei");
        }
        if (switchFruktose.isChecked()) {
            filerKeyWords.add("fruktosefrei");
        }

        products = filterProducts(allProducts, filerKeyWords.toArray(new String[0]));
        adapter.setProducts(products);
        adapter.notifyDataSetChanged();
    }

    private List<Product> filterProducts(List<Product> allProducts, String... filterKeyWords) {
        List<Product> filteredList = new ArrayList<>();

        if (filterKeyWords.length == 0) {
            filteredList.addAll(allProducts);
        } else {
            for (Product item : allProducts) {
                StringBuilder builderAllergen = new StringBuilder();
                for (String allergen : item.getAllergen()) {
                    builderAllergen.append(allergen.toLowerCase());
                    builderAllergen.append(" | ");
                }

                String allAllergens = builderAllergen.toString();
                boolean isDeclared = true;
                for (String filterKeyWord : filterKeyWords) {
                    if (!allAllergens.contains(filterKeyWord)) {
                        isDeclared = false;
                        break;
                    }
                }

                if (isDeclared) {
                    filteredList.add(item);
                }
            }
        }
        return filteredList;
    }

}
