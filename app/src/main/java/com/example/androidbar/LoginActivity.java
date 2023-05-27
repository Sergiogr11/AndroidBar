package com.example.androidbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.androidbar.model.Config;
import com.example.androidbar.model.Usuario;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiUsuarios;

import java.lang.reflect.InvocationTargetException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ImageButton configuracionButton;
    private ApiUsuarios apiUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar las vistas
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.iniciarSesion);
        configuracionButton = findViewById(R.id.btnConfiguracion);

        // Crear instancia de ApiUsuarios utilizando la clase ApiClient
        apiUsuarios = ApiClient.getClient(LoginActivity.this).create(ApiUsuarios.class);

        // Configurar el evento de click del botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de usuario y la contraseña ingresados
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Los campos están vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realizar la llamada al método login de la interfaz ApiUsuarios
                Call<Boolean> call = apiUsuarios.login(username, password);

                // Enviar la solicitud de forma asíncrona
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            boolean loginExitoso = response.body();

                            // Verificar si el inicio de sesión fue exitoso
                            if (loginExitoso) {
                                // Buscar al usuario por su nombre de usuario
                                    Call<Usuario> userCall = apiUsuarios.findUsuario(username);

                                    userCall.enqueue(new Callback<Usuario>() {
                                        @Override
                                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                            if (response.isSuccessful()) {
                                                // Establecer el usuario y continuar a la siguiente actividad
                                                Usuario usuario = response.body();
                                                Intent intent = new Intent(LoginActivity.this, MesasActivity.class);
                                                intent.putExtra("usuario", usuario);
                                                startActivity(intent);
                                            } else {
                                                // Ocurrió un error en la solicitud
                                                Toast.makeText(LoginActivity.this, "Error al obtener usuario", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Usuario> call, Throwable t) {
                                            // Ocurrió un error en la comunicación con el servidor
                                            Toast.makeText(LoginActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else{
                                    // El inicio de sesión falló, mostrar un mensaje de error al usuario
                                    Toast.makeText(LoginActivity.this, "El usuario o contraseña no son correctos", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Ocurrió un error en la solicitud
                                Toast.makeText(LoginActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                            }

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        // Ocurrió un error en la comunicación con el servidor
                        Toast.makeText(LoginActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Configurara el evento de click del botón de configuración
        configuracionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ConfiguracionActivity.class);
                startActivity(intent);
            }
        });
        
    }
}