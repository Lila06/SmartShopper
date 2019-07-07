package com.example.smartshopper.data;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.room.Room;

import com.example.smartshopper.AppApplication;
import com.example.smartshopper.data.database.Product;
import com.example.smartshopper.data.database.ScannedProductDatabase;

import java.util.List;

public class ScannedProductRepository {
    private static final String TAG = "ScannedProductRepo";
    private ScannedProductDatabase db;
    private Context context;

    public ScannedProductRepository(Context context) {
        this.context = context;
        db = Room
                .databaseBuilder(context, ScannedProductDatabase.class, "database-scanned-products")
                .enableMultiInstanceInvalidation()
                .build();
    }

    public void saveProduct(final long ean, final String data) {
        ((AppApplication) context.getApplicationContext()).getIoHandler().post(new Runnable() {
            @Override
            public void run() {
                Product product = new Product();
                product.ean = ean;
                product.data = data;
                product.scanned = System.currentTimeMillis();
                db.productDao().insertProducts(product);

                Log.i(TAG, "saved Product ean: " + product.ean + "\ndata: " + product.data + "\nscanned: " + product.scanned);
            }
        });

    }

    public void getProduct(final long ean, final GetProductCallback callback) {
        Handler ioHandler = ((AppApplication) context.getApplicationContext()).getIoHandler();
        final Handler mainHandler = ((AppApplication) context.getApplicationContext()).getMainHandler();

        ioHandler.post(new Runnable() {
            @Override
            public void run() {
                final Product product = db.productDao().getProductByEan(ean);

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGetProduct(product);
                    }
                });
            }
        });
    }

    public void getAllProducts(final GetAllProductCallback callback) {
        Handler ioHandler = ((AppApplication) context.getApplicationContext()).getIoHandler();
        final Handler mainHandler = ((AppApplication) context.getApplicationContext()).getMainHandler();

        ioHandler.post(new Runnable() {
            @Override
            public void run() {
                final List<Product> allProducts = db.productDao().getAllProducts();

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onGetAllProducts(allProducts);
                    }
                });
            }
        });
    }


    public interface GetProductCallback {
        void onGetProduct(Product product);
    }

    public interface GetAllProductCallback {
        void onGetAllProducts(List<Product> products);
    }
}
