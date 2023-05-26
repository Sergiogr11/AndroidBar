package com.example.androidbar.network;

import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.Mesa;

import java.util.List;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiComandas {
    @GET("/findLastComandaByMesa/{mesaId}")
    Call<Comanda> findLastComandaByMesa(@Path("mesaId") int mesaId);

    @GET("/findMaxIdComanda")
    Call<Integer> findMaxIdComanda();

    @POST("/createComanda")
    Call<ResponseBody> createComanda(@Body Comanda comanda);
}
