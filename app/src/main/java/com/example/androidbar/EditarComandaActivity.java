package com.example.androidbar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class EditarComandaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_comanda);

        /*
        //Inicializo la api de categorias y articulos de retrofit
        apiCategorias = ApiClient.getClient().create(ApiCategorias.class);
        apiArticulos = ApiClient.getClient().create(ApiArticulos.class);

        // Obtener referencias a los elementos de la interfaz de usuario
        listViewArticulos = findViewById(R.id.listViewArticulos);
        btnEditar = findViewById(R.id.btnEditar);
        btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        mesaActiva = findViewById(R.id.mesaActiva);

        // Obtener la Mesa para setearla en la parte superior
        mesaSeleccionada = (String) getIntent().getSerializableExtra("mesa");
        mesaActiva.setText("Mesa " + mesaSeleccionada);

        // Obtener la Comanda Activa
        comandaActiva = (Comanda) getIntent().getSerializableExtra("comanda");

        // Inicializar las listas de categorías y artículos
        categorias = new ArrayList<>();
        articulos = new ArrayList<>();
         */
    }
}
