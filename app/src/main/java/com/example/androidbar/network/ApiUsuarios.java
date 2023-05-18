package com.example.androidbar.network;


import com.example.androidbar.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiUsuarios {

    @GET("readUsuarios")
    Call<List<Usuario>> getUsuarios();
}
