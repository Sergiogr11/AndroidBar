package com.example.androidbar.network;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Mesa;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiArticulos  {

    @POST("findByCategoria")
    Call<List<Articulo>> findByCategoria(@Body RequestBody categoriaId);
}
