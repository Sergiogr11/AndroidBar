package com.example.androidbar.network;

import com.example.androidbar.model.Categoria;
import com.example.androidbar.model.Mesa;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;


public interface ApiCategorias {

    @GET("readCategoria")
    Call<List<Categoria>> readCategoria();

}
