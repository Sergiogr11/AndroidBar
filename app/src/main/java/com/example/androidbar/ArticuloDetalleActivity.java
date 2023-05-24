package com.example.androidbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.Comanda;
import com.example.androidbar.model.LineaComanda;
import com.example.androidbar.model.LineaComandaId;
import com.example.androidbar.model.Usuario;
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiCategorias;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiLineaComanda;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticuloDetalleActivity extends AppCompatActivity {

    private ApiArticulos apiArticulos;
    private ApiLineaComanda apiLineaComanda;

    private TextView nombreArticulo;
    private TextView precioArticulo;
    private TextView descripcionArticulo;
    private TextView cantidad;


    private ImageButton btnMasCantidad;
    private ImageButton btnMenosCantidad;
    private Button btnAddArticulo;

    private Articulo articuloActivo;
    private Comanda comandaActiva;
    private String mesaSeleccionada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulos);

        //Inicializo la api de articulos de retrofit
        apiArticulos = ApiClient.getClient().create(ApiArticulos.class);
        apiLineaComanda = ApiClient.getClient().create(ApiLineaComanda.class);

        // Obtener referencias a los elementos de la interfaz de usuario
        nombreArticulo = findViewById(R.id.nombreArticulo);
        descripcionArticulo = findViewById(R.id.descripcionArticulo);
        precioArticulo = findViewById(R.id.precioArticulo);
        cantidad = findViewById(R.id.cantidad);
        btnMasCantidad = findViewById(R.id.btnMasCantidad);
        btnMenosCantidad = findViewById(R.id.btnMenosCantidad);
        btnAddArticulo = findViewById(R.id.btnAddArticulo);

        // Obtener el Articulo correspondiente
        articuloActivo = (Articulo) getIntent().getSerializableExtra("articulo");
        comandaActiva = (Comanda) getIntent().getSerializableExtra("comanda");
        mesaSeleccionada = (String) getIntent().getSerializableExtra("mesa");

        //Seteo los campos con la información del artículo
        nombreArticulo.setText(articuloActivo.getNombreArticulo());
        descripcionArticulo.setText(articuloActivo.getDescripcionArticulo());
        precioArticulo.setText(String.valueOf(articuloActivo.getPrecio()));
        cantidad.setText(String.valueOf(1));

        //Añado listeners a los botones de aumentar y disminuir cantidad
        btnMasCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidadActual = Integer.parseInt(cantidad.getText().toString());
                cantidadActual++;  // Increase the quantity
                cantidad.setText(String.valueOf(cantidadActual)); // Update the text view
            }
        });

        btnMenosCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidadActual = Integer.parseInt(cantidad.getText().toString());
                if(cantidadActual > 1) { // Decrementa si es mayor de 1
                    cantidadActual--;
                    cantidad.setText(String.valueOf(cantidadActual)); // Actualizo el textview
                }
            }
        });


        //Añado listener al boton de añadir articulo, en el cual creo una linea de Comanda con el artículo correspondiente
        btnAddArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtengo el precio y la cantidad del articulo
                int cantidadInt = Integer.parseInt(cantidad.getText().toString());
                float precioTotal = articuloActivo.getPrecio() * cantidadInt;

                //Creo el id para la linea de comanda
                LineaComandaId lineaComandaId = new LineaComandaId();
                lineaComandaId.setNumeroComanda(comandaActiva.getNumeroComanda());
                lineaComandaId.setNumeroLinea(obtenerNumeroLineaMax(comandaActiva) + 1);

                //Creo la linea de comanda y la guardo en la bbdd
                LineaComanda lineaComanda = new LineaComanda();
                lineaComanda.setLineaComandaId(lineaComandaId);
                lineaComanda.setArticuloId(articuloActivo.getArticuloId());
                lineaComanda.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                lineaComanda.setPrecio(precioTotal);

                guardarLineaComanda(lineaComanda);

                volverListaArticulos();
            }
        });
    }

    private int obtenerNumeroLineaMax(Comanda comanda){
        Call<Integer> lineaComandaCall = apiLineaComanda.findLastLineaComanda(comanda.getNumeroComanda());
        final int[] maxId = new int[1];

        lineaComandaCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    maxId[0] = response.body();
                } else {
                    Toast.makeText(ArticuloDetalleActivity.this, "Error al obtener maximo linea Comanda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                // Ocurrió un error en la comunicación con el servidor
                Toast.makeText(ArticuloDetalleActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });

        return maxId[0];
    }

    private void guardarLineaComanda(LineaComanda lineaComanda){
        Call<String> crearlineaComandaCall = apiLineaComanda.createLineaComanda(lineaComanda);

        crearlineaComandaCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(ArticuloDetalleActivity.this, "Linea Comanda creada correctamente ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ArticuloDetalleActivity.this, "Error al crear comanda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Ocurrió un error en la comunicación con el servidor
                Toast.makeText(ArticuloDetalleActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void volverListaArticulos(){
        Intent intent = new Intent(ArticuloDetalleActivity.this, ListaArticulosActivity.class);
        intent.putExtra("articulo", articuloActivo);
        intent.putExtra("mesa", mesaSeleccionada);
        intent.putExtra("comanda", comandaActiva);
        startActivity(intent);
    }
}
