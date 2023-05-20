package com.example.androidbar.network;

import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.Mesa;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiComandas {
    @POST("findLastComandaByMesa")
    Call<Comanda> findLastComandaByMesa(@Field("mesaId") int mesaId);

    @GET("findMaxIdComanda")
    Call<Integer> findMaxIdComanda();

}
