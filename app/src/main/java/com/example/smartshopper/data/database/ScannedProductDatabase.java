package com.example.smartshopper.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1)
public abstract class ScannedProductDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
