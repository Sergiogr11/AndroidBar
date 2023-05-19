package com.example.androidbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidbar.model.Usuario;
import com.example.androidbar.network.ApiClient;
import com.example.androidbar.network.ApiUsuarios;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private ApiUsuarios apiUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar las vistas
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.iniciarSesion);

        // Crear instancia de ApiUsuarios utilizando la clase ApiClient
        apiUsuarios = ApiClient.getClient().create(ApiUsuarios.class);

        // Configurar el evento de clic del botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de usuario y la contraseña ingresados
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Usuario usuario = new Usuario();

                usuario.setPassword(password);
                usuario.setUsername(username);

                // Realizar la llamada al método login de la interfaz ApiUsuarios
                Call<Boolean> call = apiUsuarios.login(username, password);

                // Enviar la solicitud de forma asíncrona
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            boolean loginExitoso = response.body();
                            System.out.println(response.body());

                            // Verificar si el inicio de sesión fue exitoso
                            if (loginExitoso) {
                                // Continuar a la siguiente actividad
                                //TODO siguiente actividad
                                Intent intent = new Intent(LoginActivity.this, MesasActivity.class);
                                startActivity(intent);
                                //Toast.makeText(LoginActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                            } else {
                                // El inicio de sesión falló, mostrar un mensaje de error al usuario
                                Toast.makeText(LoginActivity.this, "Inicio de sesión incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Ocurrió un error en la solicitud
                            Toast.makeText(LoginActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        // Ocurrió un error en la comunicación con el servidor
                        Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}