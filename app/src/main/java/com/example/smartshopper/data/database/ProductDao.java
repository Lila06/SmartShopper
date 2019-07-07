package com.example.smartshopper.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(Product... products);

    @Update
    void updateProducts(Product... products);

    @Delete
    void deleteProducts(Product... products);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE ean LIKE :ean ORDER BY scanned DESC")
    Product getProductByEan(long ean);
}
