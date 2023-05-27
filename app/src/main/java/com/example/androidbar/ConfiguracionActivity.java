package com.example.androidbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {

    private TextView textViewIp;
    private TextView textViewPort;
    private Button btnGuardar;

    private static final String PREF_NAME = "AppConfig";
    private static final String KEY_IP_ADDRESS = "ipAddress";
    private static final String KEY_PORT = "port";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        // Inicializar las vistas
        textViewIp = findViewById(R.id.ipAddress);
        textViewPort = findViewById(R.id.port);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Cargar los valores guardados en SharedPreferences al iniciar la actividad
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedIpAddress = sharedPreferences.getString(KEY_IP_ADDRESS, "");
        int savedPort = sharedPreferences.getInt(KEY_PORT, 0);
        textViewIp.setText(savedIpAddress);
        textViewPort.setText(String.valueOf(savedPort));

        // Configurar el evento de clic del botón "Guardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAddress = textViewIp.getText().toString();
                String port = textViewPort.getText().toString();

                // Guardar los valores en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Valida que el puerto sea un número y esté en el rango válido
                try {
                    int puerto = Integer.parseInt(port);
                    if (puerto <= 0 || puerto > 65535) {
                        // Lanza una excepción para manejar esto como un caso de error
                        throw new NumberFormatException();
                    }
                    // Si es correcto, guarda puerto
                    editor.putInt(KEY_PORT, puerto);
                } catch (NumberFormatException e) {
                    Toast.makeText(ConfiguracionActivity.this, "Puerto inválido", Toast.LENGTH_SHORT).show();
                }

                // Valida que la IP sea válida
                if (validaIp(ipAddress)) {
                    editor.putString(KEY_IP_ADDRESS, ipAddress);
                } else {
                    Toast.makeText(ConfiguracionActivity.this, "Dirección IP inválida", Toast.LENGTH_SHORT).show();
                }

                editor.apply();

                Toast.makeText(ConfiguracionActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ConfiguracionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validaIp(String ipAddress){
        String IP_ADDRESS_REGEX =
                "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.)){3}(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$";

        return ipAddress.matches(IP_ADDRESS_REGEX);
    }


}
