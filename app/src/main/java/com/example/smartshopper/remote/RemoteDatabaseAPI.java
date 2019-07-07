package com.example.smartshopper.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteDatabaseAPI {

    @GET("?cmd=query&queryid=400000000")
    Call<String> searchProduct(@Query("ean") String ean);

}

