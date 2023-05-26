package com.example.androidbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Categoria;
import com.example.androidbar.model.Comanda;
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiCategorias;
import com.example.androidbar.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaArticulosActivity extends AppCompatActivity {

    private ListView listViewArticulos;
    private Button btnEditar;
    private TextView mesaActiva;


    private ApiCategorias apiCategorias;
    private ApiArticulos apiArticulos;

    private String mesaSeleccionada;

    private List<Categoria> categorias;
    private List<Articulo> articulos;

    private boolean showingCategories = true;

    private Comanda comandaActiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulos);

        //Inicializo la api de categorias y articulos de retrofit
        apiCategorias = ApiClient.getClient().create(ApiCategorias.class);
        apiArticulos = ApiClient.getClient().create(ApiArticulos.class);

        // Obtener referencias a los elementos de la interfaz de usuario
        listViewArticulos = findViewById(R.id.listViewArticulos);
        btnEditar = findViewById(R.id.btnEditar);
        mesaActiva = findViewById(R.id.mesaActiva);

        // Obtener la Mesa para setearla en la parte superior
        mesaSeleccionada = (String) getIntent().getSerializableExtra("mesa");
        mesaActiva.setText("Mesa " + mesaSeleccionada);

        // Obtener la Comanda Activa
        comandaActiva = (Comanda) getIntent().getSerializableExtra("comanda");

        // Inicializar las listas de categorías y artículos
        categorias = new ArrayList<>();
        articulos = new ArrayList<>();

        // Configurar el adaptador para la lista de artículos
        ArrayAdapter<Articulo> adapter = new ArrayAdapter<>(this, R.layout.layout_celdas, articulos);
        listViewArticulos.setAdapter(adapter);

        // Cargar las categorías desde la base de datos
        cargarCategorias();

        // Configurar el listener para la selección de categorías
        listViewArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (showingCategories) {
                    // Obtener la categoría seleccionada
                    Categoria categoriaSeleccionada = categorias.get(position);
                    // Obtener los artículos correspondientes a la categoría seleccionada
                    obtenerArticulosPorCategoria(categoriaSeleccionada.getCategoriaId());
                } else {
                    // Si se están mostrando los artículos, obtener el ID del artículo seleccionado y abrir su página
                    Articulo articuloSeleccionado = articulos.get(position);
                    obtenerDetalleArticulo(articuloSeleccionado);
                }
            }
        });

        //Si btnEditar se presiona se redirige a editar comanda
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigir a la pantalla de edición de comanda
                Intent intent = new Intent(ListaArticulosActivity.this, EditarComandaActivity.class);
                intent.putExtra("mesa", mesaSeleccionada);
                intent.putExtra("comanda", comandaActiva);
                startActivity(intent);
            }
        });

    }

    private void cargarCategorias() {
        showingCategories = true;
        Call<List<Categoria>> call = apiCategorias.readCategoria();
            call.enqueue(new Callback<List<Categoria>>() {
                @Override
                public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                    if (response.isSuccessful()) {
                        categorias = response.body();
                        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(ListaArticulosActivity.this, R.layout.layout_celdas, categorias);
                        listViewArticulos.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Categoria>> call, Throwable t) {
                    Toast.makeText(ListaArticulosActivity.this, "No hay categorias disponibles ", Toast.LENGTH_SHORT).show();
                }
            });
    }



    private void obtenerArticulosPorCategoria(Integer categoriaId) {
        showingCategories = false;

        Call<List<Articulo>> call = apiArticulos.findArticulosbyCategoria(categoriaId);
            call.enqueue(new Callback<List<Articulo>>() {
                @Override
                public void onResponse(Call<List<Articulo>> call, Response<List<Articulo>> response) {
                    if (response.isSuccessful()) {
                        articulos = response.body();
                        ArrayAdapter<Articulo> adapter = new ArrayAdapter<>(ListaArticulosActivity.this, R.layout.layout_celdas, articulos);
                        listViewArticulos.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Articulo>> call, Throwable t) {
                    Toast.makeText(ListaArticulosActivity.this, "No hay articulos disponibles ", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onBackPressed() {
        if (showingCategories) {
            // Si ya estamos mostrando categorías, hacer la acción predeterminada (probablemente cerrar la actividad)
            super.onBackPressed();
        } else {
            // Si está mostrando artículos, volver a cargar las categorías
            cargarCategorias();
            showingCategories = true;
        }
    }

    public void obtenerDetalleArticulo(Articulo articulo){

        Intent intent = new Intent(ListaArticulosActivity.this, ArticuloDetalleActivity.class);
        intent.putExtra("articulo", articulo);
        intent.putExtra("mesa", mesaSeleccionada);
        intent.putExtra("comanda", comandaActiva);
        startActivity(intent);
    }

}
