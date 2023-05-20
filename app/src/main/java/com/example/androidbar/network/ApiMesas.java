package com.example.androidbar.network;

import com.example.androidbar.model.Mesa;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiMesas {


    @POST("findMesasByPosicion")
    Call<List<Mesa>> findMesasByPosicion(@Body RequestBody posicion);

}
