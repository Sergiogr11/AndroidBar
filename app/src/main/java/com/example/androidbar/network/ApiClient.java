package com.example.androidbar.network;

import android.content.Context;

import com.example.androidbar.model.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    public static Retrofit getClient(Context context) {
        String ipAddress = Config.loadIpAddress(context);
        int port = Config.loadPort(context);

        String baseUrl = "http://" + ipAddress + ":" + port;

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
