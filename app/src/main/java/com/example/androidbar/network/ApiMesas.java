package com.example.androidbar.network;


import com.example.androidbar.model.Mesa;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiMesas {

    @POST("/findMesasByPosicion")
    Call<List<Mesa>> findMesasByPosicion(@Body RequestBody posicion);

    @PUT("/updateMesa")
    Call<ResponseBody> updateMesa(@Body Mesa mesa);

}
