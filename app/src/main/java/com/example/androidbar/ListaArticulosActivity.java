package com.example.androidbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Categoria;
import com.example.androidbar.model.Mesa;
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiCategorias;
import com.example.androidbar.network.ApiMesas;

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
    private Button btnRealizarPedido;
    private ApiCategorias apiCategorias;
    private ApiArticulos apiArticulos;

    private List<Categoria> categorias;
    private List<Articulo> articulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulos);

        // Obtener referencias a los elementos de la interfaz de usuario
        listViewArticulos = findViewById(R.id.listViewArticulos);
        btnEditar = findViewById(R.id.btnEditar);
        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);

        // Inicializar las listas de categorías y artículos
        categorias = new ArrayList<>();
        articulos = new ArrayList<>();

        // Configurar el adaptador para la lista de artículos
        ArrayAdapter<Articulo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, articulos);
        listViewArticulos.setAdapter(adapter);

        // Cargar las categorías desde la base de datos
        cargarCategorias();

        // Configurar el listener para la selección de categorías
        listViewArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la categoría seleccionada
                Categoria categoriaSeleccionada = categorias.get(position);

                // Obtener los artículos correspondientes a la categoría seleccionada
                obtenerArticulosPorCategoria(categoriaSeleccionada.getCategoriaId());
            }
        });

        // Configurar el listener para el botón "Editar Comanda"
        btnEditar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            /*
                // Implementar la lógica para el botón "Editar Comanda"
                // Redirigir a la pantalla de edición de comanda
                Intent intent = new Intent(MainActivity.this, EditarComandaActivity.class);
                startActivity(intent);
            */
            }
        });

        // Configurar el listener para el botón "Realizar Pedido"
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                // Implementar la lógica para el botón "Realizar Pedido"
                // Redirigir a la pantalla de realización de pedido
                Intent intent = new Intent(MainActivity.this, RealizarPedidoActivity.class);
                startActivity(intent);
                 */
            }

        });
    }

    private void cargarCategorias() {
        //Realizo la llamada a mi api para encontrar las categorias existentes
        Call<List<Categoria>> call = apiCategorias.readCategoria();
        call.enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful()) {
                    categorias = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(ListaArticulosActivity.this, "No hay categorias disponibles ", Toast.LENGTH_SHORT).show();
            }
        });

        // Actualizar la lista de categorías en la interfaz de usuario
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        listViewArticulos.setAdapter(adapter);
    }


    private void obtenerArticulosPorCategoria(Integer categoriaId) {
        //Creo requestBody para enviar la peticion
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody = RequestBody.create(mediaType, String.valueOf(categoriaId));

        // Realizar la consulta a la base de datos para obtener los artículos por categoría
        Call<List<Articulo>> call = apiArticulos.findByCategoria(requestBody);
        call.enqueue(new Callback<List<Articulo>>() {
            @Override
            public void onResponse(Call<List<Articulo>> call, Response<List<Articulo>> response) {
                if (response.isSuccessful()) {
                    articulos = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Articulo>> call, Throwable t) {
                Toast.makeText(ListaArticulosActivity.this, "No hay articulos disponibles ", Toast.LENGTH_SHORT).show();
            }
        });

        // Actualizar la lista de categorías en la interfaz de usuario
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categorias);
        listViewArticulos.setAdapter(adapter);

        // Actualizar la lista de artículos en la interfaz de usuario
        ArrayAdapter<Articulo> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, articulos);
        listViewArticulos.setAdapter(adapter2);
    }
}
