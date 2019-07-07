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

    @Query("SELECT * FROM products WHERE ean LIKE :ean")
    Product getProductByEan(long ean);

    @Query("SELECT * FROM products WHERE ean IN (:eans)")
    List<Product> getAllProductsByEans(long[] eans);
}
