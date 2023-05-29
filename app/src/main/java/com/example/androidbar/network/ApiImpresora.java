package com.example.androidbar.network;

import com.example.androidbar.model.Comanda;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiImpresora {
    @POST("/imprimirBebidayComida")
    Call<ResponseBody> imprimirBebidayComida(@Body Comanda comanda);
}
