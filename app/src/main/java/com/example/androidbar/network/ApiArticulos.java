package com.example.androidbar.network;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Mesa;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiArticulos  {

    @POST("findArticulosbyCategoria/{categoriaId}")
    Call<List<Articulo>> findArticulosbyCategoria(@Path("categoriaId") int categoriaId);


}
