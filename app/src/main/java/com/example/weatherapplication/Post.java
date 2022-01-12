package com.example.weatherapplication;

import com.example.weatherapplication.model.GetWeatherDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Post {
    @GET("forecast.json")
    Call<GetWeatherDetails> getWeatherDetails(@Query("key") String key, @Query("q") String city, @Query("days") String days, @Query("aqi") String aqi, @Query("alerts") String alerts);
}
