package activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanagerpassword.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import models.UserPass;

public class ShowPasswordsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PasswordAdapter passwordAdapter;
    private Button backButton, addButton;
    private List<UserPass> passwordList = new ArrayList<>();
    private FirebaseAuth mAuth;

    // Instancia de KeyguardManager
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_passwords_activity);

        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);
        recyclerView = findViewById(R.id.passwordRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(v -> {
            // Cerrar la sesión
            FirebaseAuth.getInstance().signOut();

            // Redirigir al login
            Intent intent = new Intent(ShowPasswordsActivity.this, MainActivity.class); // Asegúrate de que LoginActivity sea la actividad de inicio de sesión
            startActivity(intent);
            finish(); // Finaliza la actividad actual para que no se quede en el stack
        });

        // Implementar el onClick para el botón "Add"
        addButton.setOnClickListener(v -> {
            // Iniciar la actividad AddEditPasswordActivity al presionar el botón "Add"
            Intent intent = new Intent(ShowPasswordsActivity.this, AddEditPasswordActivity.class);
            startActivity(intent);
        });

        // Obtener la instancia de KeyguardManager
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        // Verificar si el dispositivo tiene un método de bloqueo configurado
        if (keyguardManager.isKeyguardSecure()) {
            // Solicitar la autenticación
            authenticateUser();
        } else {
            Toast.makeText(this, "Configura un patrón, PIN o huella dactilar", Toast.LENGTH_SHORT).show();
        }
    }

    private void authenticateUser() {
        // Verificar si la autenticación es exitosa
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(this, new KeyguardManager.KeyguardDismissCallback() {
                @Override
                public void onDismissCancelled() {
                    super.onDismissCancelled();
                    Toast.makeText(ShowPasswordsActivity.this, "Autenticación cancelada", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDismissSucceeded() {
                    super.onDismissSucceeded();
                    // Si la autenticación fue exitosa, cargar las contraseñas
                    loadPasswords();
                }
            });
        }
    }

    private void loadPasswords() {
        String userId = mAuth.getCurrentUser().getUid();

        // Obtener una referencia a la base de datos de Firebase Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("passwords").child(userId);

        // Leemos los datos del nodo "passwords" para el usuario autenticado
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpiamos la lista de contraseñas antes de cargar los nuevos datos
                passwordList.clear();

                // Iteramos sobre los datos recuperados
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extraemos los datos del snapshot
                    String idTarjeta = snapshot.child("idTarjeta").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String apuntes = snapshot.child("apuntes").getValue(String.class);
                    String pass = snapshot.child("pass").getValue(String.class);  // Desencriptar si es necesario
                    String nombrePagina = snapshot.child("nombrePagina").getValue(String.class);
                    String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);

                    // Creamos un objeto UserPass con los datos obtenidos
                    UserPass passwordObj = new UserPass(idTarjeta, id, apuntes, pass, nombrePagina, nombreUsuario);

                    // Agregamos el objeto a la lista
                    passwordList.add(passwordObj);
                }

                // Creamos el adaptador con la lista de contraseñas y lo asignamos al RecyclerView
                passwordAdapter = new PasswordAdapter(passwordList);
                recyclerView.setAdapter(passwordAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Si hay un error al leer los datos, mostramos un mensaje
                Toast.makeText(ShowPasswordsActivity.this, "Error al cargar las contraseñas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
