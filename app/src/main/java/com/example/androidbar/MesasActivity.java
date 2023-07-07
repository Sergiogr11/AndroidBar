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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesasActivity extends AppCompatActivity {

    private ApiMesas apiMesas;
    private FlexboxLayout flexboxLayout;
    private ApiComandas apiComandas;
    private Comanda comandaActiva;
    private int usuarioId;
    private String mesaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas);

        // Obtener el Usuario
        usuarioId = (int) getIntent().getSerializableExtra("usuario");

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

        //Inicializo la api de mesas y comandas de retrofit
        apiMesas = ApiClient.getClient(MesasActivity.this).create(ApiMesas.class);
        apiComandas = ApiClient.getClient(MesasActivity.this).create(ApiComandas.class);
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
        button.setTextColor(Color.BLACK);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        float flexBasisPercent = 0.3f;

        int buttonMarginInDp = 5;
        int buttonHeightInDp = 100;

        int buttonMarginInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonMarginInDp,
                getResources().getDisplayMetrics()
        );

        int buttonHeightInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                buttonHeightInDp,
                getResources().getDisplayMetrics()
        );

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                buttonHeightInPx
        );
        params.setMargins(buttonMarginInPx, buttonMarginInPx, buttonMarginInPx, buttonMarginInPx);
        params.setFlexBasisPercent(flexBasisPercent);
        button.setLayoutParams(params);

        //Depenediendo del estado de la mesa cambio el color del boton
        if (mesa.getEstadoMesa().equals("Libre")) {
            button.setBackgroundColor(Color.parseColor("#00FF00"));
        } else if (mesa.getEstadoMesa().equals("Ocupada")) {
            button.setBackgroundColor(Color.parseColor("#CF0808"));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaSeleccionada = mesa.getNombreMesa();
                // Obtener la comanda de la mesa
                obtenerComandaMesa(mesa);
            }
        });

        return button;
    }

    private void obtenerComandaMesa(Mesa mesa) {
            //Obtengo la comanda asociada a la mesa, si la mesa esta ocupada
            if (mesa.getEstadoMesa().equals("Ocupada")) {
                //Realizo la llamada a mi api para encontrar las mesas de la posicion
                int mesaId = mesa.getMesaId();
                Call<Comanda> call = apiComandas.findLastComandaByMesa(mesaId);
                call.enqueue(new Callback<Comanda>() {
                    @Override
                    public void onResponse(Call<Comanda> call, Response<Comanda> response) {
                        if (response.isSuccessful()) {
                            comandaActiva = response.body();
                            // Iniciar la actividad de la lista de artículos con la comanda obtenida
                            iniciarListaArticulosActivity();
                        } else {
                            Toast.makeText(MesasActivity.this, "Error al obtener comanda asociada ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comanda> call, Throwable t) {
                        Toast.makeText(MesasActivity.this, "No hay comanda asociada ", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Crear una nueva comanda si la mesa está libre
                crearNuevaComanda(mesa, usuarioId);
            }
    }

    // Crear una nueva comanda cuando la mesa está libre
    private void crearNuevaComanda(Mesa mesa, int usuarioId) {
        comandaActiva = new Comanda();
        comandaActiva.setPrecioTotal(0);
        long millis = System.currentTimeMillis();
        comandaActiva.setFechaHoraApertura(millis);
        comandaActiva.setNumeroComensales(mesa.getCapacidad());
        comandaActiva.setUsuarioId(usuarioId);
        comandaActiva.setMesaId(mesa.getMesaId());

        //Guardo la comanda nueva en la base de datos
        guardarComandaBd(comandaActiva, mesa);


    }


    // Método para iniciar la actividad de la lista de artículos
    private void iniciarListaArticulosActivity() {
        Intent intent = new Intent(MesasActivity.this, ListaArticulosActivity.class);
        if (comandaActiva != null) {
            intent.putExtra("mesa", mesaSeleccionada);
            intent.putExtra("comanda", comandaActiva);
        }
        startActivity(intent);
    }

    private void guardarComandaBd(Comanda comanda, Mesa mesa){
            //Realizo la llamada a mi api para crear la comanda
            Call<ResponseBody> callcrearComanda = apiComandas.createComanda(comanda);
            callcrearComanda.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(MesasActivity.this, "Comanda creada correctamente", Toast.LENGTH_SHORT).show();


                    //Pongo la mesa en estado Ocupada
                    mesa.setEstadoMesa("Ocupada");

                    //Actualizo el estado de la mesa en la bbdd
                    actualizarEstadoMesa(mesa);

                    // Obtengo la comanda recién creada
                    obtenerComandaNueva(mesa);

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(MesasActivity.this, "Error al crear comanda", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void actualizarEstadoMesa(Mesa mesa) {
            //Realizo la llamada a mi api para actualizar la mesa
            Call<ResponseBody> callupdateMesa = apiMesas.updateMesa(mesa);
            callupdateMesa.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(MesasActivity.this, "Mesa actualizada correctamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MesasActivity.this, "Error al actualizar mesa", Toast.LENGTH_SHORT).show();
                }
            });
        }

    private void obtenerComandaNueva(Mesa mesa) {
        // Realizo la llamada a mi API para encontrar la última comanda de la mesa
        int mesaId = mesa.getMesaId();
        Call<Comanda> call = apiComandas.findLastComandaByMesa(mesaId);
        call.enqueue(new Callback<Comanda>() {
            @Override
            public void onResponse(Call<Comanda> call, Response<Comanda> response) {
                if (response.isSuccessful()) {
                    comandaActiva = response.body();
                    // Iniciar la actividad de la lista de artículos con la comanda obtenida
                    iniciarListaArticulosActivity();
                } else {
                    response.errorBody();
                    Toast.makeText(MesasActivity.this, "Error al obtener comanda asociada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comanda> call, Throwable t) {
                Toast.makeText(MesasActivity.this, "No hay comanda asociada", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
