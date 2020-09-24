package com.example.cropdoctor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherinterface {

    @GET("weather")
    Call<Example> getweather(@Query("lat")String lat,@Query("lon")String lon, @Query("appid")String apikey);
}
