package com.example.androidbar.network;


import com.example.androidbar.model.Usuario;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiUsuarios {

    @FormUrlEncoded
    @POST("/login")
    Call<Boolean> login(@Field(value = "username", encoded = true) String username, @Field(value = "password", encoded = true) String password);
}
