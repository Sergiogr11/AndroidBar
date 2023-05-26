package com.example.androidbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Categoria;
import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.LineaComanda;
import com.example.androidbar.model.LineaComandaDTO;
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiComandas;
import com.example.androidbar.network.ApiLineaComanda;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditarComandaActivity extends AppCompatActivity {

    private ApiLineaComanda apiLineaComanda;
    private ApiComandas apiComandas;
    private ApiArticulos apiArticulos;

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
        btnCrear = findViewById(R.id.btnCrear);
        recyclerViewComanda = findViewById(R.id.recyclerViewComanda);

        // Obtener la Comanda Activa
        comandaActiva = (Comanda) getIntent().getSerializableExtra("comanda");

        //Obtener las lineasComanda correspondientes a la Comanda Activa
        obtenerLineasComanda(comandaActiva);

        //Añado funcionalidad a boton Crear Comanda
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Enviar comanda a ImprimirController
            }
        });

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
                            // Obtener el id del elemento a editar
                            LineaComandaDTO lineaComandaDTO = lineaComandaList.get(position);
                            LineaComanda lineaComandaSeleccionada = lineaComandaDTO.getLineaComanda();
                            obtenerArticulo(lineaComandaSeleccionada);
                        }

                        @Override
                        public void onDeleteClick(int position) {
                            borrarLineaComanda(position);
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

    private void obtenerArticulo(LineaComanda lineaComanda){
        int articuloId = lineaComanda.getArticuloId();
        apiArticulos.findArticulo(articuloId).enqueue(new Callback<Articulo>() {
            @Override
            public void onResponse(Call<Articulo> call, Response<Articulo> response) {
                if(response.isSuccessful()) {
                    Articulo articulo = response.body();
                    editarLineaComanda(articulo, lineaComanda);
                } else {
                    Toast.makeText(EditarComandaActivity.this, "Error al obtener articulo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Articulo> call, Throwable t) {
                Toast.makeText(EditarComandaActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void borrarLineaComanda(int position){
        // Obtener el id del elemento a eliminar
        LineaComandaDTO lineaComandaDTO = lineaComandaList.get(position);

        //Obtener la linea comanda a eliminar
        LineaComanda lineaComandaAEliminar = lineaComandaDTO.getLineaComanda();

        // Hacer una llamada a la API para eliminar el elemento de la base de datos

        apiLineaComanda.deleteLineaComanda(lineaComandaAEliminar).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Eliminar el elemento de la lista de la aplicación si la eliminación fue exitosa
                if(response.isSuccessful()) {
                    lineaComandaList.remove(position);
                    recyclerViewComanda.getAdapter().notifyItemRemoved(position);
                } else {
                    Toast.makeText(EditarComandaActivity.this, "Error al borrar la linea de Comanda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditarComandaActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editarLineaComanda(Articulo articulo, LineaComanda lineaComanda){
        Intent intent = new Intent(EditarComandaActivity.this, EditarArticuloDetalleActivity.class);
        intent.putExtra("comanda", comandaActiva);
        intent.putExtra("lineaComanda", lineaComanda);
        intent.putExtra("articulo", articulo);
        startActivity(intent);
    }
}
