package com.example.smartshopper.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey
    public long ean = 0L;

    public String data = "";

    public long scanned = 0L;
}
