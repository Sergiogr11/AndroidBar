package com.example.androidbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidbar.model.Articulo;
import com.example.androidbar.model.LineaComanda;
import com.example.androidbar.network.ApiArticulos;
import com.example.androidbar.network.ApiCategorias;
import com.example.androidbar.network.ApiClient;

public class ArticuloDetalleActivity extends AppCompatActivity {

    private ApiArticulos apiArticulos;

    private TextView nombreArticulo;
    private TextView precioArticulo;
    private TextView descripcionArticulo;
    private TextView cantidad;


    private ImageButton btnMasCantidad;
    private ImageButton btnMenosCantidad;
    private Button btnAddArticulo;

    private Articulo articuloActivo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulos);

        //Inicializo la api de articulos de retrofit
        apiArticulos = ApiClient.getClient().create(ApiArticulos.class);

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

        //Seteo los campos con la información del artículo
        nombreArticulo.setText(articuloActivo.getNombreArticulo());
        descripcionArticulo.setText(articuloActivo.getDescripcionArticulo());
        precioArticulo.setText(String.valueOf(articuloActivo.getPrecio()));
        cantidad.setText(0);

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
                if(cantidadActual > 0) { // Only decrease if quantity is more than 0
                    cantidadActual--; // Decrease the quantity
                    cantidad.setText(String.valueOf(cantidadActual)); // Update the text view
                }
            }
        });


        //Añado listener al boton de añadir articulo, en el cual creo una linea de Comanda con el artículo correspondiente
        btnAddArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidadInt = Integer.parseInt(cantidad.getText().toString());
                float precioTotal = articuloActivo.getPrecio() * cantidadInt;
                LineaComanda lineaComanda = new LineaComanda();
                lineaComanda.setArticuloId(articuloActivo.getArticuloId());
                lineaComanda.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                lineaComanda.setPrecio(precioTotal);


            }
        });
    }
}
