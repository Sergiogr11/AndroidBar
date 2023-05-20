package com.example.androidbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.Mesa;
import com.example.androidbar.model.Usuario;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiComandas;
import com.example.androidbar.network.ApiMesas;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesasActivity extends AppCompatActivity {

    private ApiMesas apiMesas;
    private FlexboxLayout flexboxLayout;
    private ApiComandas apiComandas;
    private Comanda comandaActiva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        //Asigno los ids de todos los elementos
        flexboxLayout = findViewById(R.id.flexboxLayout);
        Button restauranteButton = findViewById(R.id.btnRestaurante);
        Button barButton = findViewById(R.id.btnBar);
        Button terrazaButton = findViewById(R.id.btnTerraza);

        // Configurar el wrap en el FlexboxLayout
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);

        //Añado funcion a los botones
        restauranteButton.setOnClickListener(v -> actualizarMesas("Restaurante"));
        barButton.setOnClickListener(v -> actualizarMesas("Bar"));
        terrazaButton.setOnClickListener(v -> actualizarMesas("Terraza"));

        //Inicializo la api de mesas de retrofit
        apiMesas = ApiClient.getClient().create(ApiMesas.class);
    }

    private void actualizarMesas(String posicion) {
        //Creo requestBody para enviar la peticion
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody = RequestBody.create(mediaType, posicion);

        //Realizo la llamada a mi api para encontrar las mesas de la posicion
        Call<List<Mesa>> call = apiMesas.findMesasByPosicion(requestBody);
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
                Toast.makeText(MesasActivity.this, "No hay mesas disponibles ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarUI(List<Mesa> mesas) {
        flexboxLayout.removeAllViews();

        for (Mesa mesa : mesas) {
            //Creo el boton
            Button button = createButton(mesa);
            flexboxLayout.addView(button);
        }
    }

    private Button createButton(final Mesa mesa) {
        Button button = new Button(this);
        button.setText(mesa.getNombreMesa());

        //Depenediendo del estado de la mesa cambio el color del boton
        if (mesa.getEstadoMesa().equals("Libre")) {
            button.setBackgroundColor(Color.parseColor("#00FF00"));
        } else if (mesa.getEstadoMesa().equals("Ocupada")) {
            button.setBackgroundColor(Color.parseColor("#CF0808"));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Obtengo comanda mesa
                Comanda comanda = obtenerComandaMesa(mesa);

                // Crear un intent y agregar la comanda como extra
                Intent intent = new Intent(MesasActivity.this, ListaArticulosActivity.class);
                if (comanda != null) {
                    intent.putExtra("comanda", comanda);
                }

                // Iniciar la siguiente actividad
                startActivity(intent);

                // Realizar acción al hacer clic en la mesa
                //Toast.makeText(MesasActivity.this, "Mesa seleccionada: " + mesa.getNombreMesa(), Toast.LENGTH_SHORT).show();
            }
        });

        int buttonSizeInDp = 105;
        int buttonMarginInDp = 5;
        int buttonSizeInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonSizeInDp,
                getResources().getDisplayMetrics()
        );
        int buttonMarginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonMarginInDp,
                getResources().getDisplayMetrics()
        );

        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(buttonSizeInPx, buttonSizeInPx);
        layoutParams.setMargins(buttonMarginInPx, buttonMarginInPx, buttonMarginInPx, buttonMarginInPx);
        button.setLayoutParams(layoutParams);


        return button;
    }

    private Comanda obtenerComandaMesa(Mesa mesa) {
        //Obtengo la comanda asociada a la mesa, si la mesa esta ocupada
        if (mesa.getEstadoMesa().equals("Ocupada")) {
            //Realizo la llamada a mi api para encontrar las mesas de la posicion
            Call<Comanda> call = apiComandas.findLastComandaByMesa(mesa.getMesaId());
            call.enqueue(new Callback<Comanda>() {
                @Override
                public void onResponse(Call<Comanda> call, Response<Comanda> response) {
                    if (response.isSuccessful()) {
                        comandaActiva = response.body();
                    }
                }

                @Override
                public void onFailure(Call<Comanda> call, Throwable t) {
                    Toast.makeText(MesasActivity.this, "No hay comanda asociada ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            LocalDateTime now = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now = LocalDateTime.now();
            }

            try {
                // Obtengo el máximo ID de comanda desde la API
                Call<Integer> callMaxId = apiComandas.findMaxIdComanda();
                Response<Integer> responseMaxId = callMaxId.execute();

                if (responseMaxId.isSuccessful()) {
                    int comandaId = responseMaxId.body();

                    comandaActiva = new Comanda();
                    comandaActiva.setNumeroComanda(comandaId);
                    comandaActiva.setPrecioTotal(0);
                    comandaActiva.setFechaHoraApertura(now);
                    comandaActiva.setNumeroComensales(mesa.getCapacidad());
                    comandaActiva.setUsuarioId(usuario.getUserId());
                    comandaActiva.setMesaId(mesa.getMesaId());
                    System.out.println(comandaActiva);
                }else{
                    Toast.makeText(MesasActivity.this, "Error al obtener el máximo ID de comanda", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(MesasActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        }
        return comandaActiva;
    }

}