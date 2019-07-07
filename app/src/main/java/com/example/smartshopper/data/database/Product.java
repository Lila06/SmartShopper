package com.example.smartshopper.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.smartshopper.data.util.ProductDecoder;

import java.util.List;

@Entity(tableName = "products")
public class Product {

    @PrimaryKey
    public long ean = 0L;

    public String data = "";

    public long scanned = 0L;


    public String getName() {
        return findValueOf("name", data.split("\\n"));
    }

    public String getDetailName() {
        return findValueOf("detailname", data.split("\\n"));
    }

    public String getDescription() {
        return findValueOf("descr", data.split("\\n"));
    }

    public List<String> getAllergen() {
        String codeAsString = findValueOf("contents", data.split("\\n"));
        return ProductDecoder.decodeAllergen(codeAsString);
    }

    public List<String> getPackaging() {
        String codeAsString = findValueOf("pack", data.split("\\n"));
        return ProductDecoder.decodePackaging(codeAsString);
    }

    private String findValueOf(String key, String[] source) {
        for (String sourcePart : source) {
            if (sourcePart.trim().startsWith(key)) {
                String[] keyValue = sourcePart.split("=");
                if (keyValue.length > 1) {
                    return keyValue[1].trim();
                }
            }
        }
        return "";
    }
}
