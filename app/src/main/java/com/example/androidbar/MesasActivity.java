package com.example.androidbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidbar.model.Mesa;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiMesas;
import com.example.androidbar.network.ApiUsuarios;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesasActivity extends AppCompatActivity {

    private ApiMesas apiMesas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        //Asigno los ids de todos los elementos
        Button restauranteButton = findViewById(R.id.btnRestaurante);
        Button barButton = findViewById(R.id.btnBar);
        Button terrazaButton = findViewById(R.id.btnTerraza);

        //Añado funcion a los botones
        restauranteButton.setOnClickListener(v -> actualizarMesas("restaurante"));
        barButton.setOnClickListener(v -> actualizarMesas("bar"));
        terrazaButton.setOnClickListener(v -> actualizarMesas("terraza"));
        //Inicializo la api de mesas de retrofit
        apiMesas = ApiClient.getClient().create(ApiMesas.class);
    }

    private void actualizarMesas(String posicion) {
        //Realizo la llamada a mi api para encontrar las mesas de la posicion
        Call<List<Mesa>> call = apiMesas.findMesasByPosicion(posicion);
        call.enqueue(new Callback<List<Mesa>>() {
            @Override
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful()) {
                    List<Mesa> mesas = response.body();
                    actualizarUI(mesas);
                }
            }

            @Override
            public void onFailure(Call<List<Mesa>> call, Throwable t) {
                //Muestro mensaje no hay mesas
            }
        });
    }

    private void actualizarUI(List<Mesa> mesas) {


        // Agregar los botones al ConstraintLayout y guardar sus IDs en una lista
        for (Mesa mesa : mesas) {

        }
    }
}