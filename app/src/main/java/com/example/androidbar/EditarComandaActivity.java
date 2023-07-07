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
import com.example.androidbar.network.ApiImpresora;
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
    private ApiImpresora apiImpresora;

    private Button btnCrear;
    private RecyclerView recyclerViewComanda;

    private Comanda comandaActiva;
    private List<LineaComandaDTO> lineaComandaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_comanda);

        //Inicializo la api correspondientes
        apiComandas = ApiClient.getClient(EditarComandaActivity.this).create(ApiComandas.class);
        apiLineaComanda = ApiClient.getClient(EditarComandaActivity.this).create(ApiLineaComanda.class);
        apiArticulos = ApiClient.getClient(EditarComandaActivity.this).create(ApiArticulos.class);
        apiImpresora = ApiClient.getClient(EditarComandaActivity.this).create(ApiImpresora.class);

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
                imprimirBebidayComida(comandaActiva);
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

    private void imprimirBebidayComida(Comanda comanda){
        apiImpresora.imprimirBebidayComida(comanda).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    actualizarPrecioComanda(comandaActiva);
                    volverMesas();
                    Toast.makeText(EditarComandaActivity.this, "Comanda Enviada Correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarComandaActivity.this, "Error al enviar comanda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditarComandaActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarPrecioComanda(Comanda comanda){
        float precioTotal = 0;

        // Calcular la suma de los precios de todas las líneas de comanda
        for (LineaComandaDTO lineaComandaDTO : lineaComandaList) {
            LineaComanda lineaComanda = lineaComandaDTO.getLineaComanda();
            precioTotal += lineaComanda.getPrecio();
        }

        // Actualizar el precio de la comanda
        comanda.setPrecioTotal(precioTotal);

        // Llamar a la API para actualizar la comanda en la base de datos
        apiComandas.updateComanda(comanda).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarComandaActivity.this, "Precio de comanda actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarComandaActivity.this, "Error al actualizar el precio de la comanda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditarComandaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void volverMesas(){
        Intent intent = new Intent(EditarComandaActivity.this, MesasActivity.class);
        intent.putExtra("usuario",  comandaActiva.getUsuarioId());
        startActivity(intent);
    }
}
