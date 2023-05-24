package com.example.androidbar.network;

import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.LineaComanda;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiLineaComanda {

    @POST("/createLineaComanda")
    Call<String> createLineaComanda(@Body LineaComanda lineaComanda);

    @PUT("/updateLineaComanda")
    Call<String> updateLineaComanda(@Body LineaComanda lineaComanda);

    @DELETE("/deleteLineaComanda")
    Call<String> deleteLineaComanda(@Body LineaComanda lineaComanda);

    @POST("/findLastLineaComanda")
    Call<Integer> findLastLineaComanda(@Body Integer comandaId);
}
