package com.example.weatherapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit = null;

    public static Post getClient() {
        String baseURL = "http://api.weatherapi.com/v1/";

        //Gson Builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //Retrofit Builder
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getHttpClient())
                .build();

        //Creating object for our interface
        Post api = retrofit.create(Post.class);
        return api; // return the Post object
    }

    //get http client and initialize http logging
    public static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(logLevel);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20000, TimeUnit.SECONDS)
                .readTimeout(20000, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }

}
