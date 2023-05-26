package com.example.androidbar.network;

import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.LineaComanda;
import com.example.androidbar.model.LineaComandaDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiLineaComanda {

    @POST("/createLineaComanda")
    Call<ResponseBody> createLineaComanda(@Body LineaComanda lineaComanda);

    @PUT("/updateLineaComanda")
    Call<ResponseBody> updateLineaComanda(@Body LineaComanda lineaComanda);

    @HTTP(method = "DELETE", path = "/deleteLineaComanda", hasBody = true)
    Call<ResponseBody> deleteLineaComanda(@Body LineaComanda lineaComanda);

    @GET("/findLastLineaComanda")
    Call<Integer> findLastLineaComanda(@Body Integer comandaId);

    @GET("/findAllByNumeroComanda")
    Call<List<LineaComanda>> findAllByNumeroComanda(@Body Integer comandaId);

    @POST("findAllWithNombreArticulo")
    Call<List<LineaComandaDTO>> findAllWithNombreArticulo(@Body Integer comandaId);

}
