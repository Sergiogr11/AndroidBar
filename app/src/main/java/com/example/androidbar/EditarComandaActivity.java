package com.example.androidbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbar.model.Categoria;
import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.LineaComanda;
import com.example.androidbar.model.LineaComandaDTO;
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiComandas;
import com.example.androidbar.network.ApiLineaComanda;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditarComandaActivity extends AppCompatActivity {

    private ApiLineaComanda apiLineaComanda;
    private ApiComandas apiComandas;
    private ApiArticulos apiArticulos;

    private Button btnEliminar;
    private Button btnCrear;
    private RecyclerView recyclerViewComanda;

    private Comanda comandaActiva;
    private List<LineaComandaDTO> lineaComandaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_comanda);

        //Inicializo la api de comandas y lineasComanda de retrofit
        apiComandas = ApiClient.getClient().create(ApiComandas.class);
        apiLineaComanda = ApiClient.getClient().create(ApiLineaComanda.class);
        apiArticulos = ApiClient.getClient().create(ApiArticulos.class);

        // Obtener referencias a los elementos de la interfaz de usuario
        btnEliminar = findViewById(R.id.btnEliminar);
        btnCrear = findViewById(R.id.btnCrear);
        recyclerViewComanda = findViewById(R.id.recyclerViewComanda);

        // Obtener la Comanda Activa
        comandaActiva = (Comanda) getIntent().getSerializableExtra("comanda");

        //Obtener las lineasComanda correspondientes a la Comanda Activa
        obtenerLineasComanda(comandaActiva);
    }

    private void obtenerLineasComanda(Comanda comandaActiva){
        Call<List<LineaComandaDTO>> call = apiLineaComanda.findAllWithNombreArticulo(comandaActiva.getNumeroComanda());
        call.enqueue(new Callback<List<LineaComandaDTO>>() {
            @Override
            public void onResponse(Call<List<LineaComandaDTO>> call, Response<List<LineaComandaDTO>> response) {
                if (response.isSuccessful()) {
                    lineaComandaList = response.body();
                    recyclerViewComanda.setLayoutManager(new LinearLayoutManager(EditarComandaActivity.this));
                    recyclerViewComanda.setAdapter(new LineaComandaAdapter(lineaComandaList, new LineaComandaAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            // Aquí puedes manejar la lógica para abrir la otra pantalla
                        }

                        @Override
                        public void onDeleteClick(int position) {
                           borrarLineaComanda();
                        }
                    }));
                }
            }

            @Override
            public void onFailure(Call<List<LineaComandaDTO>> call, Throwable t) {
                Toast.makeText(EditarComandaActivity.this, "No hay lineas de comanda ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarLineaComanda(){

    }

}
