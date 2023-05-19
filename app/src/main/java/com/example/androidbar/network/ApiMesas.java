package com.example.androidbar.network;

import com.example.androidbar.model.Mesa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface ApiMesas {

    @POST("/findMesasByPosicion")
    Call<List<Mesa>> findMesasByPosicion(@Field(value = "posicion", encoded = true) String posicion);
}
