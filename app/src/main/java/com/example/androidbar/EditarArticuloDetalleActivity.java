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
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiLineaComanda;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarArticuloDetalleActivity extends AppCompatActivity {

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
    private LineaComanda lineaComandaActiva;
    private Comanda comandaActiva;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_detalle_articulos);

        //Inicializo la api de articulos de retrofit
        apiArticulos = ApiClient.getClient(EditarArticuloDetalleActivity.this).create(ApiArticulos.class);
        apiLineaComanda = ApiClient.getClient(EditarArticuloDetalleActivity.this).create(ApiLineaComanda.class);

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
        lineaComandaActiva = (LineaComanda) getIntent().getSerializableExtra("lineaComanda");

        //Seteo los campos con la información del artículo
        nombreArticulo.setText(articuloActivo.getNombreArticulo());
        descripcionArticulo.setText(articuloActivo.getDescripcionArticulo());
        precioArticulo.setText(String.valueOf(articuloActivo.getPrecio()));
        cantidad.setText(String.valueOf(lineaComandaActiva.getCantidad()));

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

                lineaComandaActiva.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                lineaComandaActiva.setPrecio(precioTotal);

                guardarLineaComanda(lineaComandaActiva);

            }
        });
    }


    private void guardarLineaComanda(LineaComanda lineaComanda){
        Call<ResponseBody> updateLineaComandaCall = apiLineaComanda.updateLineaComanda(lineaComanda);

        updateLineaComandaCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    volverEditarComanda();
                    //Toast.makeText(EditarArticuloDetalleActivity.this, "Linea Comanda actualizada correctamente ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarArticuloDetalleActivity.this, "Error al actualizar linea de comanda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Ocurrió un error en la comunicación con el servidor
                Toast.makeText(EditarArticuloDetalleActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void volverEditarComanda(){
        Intent intent = new Intent(EditarArticuloDetalleActivity.this, EditarComandaActivity.class);
        intent.putExtra("comanda", comandaActiva);
        startActivity(intent);
    }
}
