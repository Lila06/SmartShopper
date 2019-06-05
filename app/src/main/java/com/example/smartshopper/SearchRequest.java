package com.example.smartshopper;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchRequest implements Callback<String> {

    private static final String BASE_URL = "http://opengtindb.org/";

    private RemoteDatabaseAPI databaseAPI;

    public SearchRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        databaseAPI = retrofit.create(RemoteDatabaseAPI.class);
    }

    public void search(String ean) {
        Call<String> call = databaseAPI.searchProduct(ean);
         call.enqueue(this);
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        Log.e("Debug", "search response: successful: " + response.isSuccessful() + " >>" + response.body());
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Log.e("Debug", "search error: ", t);
    }
}
