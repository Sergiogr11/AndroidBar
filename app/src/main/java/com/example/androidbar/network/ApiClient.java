package com.example.androidbar.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.108:7676")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
